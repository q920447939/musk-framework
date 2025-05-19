package org.example.musk.functions.icon.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.cache.core.CacheKeyBuilder;
import org.example.musk.functions.cache.core.CacheManager;
import org.example.musk.functions.cache.model.CacheNamespace;
import org.example.musk.functions.icon.constant.IconConstant;
import org.example.musk.functions.icon.dao.SystemIconMapper;
import org.example.musk.functions.icon.entity.SystemIconDO;
import org.example.musk.functions.icon.entity.SystemIconResourceDO;
import org.example.musk.functions.icon.exception.IconException;
import org.example.musk.functions.icon.service.SystemIconResourceService;
import org.example.musk.functions.icon.service.SystemIconService;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 图标 Service 实现类
 *
 * @author musk-functions-icon
 */
@Service
@Validated
@Slf4j
@DS(DBConstant.SYSTEM)
public class SystemIconServiceImpl extends ServiceImpl<SystemIconMapper, SystemIconDO> implements SystemIconService {

    @Resource
    private SystemIconMapper systemIconMapper;

    @Resource
    private SystemIconResourceService systemIconResourceService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CacheKeyBuilder cacheKeyBuilder;

    @Override
    @CacheEvict(namespace = "ICON", pattern = "'icon:*'")
    public Integer createIcon(SystemIconDO icon) {
        // 校验图标编码唯一性
        validateIconCodeUnique(icon);
        // 保存图标
        save(icon);
        return icon.getId();
    }

    @Override
    @CacheEvict(namespace = "ICON", pattern = "'icon:*'")
    public void updateIcon(SystemIconDO icon) {
        // 校验图标存在
        validateIconExists(icon.getId());
        // 校验图标编码唯一性
        validateIconCodeUnique(icon);
        // 更新图标
        updateById(icon);
    }

    @Override
    @CacheEvict(namespace = "ICON", pattern = "'icon:*'")
    public void deleteIcon(Integer id) {
        // 校验图标存在
        validateIconExists(id);
        // 检查是否有资源引用
        List<SystemIconResourceDO> resources = systemIconResourceService.getResourcesByIconId(id);
        if (!resources.isEmpty()) {
            throw new IconException(IconException.ICON_HAS_RESOURCES);
        }
        // 删除图标
        removeById(id);
    }

    @Override
    @Cacheable(namespace = "ICON", key = "'icon:' + #id", expireSeconds = IconConstant.ICON_CACHE_EXPIRE_SECONDS)
    public SystemIconDO getIcon(Integer id) {
        return getById(id);
    }

    @Override
    @Cacheable(namespace = "ICON", key = "'icon:code:' + #tenantId + ':' + #domainId + ':' + #iconCode", expireSeconds = IconConstant.ICON_CACHE_EXPIRE_SECONDS)
    public SystemIconDO getIconByCode(String iconCode, Integer tenantId, Integer domainId) {
        return baseMapper.selectOne(new LambdaQueryWrapperX<SystemIconDO>()
                .eq(SystemIconDO::getIconCode, iconCode)
                .eq(SystemIconDO::getTenantId, tenantId)
                .eq(SystemIconDO::getDomainId, domainId));
    }

    @Override
    @Cacheable(namespace = "ICON", key = "'icons:category:' + #tenantId + ':' + #domainId + ':' + #categoryId", expireSeconds = IconConstant.ICON_CACHE_EXPIRE_SECONDS)
    public List<SystemIconDO> getIconsByCategory(Integer categoryId, Integer tenantId, Integer domainId) {
        return list(new LambdaQueryWrapperX<SystemIconDO>()
                .eq(SystemIconDO::getCategoryId, categoryId)
                .eq(SystemIconDO::getTenantId, tenantId)
                .eq(SystemIconDO::getDomainId, domainId)
                .eq(SystemIconDO::getStatus, IconConstant.STATUS_NORMAL));
    }

    @Override
    public List<SystemIconDO> searchIcons(String keyword, Integer tenantId, Integer domainId) {
        return list(new LambdaQueryWrapperX<SystemIconDO>()
                .and(w -> w
                        .like(SystemIconDO::getIconName, keyword)
                        .or()
                        .like(SystemIconDO::getIconCode, keyword)
                        .or()
                        .like(SystemIconDO::getDescription, keyword))
                .eq(SystemIconDO::getTenantId, tenantId)
                .eq(SystemIconDO::getDomainId, domainId)
                .eq(SystemIconDO::getStatus, IconConstant.STATUS_NORMAL));
    }

    /**
     * 校验图标编码唯一性
     */
    private void validateIconCodeUnique(SystemIconDO icon) {
        SystemIconDO existingIcon = getIconByCode(icon.getIconCode(), icon.getTenantId(), icon.getDomainId());
        if (existingIcon != null && !existingIcon.getId().equals(icon.getId())) {
            throw new IconException(IconException.ICON_CODE_DUPLICATE);
        }
    }

    /**
     * 校验图标存在
     */
    private SystemIconDO validateIconExists(Integer id) {
        SystemIconDO icon = getById(id);
        if (icon == null) {
            throw new IconException(IconException.ICON_NOT_EXISTS);
        }
        return icon;
    }
}
