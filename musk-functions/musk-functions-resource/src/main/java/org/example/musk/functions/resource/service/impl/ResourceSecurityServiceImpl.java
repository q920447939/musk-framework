package org.example.musk.functions.resource.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.resource.config.ResourceProperties;
import org.example.musk.functions.resource.dao.ResourceMapper;
import org.example.musk.functions.resource.entity.SystemResourceDO;
import org.example.musk.functions.resource.exception.ResourceException;
import org.example.musk.functions.resource.service.ResourceSecurityService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 资源安全服务实现
 *
 * @author musk-functions-resource
 */
@Service
@Slf4j
@DS(DBConstant.SYSTEM)
public class ResourceSecurityServiceImpl implements ResourceSecurityService {

    @Resource
    private ResourceProperties resourceProperties;

    @Resource
    private ResourceMapper resourceMapper;

    @Override
    public void checkFileType(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (StrUtil.isBlank(originalFilename)) {
            throw new ResourceException(ResourceException.FILE_TYPE_NOT_ALLOWED, "文件名不能为空");
        }

        String fileType = FilenameUtils.getExtension(originalFilename).toLowerCase();
        if (StrUtil.isBlank(fileType)) {
            throw new ResourceException(ResourceException.FILE_TYPE_NOT_ALLOWED, "文件类型不能为空");
        }

        // 检查黑名单
        List<String> blockedTypes = resourceProperties.getSecurity().getBlockedTypes();
        if (blockedTypes.contains(fileType)) {
            throw new ResourceException(ResourceException.FILE_TYPE_BLOCKED, fileType);
        }

        // 检查白名单
        List<String> allowedTypes = resourceProperties.getSecurity().getAllowedTypes();
        if (!allowedTypes.isEmpty() && !allowedTypes.contains(fileType)) {
            throw new ResourceException(ResourceException.FILE_TYPE_NOT_ALLOWED, fileType);
        }
    }

    @Override
    public void checkFileSize(MultipartFile file) {
        long maxFileSize = resourceProperties.getSecurity().getMaxFileSize();
        if (file.getSize() > maxFileSize) {
            throw new ResourceException(ResourceException.FILE_SIZE_EXCEEDED, file.getSize(), maxFileSize);
        }
    }

    @Override
    public boolean validateSignature(String url, String signature, long timestamp) {
        // 检查签名是否启用
        if (!resourceProperties.getSecurity().getSignature().getEnabled()) {
            return true;
        }

        // 检查时间戳是否过期
        long currentTime = System.currentTimeMillis() / 1000;
        int expireSeconds = resourceProperties.getSecurity().getSignature().getExpireSeconds();
        if (currentTime - timestamp > expireSeconds) {
            log.warn("URL签名已过期: url={}, timestamp={}, currentTime={}", url, timestamp, currentTime);
            return false;
        }

        // 验证签名
        String expectedSignature = generateSignature(url, timestamp);
        boolean valid = expectedSignature.equals(signature);
        if (!valid) {
            log.warn("URL签名无效: url={}, expectedSignature={}, actualSignature={}", url, expectedSignature, signature);
        }
        return valid;
    }

    @Override
    public String generateSignature(String url, long timestamp) {
        String secret = resourceProperties.getSecurity().getSignature().getSecret();
        String stringToSign = url + ":" + timestamp + ":" + secret;
        return DigestUtil.md5Hex(stringToSign);
    }

    @Override
    public boolean checkReferer(String referer) {
        // 检查防盗链是否启用
        if (!resourceProperties.getSecurity().getAntiLeech().getEnabled()) {
            return true;
        }

        // 检查Referer是否为空
        if (StrUtil.isBlank(referer)) {
            return resourceProperties.getSecurity().getAntiLeech().getAllowEmptyReferer();
        }

        // 检查Referer白名单
        List<String> allowedReferers = resourceProperties.getSecurity().getAntiLeech().getAllowedReferers();
        if (allowedReferers.isEmpty()) {
            return true;
        }

        for (String allowedReferer : allowedReferers) {
            if (referer.startsWith(allowedReferer)) {
                return true;
            }
        }

        log.warn("Referer不允许: referer={}", referer);
        return false;
    }

    @Override
    public boolean checkStorageSpace(long fileSize) {
        // 获取租户级别限制
        long maxStorageSize = resourceProperties.getLimit().getTenant().getMaxStorageSize();
        if (maxStorageSize <= 0) {
            return true;
        }

        // 计算当前已使用的存储空间
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        LambdaQueryWrapper<SystemResourceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemResourceDO::getTenantId, tenantId);
        queryWrapper.select(SystemResourceDO::getFileSize);
        List<SystemResourceDO> resources = resourceMapper.selectList(queryWrapper);
        long usedStorageSize = resources.stream().mapToLong(SystemResourceDO::getFileSize).sum();

        // 检查是否超过限制
        boolean allowed = usedStorageSize + fileSize <= maxStorageSize;
        if (!allowed) {
            log.warn("存储空间不足: tenantId={}, usedStorageSize={}, fileSize={}, maxStorageSize={}",
                    tenantId, usedStorageSize, fileSize, maxStorageSize);
        }
        return allowed;
    }

    @Override
    public boolean checkFileCount() {
        // 获取租户级别限制
        int maxFileCount = resourceProperties.getLimit().getTenant().getMaxFileCount();
        if (maxFileCount <= 0) {
            return true;
        }

        // 计算当前文件数量
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        LambdaQueryWrapper<SystemResourceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemResourceDO::getTenantId, tenantId);
        long fileCount = resourceMapper.selectCount(queryWrapper);

        // 检查是否超过限制
        boolean allowed = fileCount < maxFileCount;
        if (!allowed) {
            log.warn("文件数量超过限制: tenantId={}, fileCount={}, maxFileCount={}",
                    tenantId, fileCount, maxFileCount);
        }
        return allowed;
    }
}
