package org.example.musk.framework.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.musk.framework.permission.entity.DomainPermissionDO;
import org.example.musk.framework.permission.enums.OperationTypeEnum;
import org.example.musk.framework.permission.enums.ResourceTypeEnum;

/**
 * 领域权限 Service 接口
 *
 * @author musk-framework-permission
 */
public interface DomainPermissionService extends IService<DomainPermissionDO> {

    /**
     * 检查是否有权限
     *
     * @param tenantId 租户ID
     * @param domainId 领域ID
     * @param resourceType 资源类型
     * @param resourceId 资源ID，可以为null表示整个资源类型
     * @param operationType 操作类型
     * @return 是否有权限
     */
    boolean hasPermission(Integer tenantId, Integer domainId, String resourceType, 
                         String resourceId, String operationType);
    
    /**
     * 检查是否有权限（使用枚举）
     *
     * @param tenantId 租户ID
     * @param domainId 领域ID
     * @param resourceType 资源类型
     * @param resourceId 资源ID，可以为null表示整个资源类型
     * @param operationType 操作类型
     * @return 是否有权限
     */
    boolean hasPermission(Integer tenantId, Integer domainId, ResourceTypeEnum resourceType, 
                         String resourceId, OperationTypeEnum operationType);
    
    /**
     * 检查当前上下文是否有权限
     *
     * @param resourceType 资源类型
     * @param resourceId 资源ID，可以为null表示整个资源类型
     * @param operationType 操作类型
     * @return 是否有权限
     */
    boolean hasPermission(String resourceType, String resourceId, String operationType);
    
    /**
     * 检查当前上下文是否有权限（使用枚举）
     *
     * @param resourceType 资源类型
     * @param resourceId 资源ID，可以为null表示整个资源类型
     * @param operationType 操作类型
     * @return 是否有权限
     */
    boolean hasPermission(ResourceTypeEnum resourceType, String resourceId, OperationTypeEnum operationType);
    
    /**
     * 检查是否有系统参数权限
     *
     * @param tenantId 租户ID
     * @param domainId 领域ID
     * @param paramKey 参数键
     * @param operationType 操作类型
     * @return 是否有权限
     */
    boolean hasSystemParamPermission(Integer tenantId, Integer domainId, String paramKey, String operationType);
    
    /**
     * 检查当前上下文是否有系统参数权限
     *
     * @param paramKey 参数键
     * @param operationType 操作类型
     * @return 是否有权限
     */
    boolean hasSystemParamPermission(String paramKey, String operationType);
    
    /**
     * 创建权限
     *
     * @param permission 权限信息
     * @return 权限ID
     */
    Long createPermission(DomainPermissionDO permission);
    
    /**
     * 更新权限
     *
     * @param permission 权限信息
     */
    void updatePermission(DomainPermissionDO permission);
    
    /**
     * 删除权限
     *
     * @param id 权限ID
     */
    void deletePermission(Long id);
}
