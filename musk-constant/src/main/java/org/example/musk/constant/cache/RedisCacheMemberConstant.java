package org.example.musk.constant.cache;

/**
 * @Description: 会员级别的缓存key
 * @date 2024年08月05日
 */
public class RedisCacheMemberConstant {
    /**
     * 获取用户分佣等级统计
     */
    public static final String MEMBER_CONTEXT_REDIS_CACHE_TYPE_TENANT_PACKAGE_COUNT_CURRENT_MEMBER_BROKERAGE_INFO = "member_context_redis_cache_type_tenant_package_count_current_member_brokerage_info:%d:%d";

    /**
     * 会员个人信息
     */
    public static final String TENANT_PACKAGE_CONTEXT_REDIS_CACHE_TYPE_MEMBER_PROFILE = "tenant_package_context_redis_cache_type_member_profile:%d:%d";


    /**
     * 会员每日可取消的投资上限统计
     * 租户id
     * 会员id
     * 日期
     */
    public static final String TENANT_PACKAGE_CONTEXT_REDIS_CACHE_TYPE_MEMBER_DAILY_CANCEL_INVEST_PROJECT_MAX = "tenant_package_context_redis_cache_type_member_daily_cancel_invest_project_max:%d:%d:%s";

}
