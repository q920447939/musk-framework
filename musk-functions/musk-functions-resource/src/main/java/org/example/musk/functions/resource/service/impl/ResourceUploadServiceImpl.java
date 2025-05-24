package org.example.musk.functions.resource.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.resource.config.ResourceProperties;
import org.example.musk.functions.resource.constant.ResourceConstant;
import org.example.musk.functions.resource.dao.ResourceMapper;
import org.example.musk.functions.resource.entity.SystemResourceCategoryDO;
import org.example.musk.functions.resource.entity.SystemResourceDO;
import org.example.musk.functions.resource.enums.ResourceTypeEnum;
import org.example.musk.functions.resource.exception.ResourceException;
import org.example.musk.functions.resource.service.ResourceCategoryService;
import org.example.musk.functions.resource.service.ResourceLogService;
import org.example.musk.functions.resource.service.ResourceSecurityService;
import org.example.musk.functions.resource.service.ResourceUploadService;
import org.example.musk.functions.resource.storage.ResourceStorageStrategy;
import org.example.musk.functions.resource.storage.StorageResult;
import org.example.musk.functions.resource.storage.StorageStrategyFactory;
import org.example.musk.functions.resource.util.ResourceTypeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * 资源上传服务实现
 *
 * @author musk-functions-resource
 */
@Service
@Slf4j
@DS(DBConstant.SYSTEM)
public class ResourceUploadServiceImpl implements ResourceUploadService {

    @Resource
    private ResourceMapper resourceMapper;

    @Resource
    private ResourceSecurityService resourceSecurityService;

    @Resource
    private ResourceLogService resourceLogService;

    @Resource
    private StorageStrategyFactory storageStrategyFactory;

    @Resource
    private ResourceProperties resourceProperties;

    @Resource
    private ResourceCategoryService resourceCategoryService;

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public SystemResourceDO uploadFile(MultipartFile file, Integer categoryId, String tags, String description) {
        try {
            if (null == file || file.isEmpty()) {
                throw new ResourceException(ResourceException.FILE_CANNOT_BE_EMPTY, categoryId);
            }
            // 1. 安全检查
            resourceSecurityService.checkFileType(file);
            resourceSecurityService.checkFileSize(file);

            // 2. 计算MD5，检查文件是否已存在
            String md5 = calculateMd5(file);
            LambdaQueryWrapper<SystemResourceDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SystemResourceDO::getMd5, md5);
            SystemResourceDO existResource = resourceMapper.selectOne(queryWrapper);
            if (existResource != null && !existResource.getDeleted()) {
                // 文件已存在，直接返回
                return existResource;
            }

            SystemResourceCategoryDO category = resourceCategoryService.getCategory(categoryId);
            if (category == null) {
                throw new ResourceException(ResourceException.CATEGORY_NOT_FOUND, categoryId);
            }

            // 3. 生成文件名
            String originalFilename = file.getOriginalFilename();
            String fileType = FilenameUtils.getExtension(originalFilename);
            String fileName = generateFileName(fileType);

            // 4. 获取存储策略
            Integer storageType = resourceProperties.getStorage().getDefaultType();
            ResourceStorageStrategy strategy = storageStrategyFactory.getStrategy(storageType);

            // 5. 存储文件
            StorageResult storageResult = strategy.store(file.getInputStream(), fileName, file.getSize(), fileType);
            if (!storageResult.getSuccess()) {
                throw new ResourceException(ResourceException.FILE_UPLOAD_FAILED, storageResult.getErrorMessage());
            }

            // 6. 保存资源记录
            SystemResourceDO resource = createResourceDO(
                    originalFilename,
                    ResourceTypeUtils.getResourceTypeByFileType(fileType),
                    fileType,
                    file.getSize(),
                    storageType,
                    storageResult.getStoragePath(),
                    storageResult.getAccessUrl(),
                    md5,
                    categoryId,
                    tags,
                    description
            );
            resourceMapper.insert(resource);

            // 7. 记录操作日志
            resourceLogService.logResourceOperation(resource.getId(), 1, true, null);

            return resource;
        } catch (IOException e) {
            log.error("上传文件失败", e);
            throw new ResourceException(ResourceException.FILE_UPLOAD_FAILED, e, e.getMessage());
        }
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public List<SystemResourceDO> batchUploadFiles(List<MultipartFile> files, Integer categoryId, String tags, String description) {
        List<SystemResourceDO> resources = new ArrayList<>();
        for (MultipartFile file : files) {
            SystemResourceDO resource = uploadFile(file, categoryId, tags, description);
            resources.add(resource);
        }
        return resources;
    }

    @Override
    public boolean uploadChunk(MultipartFile file, String fileName, Integer chunkIndex, Integer chunks, String md5) {
        try {
            // 1. 安全检查
            resourceSecurityService.checkFileType(file);

            // 2. 创建分片目录
            String chunkDirPath = getChunkDirPath(md5);
            File chunkDir = new File(chunkDirPath);
            if (!chunkDir.exists()) {
                chunkDir.mkdirs();
            }

            // 3. 保存分片文件
            String chunkFilePath = chunkDirPath + File.separator + chunkIndex;
            File chunkFile = new File(chunkFilePath);
            file.transferTo(chunkFile);

            return true;
        } catch (IOException e) {
            log.error("上传分片失败", e);
            throw new ResourceException(ResourceException.FILE_UPLOAD_FAILED, e, e.getMessage());
        }
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public SystemResourceDO mergeChunks(String fileName, Integer chunks, String md5, Integer categoryId, String tags, String description) {
        try {
            // 1. 检查分片是否都已上传
            String chunkDirPath = getChunkDirPath(md5);
            File chunkDir = new File(chunkDirPath);
            if (!chunkDir.exists()) {
                throw new ResourceException(ResourceException.FILE_UPLOAD_FAILED, "分片目录不存在");
            }

            File[] chunkFiles = chunkDir.listFiles();
            if (chunkFiles == null || chunkFiles.length != chunks) {
                throw new ResourceException(ResourceException.FILE_UPLOAD_FAILED, "分片数量不匹配");
            }

            // 2. 检查文件是否已存在
            LambdaQueryWrapper<SystemResourceDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SystemResourceDO::getMd5, md5);
            queryWrapper.eq(SystemResourceDO::getTenantId, ThreadLocalTenantContext.getTenantId());
            queryWrapper.eq(SystemResourceDO::getDomainId, ThreadLocalTenantContext.getDomainId());
            SystemResourceDO existResource = resourceMapper.selectOne(queryWrapper);
            if (existResource != null && !existResource.getDeleted()) {
                // 文件已存在，直接返回
                return existResource;
            }

            // 3. 合并分片
            String fileType = FilenameUtils.getExtension(fileName);
            String mergedFileName = generateFileName(fileType);
            String mergedFilePath = chunkDirPath + File.separator + mergedFileName;
            File mergedFile = new File(mergedFilePath);

            // 按索引排序分片文件
            List<File> sortedChunkFiles = new ArrayList<>(List.of(chunkFiles));
            sortedChunkFiles.sort(Comparator.comparing(file -> Integer.parseInt(file.getName())));

            // 合并文件
            try (FileOutputStream outputStream = new FileOutputStream(mergedFile)) {
                for (File chunkFile : sortedChunkFiles) {
                    try (FileInputStream inputStream = new FileInputStream(chunkFile)) {
                        IOUtils.copy(inputStream, outputStream);
                    }
                    // 删除分片文件
                    chunkFile.delete();
                }
            }

            // 4. 验证MD5
            String actualMd5 = DigestUtil.md5Hex(new FileInputStream(mergedFile));
            if (!md5.equals(actualMd5)) {
                throw new ResourceException(ResourceException.FILE_UPLOAD_FAILED, "文件MD5不匹配");
            }

            // 5. 获取存储策略
            Integer storageType = resourceProperties.getStorage().getDefaultType();
            ResourceStorageStrategy strategy = storageStrategyFactory.getStrategy(storageType);

            // 6. 存储文件
            try (FileInputStream inputStream = new FileInputStream(mergedFile)) {
                StorageResult storageResult = strategy.store(inputStream, mergedFileName, mergedFile.length(), fileType);
                if (!storageResult.getSuccess()) {
                    throw new ResourceException(ResourceException.FILE_UPLOAD_FAILED, storageResult.getErrorMessage());
                }

                // 7. 保存资源记录
                SystemResourceDO resource = createResourceDO(
                        fileName,
                        ResourceTypeUtils.getResourceTypeByFileType(fileType),
                        fileType,
                        mergedFile.length(),
                        storageType,
                        storageResult.getStoragePath(),
                        storageResult.getAccessUrl(),
                        md5,
                        categoryId,
                        tags,
                        description
                );
                resourceMapper.insert(resource);

                // 8. 记录操作日志
                resourceLogService.logResourceOperation(resource.getId(), 1, true, null);

                // 9. 清理临时文件
                mergedFile.delete();
                chunkDir.delete();

                return resource;
            }
        } catch (IOException e) {
            log.error("合并分片失败", e);
            throw new ResourceException(ResourceException.FILE_UPLOAD_FAILED, e, e.getMessage());
        }
    }

    /**
     * 计算文件MD5
     *
     * @param file 文件
     * @return MD5值
     */
    private String calculateMd5(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            return DigestUtil.md5Hex(inputStream);
        }
    }

    /**
     * 生成文件名
     *
     * @param fileType 文件类型
     * @return 文件名
     */
    private String generateFileName(String fileType) {
        // 格式：租户ID_域ID_时间戳_随机字符串.扩展名
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();
        String timestamp = DateUtil.format(LocalDateTime.now(), ResourceConstant.FILENAME_DATE_FORMAT);
        String randomStr = IdUtil.fastSimpleUUID().substring(0, ResourceConstant.FILENAME_RANDOM_LENGTH);

        return tenantId + ResourceConstant.FILENAME_SEPARATOR +
                domainId + ResourceConstant.FILENAME_SEPARATOR +
                timestamp + ResourceConstant.FILENAME_SEPARATOR +
                randomStr + "." + fileType;
    }

    /**
     * 获取分片目录路径
     *
     * @param md5 文件MD5
     * @return 分片目录路径
     */
    private String getChunkDirPath(String md5) {
        String basePath = resourceProperties.getStorage().getLocal().getBasePath();
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();
        return basePath + File.separator +
                ResourceConstant.CHUNK_TEMP_DIR + File.separator +
                tenantId + File.separator +
                domainId + File.separator +
                md5;
    }

    /**
     * 创建资源DO对象
     *
     * @param resourceName 资源名称
     * @param resourceType 资源类型
     * @param fileType     文件类型
     * @param fileSize     文件大小
     * @param storageType  存储类型
     * @param storagePath  存储路径
     * @param accessUrl    访问URL
     * @param md5          MD5值
     * @param categoryId   分类ID
     * @param tags         标签
     * @param description  描述
     * @return 资源DO对象
     */
    private SystemResourceDO createResourceDO(
            String resourceName,
            Integer resourceType,
            String fileType,
            Long fileSize,
            Integer storageType,
            String storagePath,
            String accessUrl,
            String md5,
            Integer categoryId,
            String tags,
            String description
    ) {
        SystemResourceDO resource = new SystemResourceDO();
        resource.setResourceName(resourceName);
        resource.setResourceType(resourceType);
        resource.setFileType(fileType);
        resource.setFileSize(fileSize);
        resource.setStorageType(storageType);
        resource.setStoragePath(storagePath);
        resource.setAccessUrl(accessUrl);
        resource.setMd5(md5);
        resource.setStatus(0); // 正常状态
        resource.setCategoryId(categoryId);
        resource.setTags(tags);
        resource.setDescription(description);
        resource.setTenantId(ThreadLocalTenantContext.getTenantId());
        resource.setDomainId(ThreadLocalTenantContext.getDomainId());
        return resource;
    }
}
