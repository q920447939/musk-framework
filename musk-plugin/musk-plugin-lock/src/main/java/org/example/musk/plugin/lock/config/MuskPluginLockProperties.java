/*
package org.example.musk.plugin.lock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

*/
/**
 * Cache 配置项
 *
 * @author Wanwan
 *//*

@ConfigurationProperties("musk.frame.middleware.redis.cache")
@Data
@Validated
public class MuskPluginLockProperties {

    */
/**
     * {@link #redisScanBatchSize} 默认值
     *//*

    private static final Integer REDIS_SCAN_BATCH_SIZE_DEFAULT = 30;

    */
/**
     * redis scan 一次返回数量
     *//*

    private Integer redisScanBatchSize = REDIS_SCAN_BATCH_SIZE_DEFAULT;

}
*/
