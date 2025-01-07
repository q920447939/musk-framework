package org.example.musk.middleware.mq.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.example.musk.middleware.mq.redis.core.RedisMQTemplate;
import org.example.musk.middleware.mq.redis.core.interceptor.RedisMessageInterceptor;
import org.example.musk.middleware.redis.config.MuskRedisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * Redis 消息队列 Producer 配置类
 *
 * @author musk
 */
@Slf4j
@AutoConfiguration(after = MuskRedisAutoConfiguration.class)
public class MuskRedisMQProducerAutoConfiguration {

    @Bean
    public RedisMQTemplate redisMQTemplate(StringRedisTemplate redisTemplate,
                                           List<RedisMessageInterceptor> interceptors) {
        RedisMQTemplate redisMQTemplate = new RedisMQTemplate(redisTemplate);
        // 添加拦截器
        interceptors.forEach(redisMQTemplate::addInterceptor);
        return redisMQTemplate;
    }

}
