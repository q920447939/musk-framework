package org.example.musk.framework.permission.constant;

/**
 * 权限常量类
 *
 * @author musk-framework-permission
 */
public class PermissionConstant {

    /**
     * 权限缓存前缀
     */
    public static final String PERMISSION_CACHE_PREFIX = "musk:permission:";
    
    /**
     * 权限缓存Key格式
     * 参数：tenantId, domainId, resourceType, resourceId, operationType
     */
    public static final String PERMISSION_CACHE_KEY = PERMISSION_CACHE_PREFIX + "%d:%d:%s:%s:%s";
    
    /**
     * 权限缓存过期时间（秒）
     */
    public static final long PERMISSION_CACHE_EXPIRE_SECONDS = 3600;
    
    /**
     * 领域ID请求头
     */
    public static final String DOMAIN_ID_HEADER = "X-Domain-Id";
    
    /**
     * 默认领域ID
     */
    public static final Integer DEFAULT_DOMAIN_ID = 1;
}
