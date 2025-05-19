package org.example.musk.functions.navigation.constant;

/**
 * 导航常量类
 *
 * @author musk-functions-navigation
 */
public class NavigationConstant {

    /**
     * 根导航层级
     */
    public static final Integer ROOT_NAVIGATION_LEVEL = 0;

    /**
     * 数据源名称
     */
    public static final String DB_NAME = "navigation";

    /**
     * Redis缓存前缀
     */
    public static final String REDIS_CACHE_PREFIX = "musk:navigation:";

    /**
     * 导航缓存Key
     * 参数：tenantId, domainId, platformType
     */
    public static final String NAVIGATION_CACHE_KEY = REDIS_CACHE_PREFIX + "list:%d:%d:%d";

    /**
     * 导航树缓存Key
     * 参数：tenantId, domainId, platformType
     */
    public static final String NAVIGATION_TREE_CACHE_KEY = REDIS_CACHE_PREFIX + "tree:%d:%d:%d";

    /**
     * 导航缓存过期时间（秒）
     */
    public static final long NAVIGATION_CACHE_EXPIRE_SECONDS = 3600;
}
