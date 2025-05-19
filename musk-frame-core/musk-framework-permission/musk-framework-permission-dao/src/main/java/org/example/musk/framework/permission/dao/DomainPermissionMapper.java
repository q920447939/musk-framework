package org.example.musk.framework.permission.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.musk.framework.permission.entity.DomainPermissionDO;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 领域权限 Mapper
 *
 * @author musk-framework-permission
 */
@Mapper
public interface DomainPermissionMapper extends BaseMapperX<DomainPermissionDO> {

    /**
     * 查询特定资源的权限
     *
     * @param tenantId 租户ID
     * @param domainId 领域ID
     * @param resourceType 资源类型
     * @param resourceId 资源ID
     * @param operationType 操作类型
     * @return 权限信息
     */
    default DomainPermissionDO selectPermission(
            @Param("tenantId") Integer tenantId,
            @Param("domainId") Integer domainId,
            @Param("resourceType") String resourceType,
            @Param("resourceId") String resourceId,
            @Param("operationType") String operationType) {
        return selectOne(new LambdaQueryWrapperX<DomainPermissionDO>()
                .eq(DomainPermissionDO::getTenantId, tenantId)
                .eq(DomainPermissionDO::getDomainId, domainId)
                .eq(DomainPermissionDO::getResourceType, resourceType)
                .eq(DomainPermissionDO::getResourceId, resourceId)
                .eq(DomainPermissionDO::getOperationType, operationType));
    }

    /**
     * 查询整个资源类型的权限
     *
     * @param tenantId 租户ID
     * @param domainId 领域ID
     * @param resourceType 资源类型
     * @param operationType 操作类型
     * @return 权限信息
     */
    default DomainPermissionDO selectGeneralPermission(
            @Param("tenantId") Integer tenantId,
            @Param("domainId") Integer domainId,
            @Param("resourceType") String resourceType,
            @Param("operationType") String operationType) {
        return selectOne(new LambdaQueryWrapperX<DomainPermissionDO>()
                .eq(DomainPermissionDO::getTenantId, tenantId)
                .eq(DomainPermissionDO::getDomainId, domainId)
                .eq(DomainPermissionDO::getResourceType, resourceType)
                .isNull(DomainPermissionDO::getResourceId)
                .eq(DomainPermissionDO::getOperationType, operationType));
    }
}
