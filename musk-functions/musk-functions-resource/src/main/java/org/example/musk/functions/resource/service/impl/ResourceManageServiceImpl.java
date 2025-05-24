package org.example.musk.functions.resource.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.resource.constant.ResourceConstant;
import org.example.musk.functions.resource.dao.ResourceMapper;
import org.example.musk.functions.resource.entity.SystemResourceDO;
import org.example.musk.functions.resource.enums.OperationTypeEnum;
import org.example.musk.functions.resource.exception.ResourceException;
import org.example.musk.functions.resource.service.ResourceLogService;
import org.example.musk.functions.resource.service.ResourceManageService;
import org.example.musk.functions.resource.service.ResourceQueryService;
import org.example.musk.functions.resource.storage.ResourceStorageStrategy;
import org.example.musk.functions.resource.storage.StorageStrategyFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.Arrays;

/**
 * 资源管理服务实现
 *
 * @author musk-functions-resource
 */
@Service
@Slf4j
@DS(DBConstant.SYSTEM)
public class ResourceManageServiceImpl implements ResourceManageService {

    @Resource
    private ResourceMapper resourceMapper;

    @Resource
    private ResourceQueryService resourceQueryService;

    @Resource
    private ResourceLogService resourceLogService;

    @Resource
    private StorageStrategyFactory storageStrategyFactory;

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = ResourceConstant.CACHE_NAMESPACE, pattern = "resource:*")
    public void updateResource(SystemResourceDO resource) {
        // 获取原资源信息
        SystemResourceDO oldResource = resourceQueryService.getResource(resource.getId());
        if (oldResource == null) {
            throw new ResourceException(ResourceException.RESOURCE_NOT_FOUND, resource.getId());
        }

        // 检查资源编码唯一性
        if (StrUtil.isNotBlank(resource.getResourceCode()) && !resource.getResourceCode().equals(oldResource.getResourceCode())) {
            LambdaQueryWrapper<SystemResourceDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SystemResourceDO::getResourceCode, resource.getResourceCode());
            queryWrapper.eq(SystemResourceDO::getTenantId, ThreadLocalTenantContext.getTenantId());
            queryWrapper.eq(SystemResourceDO::getDomainId, ThreadLocalTenantContext.getDomainId());
            SystemResourceDO existResource = resourceMapper.selectOne(queryWrapper);
            if (existResource != null && !existResource.getId().equals(resource.getId())) {
                throw new ResourceException(ResourceException.RESOURCE_CODE_DUPLICATE, resource.getResourceCode());
            }
        }

        // 更新资源信息
        resource.setStorageType(oldResource.getStorageType());
        resource.setStoragePath(oldResource.getStoragePath());
        resource.setAccessUrl(oldResource.getAccessUrl());
        resource.setMd5(oldResource.getMd5());
        resource.setFileSize(oldResource.getFileSize());
        resource.setFileType(oldResource.getFileType());
        resource.setResourceType(oldResource.getResourceType());
        resourceMapper.updateById(resource);

        // 记录操作日志
        resourceLogService.logResourceOperation(resource.getId(), OperationTypeEnum.UPDATE.getCode(), true, null);
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = ResourceConstant.CACHE_NAMESPACE, pattern = "resource:*")
    public void deleteResource(Integer id) {
        // 获取资源信息
        SystemResourceDO resource = resourceQueryService.getResource(id);
        if (resource == null) {
            throw new ResourceException(ResourceException.RESOURCE_NOT_FOUND, id);
        }

        try {
            // 获取存储策略
            ResourceStorageStrategy strategy = storageStrategyFactory.getStrategy(resource.getStorageType());

            // 删除物理文件
            boolean deleted = strategy.delete(resource.getStoragePath());
            if (!deleted) {
                log.warn("删除物理文件失败: id={}, storagePath={}", id, resource.getStoragePath());
            }

            // 删除数据库记录（逻辑删除）
            resourceMapper.deleteById(id);

            // 记录操作日志
            resourceLogService.logResourceOperation(id, OperationTypeEnum.DELETE.getCode(), true, null);
        } catch (Exception e) {
            log.error("删除资源失败", e);
            // 记录操作日志
            resourceLogService.logResourceOperation(id, OperationTypeEnum.DELETE.getCode(), false, e.getMessage());
            throw new ResourceException(ResourceException.FILE_DELETE_FAILED, e, e.getMessage());
        }
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = ResourceConstant.CACHE_NAMESPACE, pattern = "resource:*")
    public void batchDeleteResources(Integer[] ids) {
        for (Integer id : ids) {
            deleteResource(id);
        }
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = ResourceConstant.CACHE_NAMESPACE, pattern = "resource:*")
    public void updateResourceStatus(Integer id, Integer status) {
        // 获取资源信息
        SystemResourceDO resource = resourceQueryService.getResource(id);
        if (resource == null) {
            throw new ResourceException(ResourceException.RESOURCE_NOT_FOUND, id);
        }

        // 更新状态
        SystemResourceDO updateResource = new SystemResourceDO();
        updateResource.setId(id);
        updateResource.setStatus(status);
        resourceMapper.updateById(updateResource);

        // 记录操作日志
        resourceLogService.logResourceOperation(id, OperationTypeEnum.UPDATE.getCode(), true, null);
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = ResourceConstant.CACHE_NAMESPACE, pattern = "resource:*")
    public void updateResourceCategory(Integer id, Integer categoryId) {
        // 获取资源信息
        SystemResourceDO resource = resourceQueryService.getResource(id);
        if (resource == null) {
            throw new ResourceException(ResourceException.RESOURCE_NOT_FOUND, id);
        }

        // 更新分类
        SystemResourceDO updateResource = new SystemResourceDO();
        updateResource.setId(id);
        updateResource.setCategoryId(categoryId);
        resourceMapper.updateById(updateResource);

        // 记录操作日志
        resourceLogService.logResourceOperation(id, OperationTypeEnum.UPDATE.getCode(), true, null);
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = ResourceConstant.CACHE_NAMESPACE, pattern = "resource:*")
    public void updateResourceTags(Integer id, String tags) {
        // 获取资源信息
        SystemResourceDO resource = resourceQueryService.getResource(id);
        if (resource == null) {
            throw new ResourceException(ResourceException.RESOURCE_NOT_FOUND, id);
        }

        // 更新标签
        SystemResourceDO updateResource = new SystemResourceDO();
        updateResource.setId(id);
        updateResource.setTags(tags);
        resourceMapper.updateById(updateResource);

        // 记录操作日志
        resourceLogService.logResourceOperation(id, OperationTypeEnum.UPDATE.getCode(), true, null);
    }
}
