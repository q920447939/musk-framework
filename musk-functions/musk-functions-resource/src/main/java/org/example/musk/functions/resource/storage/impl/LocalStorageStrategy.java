package org.example.musk.functions.resource.storage.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.functions.resource.config.ResourceProperties;
import org.example.musk.functions.resource.constant.ResourceConstant;
import org.example.musk.functions.resource.enums.StorageTypeEnum;
import org.example.musk.functions.resource.exception.ResourceException;
import org.example.musk.functions.resource.storage.ResourceStorageStrategy;
import org.example.musk.functions.resource.storage.StorageResult;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 本地存储策略实现
 *
 * @author musk-functions-resource
 */
@Component
@Slf4j
public class LocalStorageStrategy implements ResourceStorageStrategy {

    @Resource
    private ResourceProperties resourceProperties;

    @Override
    public StorageResult store(InputStream inputStream, String fileName, long fileSize, String fileType) {
        try {
            // 生成存储路径
            String relativePath = generateRelativePath(fileName);
            String absolutePath = getAbsolutePath(relativePath);

            // 确保目录存在
            FileUtil.mkParentDirs(absolutePath);

            // 写入文件
            try (FileOutputStream outputStream = new FileOutputStream(absolutePath)) {
                IOUtils.copy(inputStream, outputStream);
            }

            // 生成访问URL
            String accessUrl = generateAccessUrl(relativePath);

            // 返回存储结果
            return StorageResult.builder()
                    .storagePath(relativePath)
                    .accessUrl(accessUrl)
                    .fileName(fileName)
                    .fileSize(fileSize)
                    .fileType(fileType)
                    .storageType(getStorageType())
                    .success(true)
                    .build();
        } catch (IOException e) {
            log.error("存储文件失败", e);
            return StorageResult.builder()
                    .success(false)
                    .errorMessage("存储文件失败: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public boolean delete(String storagePath) {
        try {
            String absolutePath = getAbsolutePath(storagePath);
            File file = new File(absolutePath);
            if (file.exists()) {
                return file.delete();
            }
            return true;
        } catch (Exception e) {
            log.error("删除文件失败", e);
            return false;
        }
    }

    @Override
    public String getAccessUrl(String storagePath, int expireSeconds) {
        // 本地存储不支持过期时间，直接返回访问URL
        return generateAccessUrl(storagePath);
    }

    @Override
    public InputStream getContent(String storagePath) {
        try {
            String absolutePath = getAbsolutePath(storagePath);
            File file = new File(absolutePath);
            if (!file.exists()) {
                throw new ResourceException("文件不存在");
            }
            return new FileInputStream(file);
        } catch (IOException e) {
            log.error("获取文件内容失败", e);
            throw new ResourceException("获取文件内容失败: " + e.getMessage());
        }
    }

    @Override
    public Integer getStorageType() {
        return StorageTypeEnum.LOCAL.getCode();
    }

    /**
     * 生成相对路径
     *
     * @param fileName 文件名
     * @return 相对路径
     */
    private String generateRelativePath(String fileName) {
        // 生成日期路径，格式：yyyy/MM/dd
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern(ResourceConstant.PATH_DATE_FORMAT));

        // 生成租户和域路径
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();
        String tenantDomainPath = tenantId + ResourceConstant.PATH_SEPARATOR + domainId;

        // 组合路径
        return tenantDomainPath + ResourceConstant.PATH_SEPARATOR + datePath + ResourceConstant.PATH_SEPARATOR + fileName;
    }

    /**
     * 获取绝对路径
     *
     * @param relativePath 相对路径
     * @return 绝对路径
     */
    private String getAbsolutePath(String relativePath) {
        String basePath = resourceProperties.getStorage().getLocal().getBasePath();
        return basePath + ResourceConstant.PATH_SEPARATOR + relativePath;
    }

    /**
     * 生成访问URL
     *
     * @param relativePath 相对路径
     * @return 访问URL
     */
    private String generateAccessUrl(String relativePath) {
        String domain = resourceProperties.getStorage().getLocal().getDomain();
        if (domain.endsWith(ResourceConstant.PATH_SEPARATOR)) {
            domain = domain.substring(0, domain.length() - 1);
        }
        return domain + ResourceConstant.PATH_SEPARATOR + "resources" + ResourceConstant.PATH_SEPARATOR + relativePath;
    }
}
