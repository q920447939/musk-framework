package org.example.musk.functions.resource.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.resource.constant.ResourceConstant;
import org.example.musk.functions.resource.dao.ResourceMapper;
import org.example.musk.functions.resource.entity.SystemResourceDO;
import org.example.musk.functions.resource.enums.OperationTypeEnum;
import org.example.musk.functions.resource.exception.ResourceException;
import org.example.musk.functions.resource.service.ResourceLogService;
import org.example.musk.functions.resource.service.ResourceQueryService;
import org.example.musk.functions.resource.storage.ResourceStorageStrategy;
import org.example.musk.functions.resource.storage.StorageStrategyFactory;
import org.example.musk.functions.resource.vo.ResourcePageReqVO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 资源查询服务实现
 *
 * @author musk-functions-resource
 */
@Service
@Slf4j
@DS(DBConstant.SYSTEM)
public class ResourceQueryServiceImpl implements ResourceQueryService {

    @Resource
    private ResourceMapper resourceMapper;

    @Resource
    private StorageStrategyFactory storageStrategyFactory;

    @Resource
    private ResourceLogService resourceLogService;

    @Override
    @Cacheable(namespace = ResourceConstant.CACHE_NAMESPACE, key = "'resource:' + #id", expireSeconds = ResourceConstant.RESOURCE_CACHE_EXPIRE_SECONDS)
    public SystemResourceDO getResource(Integer id) {
        SystemResourceDO resource = resourceMapper.selectById(id);
        if (resource == null || resource.getDeleted()) {
            throw new ResourceException(ResourceException.RESOURCE_NOT_FOUND, id);
        }
        return resource;
    }

    @Override
    public InputStream getResourceContent(Integer id) {
        // 获取资源信息
        SystemResourceDO resource = getResource(id);

        try {
            // 获取存储策略
            ResourceStorageStrategy strategy = storageStrategyFactory.getStrategy(resource.getStorageType());

            // 获取资源内容
            InputStream inputStream = strategy.getContent(resource.getStoragePath());

            // 记录下载日志
            resourceLogService.logResourceOperation(id, OperationTypeEnum.DOWNLOAD.getCode(), true, null);

            return inputStream;
        } catch (Exception e) {
            log.error("获取资源内容失败", e);
            // 记录下载失败日志
            resourceLogService.logResourceOperation(id, OperationTypeEnum.DOWNLOAD.getCode(), false, e.getMessage());
            throw new ResourceException(ResourceException.FILE_DOWNLOAD_FAILED, e, e.getMessage());
        }
    }

    @Override
    @Cacheable(namespace = ResourceConstant.CACHE_NAMESPACE, key = "'resource:url:' + #id + ':' + #expireSeconds", expireSeconds = ResourceConstant.URL_CACHE_EXPIRE_SECONDS)
    public String getResourceAccessUrl(Integer id, Integer expireSeconds) {
        // 获取资源信息
        SystemResourceDO resource = getResource(id);

        try {
            // 获取存储策略
            ResourceStorageStrategy strategy = storageStrategyFactory.getStrategy(resource.getStorageType());

            // 获取访问URL
            String accessUrl = strategy.getAccessUrl(resource.getStoragePath(), expireSeconds);

            // 记录查看日志
            resourceLogService.logResourceOperation(id, OperationTypeEnum.VIEW.getCode(), true, null);

            return accessUrl;
        } catch (Exception e) {
            log.error("获取资源访问URL失败", e);
            // 记录查看失败日志
            resourceLogService.logResourceOperation(id, OperationTypeEnum.VIEW.getCode(), false, e.getMessage());
            throw new ResourceException(ResourceException.FILE_DOWNLOAD_FAILED, e, e.getMessage());
        }
    }

    @Override
    @Cacheable(namespace = ResourceConstant.CACHE_NAMESPACE, key = "'resource:category:' + #categoryId", expireSeconds = ResourceConstant.RESOURCE_CACHE_EXPIRE_SECONDS)
    public List<SystemResourceDO> getResourcesByCategory(Integer categoryId) {
        LambdaQueryWrapper<SystemResourceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemResourceDO::getCategoryId, categoryId);
        queryWrapper.eq(SystemResourceDO::getStatus, 0); // 正常状态
        queryWrapper.orderByDesc(SystemResourceDO::getCreateTime);
        return resourceMapper.selectList(queryWrapper);
    }

    @Override
    @Cacheable(namespace = ResourceConstant.CACHE_NAMESPACE, key = "'resource:tag:' + #tag", expireSeconds = ResourceConstant.RESOURCE_CACHE_EXPIRE_SECONDS)
    public List<SystemResourceDO> getResourcesByTag(String tag) {
        LambdaQueryWrapper<SystemResourceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(SystemResourceDO::getTags, tag);
        queryWrapper.eq(SystemResourceDO::getStatus, 0); // 正常状态
        queryWrapper.orderByDesc(SystemResourceDO::getCreateTime);
        return resourceMapper.selectList(queryWrapper);
    }

    @Override
    @Cacheable(namespace = ResourceConstant.CACHE_NAMESPACE, key = "'resource:type:' + #resourceType", expireSeconds = ResourceConstant.RESOURCE_CACHE_EXPIRE_SECONDS)
    public List<SystemResourceDO> getResourcesByType(Integer resourceType) {
        LambdaQueryWrapper<SystemResourceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemResourceDO::getResourceType, resourceType);
        queryWrapper.eq(SystemResourceDO::getStatus, 0); // 正常状态
        queryWrapper.orderByDesc(SystemResourceDO::getCreateTime);
        return resourceMapper.selectList(queryWrapper);
    }

    @Override
    public PageResult<SystemResourceDO> pageResources(ResourcePageReqVO pageReqVO) {
        // 构建查询条件
        LambdaQueryWrapper<SystemResourceDO> queryWrapper = new LambdaQueryWrapper<>();

        // 添加查询条件
        queryWrapper.like(StrUtil.isNotBlank(pageReqVO.getResourceName()), SystemResourceDO::getResourceName, pageReqVO.getResourceName());
        queryWrapper.eq(StrUtil.isNotBlank(pageReqVO.getResourceCode()), SystemResourceDO::getResourceCode, pageReqVO.getResourceCode());
        queryWrapper.eq(pageReqVO.getResourceType() != null, SystemResourceDO::getResourceType, pageReqVO.getResourceType());
        queryWrapper.eq(StrUtil.isNotBlank(pageReqVO.getFileType()), SystemResourceDO::getFileType, pageReqVO.getFileType());
        queryWrapper.eq(pageReqVO.getCategoryId() != null, SystemResourceDO::getCategoryId, pageReqVO.getCategoryId());
        queryWrapper.like(StrUtil.isNotBlank(pageReqVO.getTag()), SystemResourceDO::getTags, pageReqVO.getTag());
        queryWrapper.eq(pageReqVO.getStatus() != null, SystemResourceDO::getStatus, pageReqVO.getStatus());

        // 添加时间范围条件
        if (StrUtil.isNotBlank(pageReqVO.getBeginTime()) && StrUtil.isNotBlank(pageReqVO.getEndTime())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime beginTime = LocalDateTime.parse(pageReqVO.getBeginTime(), formatter);
            LocalDateTime endTime = LocalDateTime.parse(pageReqVO.getEndTime(), formatter);
            queryWrapper.between(SystemResourceDO::getCreateTime, beginTime, endTime);
        }

        // 按创建时间降序排序
        queryWrapper.orderByDesc(SystemResourceDO::getCreateTime);

        // 执行分页查询
        Page<SystemResourceDO> page = new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize());
        Page<SystemResourceDO> pageResult = resourceMapper.selectPage(page, queryWrapper);

        // 转换为PageResult
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal());
    }

    @Override
    public List<SystemResourceDO> searchResources(String keyword) {
        LambdaQueryWrapper<SystemResourceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemResourceDO::getStatus, 0); // 正常状态

        // 添加关键字搜索条件
        queryWrapper.and(wrapper -> wrapper
                .like(SystemResourceDO::getResourceName, keyword)
                .or()
                .like(SystemResourceDO::getResourceCode, keyword)
                .or()
                .like(SystemResourceDO::getTags, keyword)
                .or()
                .like(SystemResourceDO::getDescription, keyword)
        );

        // 按创建时间降序排序
        queryWrapper.orderByDesc(SystemResourceDO::getCreateTime);

        // 限制返回数量
        queryWrapper.last("LIMIT 20");

        return resourceMapper.selectList(queryWrapper);
    }
}
