package org.example.musk.functions.icon.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.cache.core.CacheKeyBuilder;
import org.example.musk.functions.cache.core.CacheManager;
import org.example.musk.functions.icon.constant.IconConstant;
import org.example.musk.functions.icon.dao.SystemIconResourceMapper;
import org.example.musk.functions.icon.entity.SystemIconResourceDO;
import org.example.musk.functions.icon.exception.IconException;
import org.example.musk.functions.icon.service.SystemIconResourceService;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 图标资源 Service 实现类
 *
 * @author musk-functions-icon
 */
@Service
@Validated
@Slf4j
@DS(DBConstant.SYSTEM)
public class SystemIconResourceServiceImpl extends ServiceImpl<SystemIconResourceMapper, SystemIconResourceDO> implements SystemIconResourceService {

    @Override
    @CacheEvict(namespace = "ICON", pattern = "resource:*")
    public Integer createIconResource(SystemIconResourceDO resource) {
        // 如果设置为默认资源，则将同一图标同一域的其他资源设为非默认
        if (Boolean.TRUE.equals(resource.getIsDefault())) {
            updateDefaultResource(resource.getIconId());
        }
        // 保存资源
        save(resource);
        return resource.getId();
    }

    @Override
    @CacheEvict(namespace = "ICON", pattern = "resource:*")
    public void updateIconResource(SystemIconResourceDO resource) {
        // 校验资源存在
        validateResourceExists(resource.getId());
        // 如果设置为默认资源，则将同一图标同一域的其他资源设为非默认
        if (Boolean.TRUE.equals(resource.getIsDefault())) {
            updateDefaultResource(resource.getIconId());
        }
        // 更新资源
        updateById(resource);
    }

    @Override
    @CacheEvict(namespace = "ICON", pattern = "resource:*")
    public void deleteIconResource(Integer id) {
        // 校验资源存在
        SystemIconResourceDO resource = validateResourceExists(id);
        // 如果是默认资源，不允许删除
        if (Boolean.TRUE.equals(resource.getIsDefault())) {
            throw new IconException(IconException.DEFAULT_RESOURCE_CANNOT_DELETE);
        }
        // 删除资源
        removeById(id);
    }

    @Override
    @Cacheable(namespace = "ICON", key = "'resource:' + #id", expireSeconds = IconConstant.RESOURCE_CACHE_EXPIRE_SECONDS)
    public SystemIconResourceDO getIconResource(Integer id) {
        return getById(id);
    }

    @Override
    @Cacheable(namespace = "ICON", key = "'resource:icon:' + #iconId", expireSeconds = IconConstant.RESOURCE_CACHE_EXPIRE_SECONDS)
    public List<SystemIconResourceDO> getResourcesByIconId(Integer iconId) {
        return list(new LambdaQueryWrapperX<SystemIconResourceDO>()
                .eq(SystemIconResourceDO::getIconId, iconId));
    }


    @Override
    @Cacheable(namespace = "ICON", key = "'resource:default:' + #iconId + ':' ", expireSeconds = IconConstant.RESOURCE_CACHE_EXPIRE_SECONDS)
    public SystemIconResourceDO getDefaultResource(Integer iconId) {
        return getOne(new LambdaQueryWrapperX<SystemIconResourceDO>()
                .eq(SystemIconResourceDO::getIconId, iconId)
                .eq(SystemIconResourceDO::getIsDefault, true));
    }

    /**
     * 更新默认资源
     */
    private void updateDefaultResource(Integer iconId) {
        update(new LambdaUpdateWrapper<SystemIconResourceDO>()
                .eq(SystemIconResourceDO::getIconId, iconId)
                .set(SystemIconResourceDO::getIsDefault, false));
    }

    /**
     * 校验资源存在
     */
    private SystemIconResourceDO validateResourceExists(Integer id) {
        SystemIconResourceDO resource = getById(id);
        if (resource == null) {
            throw new IconException(IconException.ICON_RESOURCE_NOT_EXISTS);
        }
        return resource;
    }
}
