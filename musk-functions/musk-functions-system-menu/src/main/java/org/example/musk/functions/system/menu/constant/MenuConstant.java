package org.example.musk.functions.system.menu.constant;

/**
 * 菜单常量类
 *
 * @author musk-functions-menu
 */
public class MenuConstant {

    /**
     * 根菜单层级
     */
    public static final Integer ROOT_MENU_LEVEL = 0;

    /**
     * 数据源名称
     */
    public static final String DB_NAME = "menu";

    /**
     * Redis缓存前缀
     */
    public static final String REDIS_CACHE_PREFIX = "musk:menu:";

    /**
     * 菜单树缓存Key
     */
    public static final String MENU_TREE_CACHE_KEY = REDIS_CACHE_PREFIX + "tree:";

    /**
     * 菜单缓存过期时间（秒）
     */
    public static final long MENU_CACHE_EXPIRE_SECONDS = 3600;
}
