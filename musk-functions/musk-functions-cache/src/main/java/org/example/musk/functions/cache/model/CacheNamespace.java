package org.example.musk.functions.cache.model;

import lombok.Getter;

/**
 * 缓存命名空间枚举
 * <p>
 * 用于区分不同模块的缓存，确保缓存键的唯一性和规范性
 *
 * @author musk-functions-cache
 */
@Getter
public enum CacheNamespace {

    /**
     * 导航模块缓存
     */
    NAVIGATION("navigation"),

    /**
     * 菜单模块缓存
     */
    MENU("menu"),

    /**
     * 权限模块缓存
     */
    PERMISSION("permission"),

    /**
     * 租户级别缓存
     */
    TENANT("tenant"),

    /**
     * 会员级别缓存
     */
    MEMBER("member"),

    /**
     * 消息
     */
    MESSAGE("MESSAGE"),

    /**
     * 图标
     */
    ICON("ICON"),
    /**
     * 资源
     */
    RESOURCE("RESOURCE"),
    /**
     * 邀请
     */
    INVITATION("INVITATION"),

    /**
     * 系统参数缓存
     */
    SYSTEM_PARAMS("system:params");

    /**
     * 缓存前缀
     */
    private final String prefix;

    CacheNamespace(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 获取完整的缓存前缀
     *
     * @return 带有musk:前缀的缓存命名空间
     */
    public String getPrefix() {
        return "musk:" + prefix + ":";
    }
}
