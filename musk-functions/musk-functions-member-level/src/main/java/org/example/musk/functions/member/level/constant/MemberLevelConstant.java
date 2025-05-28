package org.example.musk.functions.member.level.constant;

/**
 * 会员等级常量类
 *
 * @author musk-functions-member-level
 */
public class MemberLevelConstant {


    /**
     * Redis缓存前缀
     */
    public static final String REDIS_CACHE_PREFIX = "musk:member:level:";

    /**
     * 会员当前等级缓存Key
     */
    public static final String MEMBER_CURRENT_LEVEL_CACHE_KEY = REDIS_CACHE_PREFIX + "current:";

    /**
     * 会员积分缓存Key
     */
    public static final String MEMBER_POINTS_CACHE_KEY = REDIS_CACHE_PREFIX + "points:";

    /**
     * 会员成长值缓存Key
     */
    public static final String MEMBER_GROWTH_VALUE_CACHE_KEY = REDIS_CACHE_PREFIX + "growth:";

    /**
     * 等级定义列表缓存Key
     */
    public static final String LEVEL_DEFINITION_LIST_CACHE_KEY = REDIS_CACHE_PREFIX + "definition:list:";

    /**
     * 等级权益列表缓存Key
     */
    public static final String LEVEL_BENEFIT_LIST_CACHE_KEY = REDIS_CACHE_PREFIX + "benefit:list:";

    /**
     * 会员当前等级缓存过期时间（秒）
     */
    public static final long MEMBER_CURRENT_LEVEL_CACHE_EXPIRE_SECONDS = 3600;

    /**
     * 会员积分缓存过期时间（秒）
     */
    public static final long MEMBER_POINTS_CACHE_EXPIRE_SECONDS = 3600;

    /**
     * 会员成长值缓存过期时间（秒）
     */
    public static final long MEMBER_GROWTH_VALUE_CACHE_EXPIRE_SECONDS = 3600;

    /**
     * 等级定义列表缓存过期时间（秒）
     */
    public static final long LEVEL_DEFINITION_LIST_CACHE_EXPIRE_SECONDS = 86400;

    /**
     * 等级权益列表缓存过期时间（秒）
     */
    public static final long LEVEL_BENEFIT_LIST_CACHE_EXPIRE_SECONDS = 86400;
}
