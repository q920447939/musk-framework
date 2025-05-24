package org.example.musk.auth.constants.cache;

/**
 * @Description: 租户级别的缓存key
 * @date 2024年08月05日
 */
public class RedisCacheTenantConstant {




    /**
     * 根据导航code查询导航配置
     * 租户id
     * json请求参数
     */
    public static final String TENANT_CONTEXT_REDIS_CACHE_TYPE_TENANT_CUSTOMER_NAVIGATOR_CODE = "tenant_context_redis_cache_type_tenant_customer_navigator_code:%d:%s";

    /**
     * 实名认证
     */
    public static final String TENANT_CONTEXT_REDIS_CACHE_TYPE_TENANT_REAL_NAME_COUNTRY = "tenant_context_redis_cache_type_tenant_real_name_country:%d";


    /**
     * 新闻
     */
    public static final String TENANT_CONTEXT_REDIS_CACHE_TYPE_TENANT_NEWS_PAGE = "tenant_context_redis_cache_type_tenant_news_page:%d:%d:%d";


    /**
     * 滚动公告
     * 租户
     * 开始页
     * 每页条数
     */
    public static final String TENANT_CONTEXT_REDIS_CACHE_TYPE_TENANT_ROLL_NOTICE_PAGE = "tenant_context_redis_cache_type_tenant_roll_notice_page:%d:%d:%d";

    /**
     * 抽奖模拟中奖用户
     */
    public static final String TENANT_CONTEXT_REDIS_CACHE_LUCK_LOTTERY_MOCK_DRAW_MEMBER = "tenant_context_redis_cache_luck_lottery_mock_draw_member:%d:%s";

    /**
     * 抽奖模拟中奖用户
     */
    public static final String TENANT_CONTEXT_REDIS_CACHE_SYSTEM_APP_PARAMS = "tenant_context_redis_cache_system_app_params:%d:%s";

    /**
     * 最大邀请ID生成  ，
     * 租户id
     * 注册渠道
     */
    public static final String TENANT_CONTEXT_REDIS_CACHE_MEMBER_SIMPLE_ID_GENERATOR_CHANNEL_MAX_ID = "tenant_context_redis_cache_member_simple_id_generator_channel_max_id:%d:%s";


    /**
     * 未登录时的通告
     */
    public static final String TENANT_CONTEXT_REDIS_CACHE_TYPE_TENANT_NOTICE = "tenant_context_redis_cache_type_tenant_notice:%d";


    /**
     * 登录频率限制
     * 租户id
     * 客户端ip
     */
    public static final String TENANT_CONTEXT_REDIS_CACHE_TYPE_REQUEST_LIMIT_RATE = "tenant_context_redis_cache_type_request_limit_rate:%d:%s";


}
