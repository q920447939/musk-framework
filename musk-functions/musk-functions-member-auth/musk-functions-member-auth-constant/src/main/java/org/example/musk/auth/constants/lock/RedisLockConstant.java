package org.example.musk.auth.constants.lock;


/**
 * Redis锁常量
 *
 * @author
 * @date 2024/07/16
 */
public class RedisLockConstant {
    /**
     * 最大邀请ID生成  ，
     * 租户id
     * 注册渠道
     */
    public static final String TENANT_PACKAGE_CONTEXT_REDIS_CACHE_MEMBER_SIMPLE_ID_GENERATOR_CHANNEL_MAX_ID_LOCK = "tenant_package_context_redis_cache_member_simple_id_generator_channel_max_id_lock:%d:%s";



    /**
     * 注册用户统计 租户级
     * 租户id
     */
    public static final String TENANT_CONTEXT_REDIS_REGISTER_TOTAL_STATISTICS = "tenant_context_redis_register_total_statistics:%d";

    /**
     * 注册用户统计
     * 租户id
     * 日期
     */
    public static final String TENANT_CONTEXT_REDIS_REGISTER_DAILY_STATISTICS = "tenant_context_redis_register_daily_statistics:%d:%s";

    /**
     * 登录次数统计
     */
    public static final String TENANT_CONTEXT_REDIS_LOGIN_DAILY_STATISTICS = "tenant_context_redis_login_daily_statistics:%d:%s";

}
