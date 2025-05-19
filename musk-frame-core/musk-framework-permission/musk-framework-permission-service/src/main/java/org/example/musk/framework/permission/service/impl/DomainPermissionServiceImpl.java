package org.example.musk.framework.permission.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.framework.permission.constant.PermissionConstant;
import org.example.musk.framework.permission.dao.DomainPermissionMapper;
import org.example.musk.framework.permission.entity.DomainPermissionDO;
import org.example.musk.framework.permission.enums.OperationTypeEnum;
import org.example.musk.framework.permission.enums.ResourceTypeEnum;
import org.example.musk.framework.permission.enums.SystemParamGroupEnum;
import org.example.musk.framework.permission.exception.PermissionException;
import org.example.musk.framework.permission.service.DomainPermissionService;
import org.example.musk.middleware.redis.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 领域权限 Service 实现类
 *
 * @author musk-framework-permission
 */
@Service
@Validated
@Slf4j
@DS(DBConstant.SYSTEM)
public class DomainPermissionServiceImpl extends ServiceImpl<DomainPermissionMapper, DomainPermissionDO> implements DomainPermissionService {

    @Resource
    private DomainPermissionMapper domainPermissionMapper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean hasPermission(Integer tenantId, Integer domainId, String resourceType,
                               String resourceId, String operationType) {
        // 首先检查特定资源的权限（如果resourceId不为空）
        if (resourceId != null) {
            String specificCacheKey = String.format(PermissionConstant.PERMISSION_CACHE_KEY,
                                      tenantId, domainId, resourceType, resourceId, operationType);

            Boolean specificPermission = (Boolean) redisUtil.get(specificCacheKey);
            if (specificPermission != null) {
                return specificPermission;
            }

            DomainPermissionDO specificPermissionDO = domainPermissionMapper.selectPermission(
                tenantId, domainId, resourceType, resourceId, operationType);

            if (specificPermissionDO != null) {
                boolean isAllowed = specificPermissionDO.getIsAllowed();
                redisUtil.set(specificCacheKey, isAllowed, PermissionConstant.PERMISSION_CACHE_EXPIRE_SECONDS);
                return isAllowed;
            }
        }

        // 如果没有找到特定资源的权限，或者resourceId为空，检查整个资源类型的权限
        String generalCacheKey = String.format(PermissionConstant.PERMISSION_CACHE_KEY,
                                 tenantId, domainId, resourceType, "null", operationType);

        Boolean generalPermission = (Boolean) redisUtil.get(generalCacheKey);
        if (generalPermission != null) {
            return generalPermission;
        }

        DomainPermissionDO generalPermissionDO = domainPermissionMapper.selectGeneralPermission(
            tenantId, domainId, resourceType, operationType);

        boolean isAllowed = generalPermissionDO != null && generalPermissionDO.getIsAllowed();
        redisUtil.set(generalCacheKey, isAllowed, PermissionConstant.PERMISSION_CACHE_EXPIRE_SECONDS);

        return isAllowed;
    }

    @Override
    public boolean hasPermission(Integer tenantId, Integer domainId, ResourceTypeEnum resourceType,
                               String resourceId, OperationTypeEnum operationType) {
        return hasPermission(tenantId, domainId, resourceType.getCode(), resourceId, operationType.getCode());
    }

    @Override
    public boolean hasPermission(String resourceType, String resourceId, String operationType) {
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();

        if (tenantId == null || domainId == null) {
            log.warn("当前上下文中未找到租户ID或领域ID");
            throw new PermissionException(PermissionException.DOMAIN_ID_NOT_EXISTS);
        }

        return hasPermission(tenantId, domainId, resourceType, resourceId, operationType);
    }

    @Override
    public boolean hasPermission(ResourceTypeEnum resourceType, String resourceId, OperationTypeEnum operationType) {
        return hasPermission(resourceType.getCode(), resourceId, operationType.getCode());
    }

    @Override
    public boolean hasSystemParamPermission(Integer tenantId, Integer domainId, String paramKey, String operationType) {
        // 1. 首先检查特定配置项的权限
        boolean hasSpecificPermission = hasPermission(tenantId, domainId,
                                                   ResourceTypeEnum.SYSTEM_PARAMS.getCode(),
                                                   paramKey, operationType);
        if (hasSpecificPermission) {
            return true;
        }

        // 2. 如果没有特定配置项的权限，检查配置组的权限
        SystemParamGroupEnum group = SystemParamGroupEnum.findByParamKey(paramKey);
        if (group != null) {
            boolean hasGroupPermission = hasPermission(tenantId, domainId,
                                                    ResourceTypeEnum.SYSTEM_PARAMS.getCode(),
                                                    group.getCode(), operationType);
            if (hasGroupPermission) {
                return true;
            }
        }

        // 3. 最后检查整个系统配置模块的权限
        return hasPermission(tenantId, domainId, ResourceTypeEnum.SYSTEM_PARAMS.getCode(),
                          null, operationType);
    }

    @Override
    public boolean hasSystemParamPermission(String paramKey, String operationType) {
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();

        if (tenantId == null || domainId == null) {
            log.warn("当前上下文中未找到租户ID或领域ID");
            throw new PermissionException(PermissionException.DOMAIN_ID_NOT_EXISTS);
        }

        return hasSystemParamPermission(tenantId, domainId, paramKey, operationType);
    }

    @Override
    public Long createPermission(DomainPermissionDO permission) {
        // 保存权限
        save(permission);

        // 清除缓存
        clearPermissionCache(permission);

        return permission.getId();
    }

    @Override
    public void updatePermission(DomainPermissionDO permission) {
        // 更新权限
        updateById(permission);

        // 清除缓存
        clearPermissionCache(permission);
    }

    @Override
    public void deletePermission(Long id) {
        // 获取权限信息，用于清除缓存
        DomainPermissionDO permission = getById(id);

        if (permission != null) {
            // 删除权限
            removeById(id);

            // 清除缓存
            clearPermissionCache(permission);
        }
    }

    /**
     * 清除权限缓存
     *
     * @param permission 权限信息
     */
    private void clearPermissionCache(DomainPermissionDO permission) {
        String resourceId = permission.getResourceId() != null ? permission.getResourceId() : "null";
        String cacheKey = String.format(PermissionConstant.PERMISSION_CACHE_KEY,
                          permission.getTenantId(), permission.getDomainId(),
                          permission.getResourceType(), resourceId,
                          permission.getOperationType());

        redisUtil.del(cacheKey);
        log.debug("清除权限缓存: {}", cacheKey);
    }
}
