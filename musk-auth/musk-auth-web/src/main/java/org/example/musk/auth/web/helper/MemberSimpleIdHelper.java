package org.example.musk.auth.web.helper;


import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.example.musk.auth.constants.cache.RedisCacheTenantConstant;
import org.example.musk.auth.constants.lock.RedisLockConstant;
import org.example.musk.auth.service.core.wrapper.lock.LockWrapper;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.middleware.redis.RedisUtil;
import org.springframework.stereotype.Service;

@Service
public class MemberSimpleIdHelper {

    private static final int ID_LENGTH = 8;

    /**
     * 步进数
     */
    private static final int SEQ = 1;

    @Resource
    private RedisUtil redisUtil;

    public  int generateSimpleId(String channel) {
        int id = 1;
        /**
         * 会员短id 生成 ，生成规则 ，总共生成长度是8位数字
         * 根据入参传过来的 来源渠道
         *
         * 然后去redis 里面找 一个特定的kye 来生成一个自增的数字
         * 如果redis 里面没有的话，那么就设置这个key
         * 否则就拿到这个自增key ，然后 返回数字
         *
         *   返回的数字格式   ： 两位来源渠道   ，如果来源渠道 是一位数字，那么就 前面补0 ， 后面的数字 根据 生成的最大长度-2 补0
         */
        String formattedChannel = StringUtils.leftPad(channel, 2, '0');
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        return LockWrapper.facadeLock(String.format(RedisLockConstant.TENANT_PACKAGE_CONTEXT_REDIS_CACHE_MEMBER_SIMPLE_ID_GENERATOR_CHANNEL_MAX_ID_LOCK,tenantId,  channel),()->{
            String redisKey = String.format(RedisCacheTenantConstant.TENANT_CONTEXT_REDIS_CACHE_MEMBER_SIMPLE_ID_GENERATOR_CHANNEL_MAX_ID,tenantId,  channel);
            Object object = redisUtil.get(redisKey);
            if (object == null) {
                int anInt = Integer.parseInt(formattedChannel + StringUtils.leftPad(String.valueOf(id), ID_LENGTH - 2, '0'));
                Thread.ofVirtual()
                        .start(() -> redisUtil.set(redisKey, anInt));
                return anInt;
            }
            Thread.ofVirtual()
                    .start(() -> redisUtil.incr(redisKey, SEQ));
            return (int) object + SEQ;
        });
    }
}
