package org.example.musk.functions.message.config;

import org.example.musk.functions.cache.config.MuskFunctionsCacheAutoConfiguration;
import org.example.musk.middleware.mq.redis.config.MuskRedisMQProducerAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 消息模块自动配置类
 *
 * @author musk-functions-message
 */
@AutoConfiguration(after = {MuskFunctionsCacheAutoConfiguration.class, MuskRedisMQProducerAutoConfiguration.class})
@ComponentScan(basePackages = "org.example.musk.functions.message")
public class MuskFunctionsMessageAutoConfiguration {
}
