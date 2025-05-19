package org.example.musk.functions.cache.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 缓存选项配置类
 * <p>
 * 用于配置缓存的过期时间、是否缓存空值等选项
 * 
 * @author musk-functions-cache
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheOptions {
    
    /**
     * 默认缓存过期时间（秒）：1小时
     */
    public static final long DEFAULT_EXPIRE_SECONDS = 3600;
    
    /**
     * 缓存过期时间（秒）
     */
    private long expireSeconds;
    
    /**
     * 是否缓存空值
     */
    private boolean cacheNullValues;
    
    /**
     * 创建默认缓存选项
     * <p>
     * 默认过期时间为1小时，不缓存空值
     * 
     * @return 默认缓存选项
     */
    public static CacheOptions defaultOptions() {
        return CacheOptions.builder()
                .expireSeconds(DEFAULT_EXPIRE_SECONDS)
                .cacheNullValues(false)
                .build();
    }
    
    /**
     * 创建自定义过期时间的缓存选项
     * 
     * @param expireSeconds 过期时间（秒）
     * @return 自定义过期时间的缓存选项
     */
    public static CacheOptions ofExpireSeconds(long expireSeconds) {
        return CacheOptions.builder()
                .expireSeconds(expireSeconds)
                .cacheNullValues(false)
                .build();
    }
    
    /**
     * 创建自定义缓存选项
     * 
     * @param expireSeconds 过期时间（秒）
     * @param cacheNullValues 是否缓存空值
     * @return 自定义缓存选项
     */
    public static CacheOptions of(long expireSeconds, boolean cacheNullValues) {
        return CacheOptions.builder()
                .expireSeconds(expireSeconds)
                .cacheNullValues(cacheNullValues)
                .build();
    }
}
