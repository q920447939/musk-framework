package org.example.musk.functions.cache.core.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.musk.functions.cache.core.CacheManager;
import org.example.musk.functions.cache.model.CacheOptions;
import org.example.musk.middleware.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Redis缓存管理器实现
 * <p>
 * 基于Redis实现的缓存管理器，提供统一的缓存操作接口
 * 
 * @author musk-functions-cache
 */
@Slf4j
@Component("functionsCacheManager")
public class FunctionsCacheManagerImpl implements CacheManager {
    
    @Autowired
    private RedisUtil redisUtil;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public <T> T getOrCompute(String key, Supplier<T> valueLoader, CacheOptions options) {
        try {
            // 尝试从缓存获取
            @SuppressWarnings("unchecked")
            T value = (T) redisUtil.get(key);
            if (value != null) {
                log.debug("缓存命中: {}", key);
                return value;
            }
            
            log.debug("缓存未命中，从数据源加载: {}", key);
            // 缓存未命中，计算值
            value = valueLoader.get();
            
            // 缓存值（如果不为null或者配置了缓存null值）
            if (value != null || options.isCacheNullValues()) {
                redisUtil.set(key, value, options.getExpireSeconds());
                log.debug("值已缓存: {}, 过期时间: {}秒", key, options.getExpireSeconds());
            }
            
            return value;
        } catch (Exception e) {
            log.error("缓存操作异常: {}", key, e);
            // 发生异常时，直接从数据源加载，确保功能正常
            return valueLoader.get();
        }
    }
    
    @Override
    public <T> T getOrCompute(String key, Supplier<T> valueLoader) {
        return getOrCompute(key, valueLoader, CacheOptions.defaultOptions());
    }
    
    @Override
    public <T> T get(String key, Class<T> type) {
        try {
            @SuppressWarnings("unchecked")
            T value = (T) redisUtil.get(key);
            return value;
        } catch (Exception e) {
            log.error("获取缓存异常: {}", key, e);
            return null;
        }
    }
    
    @Override
    public <T> void put(String key, T value, CacheOptions options) {
        try {
            if (value != null || options.isCacheNullValues()) {
                redisUtil.set(key, value, options.getExpireSeconds());
                log.debug("值已缓存: {}, 过期时间: {}秒", key, options.getExpireSeconds());
            }
        } catch (Exception e) {
            log.error("设置缓存异常: {}", key, e);
        }
    }
    
    @Override
    public <T> void put(String key, T value) {
        put(key, value, CacheOptions.defaultOptions());
    }
    
    @Override
    public void remove(String key) {
        try {
            redisUtil.del(key);
            log.debug("缓存已删除: {}", key);
        } catch (Exception e) {
            log.error("删除缓存异常: {}", key, e);
        }
    }
    
    @Override
    public void removeByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.debug("已删除匹配模式的缓存: {}, 共{}个", pattern, keys.size());
            }
        } catch (Exception e) {
            log.error("批量删除缓存异常: {}", pattern, e);
        }
    }
    
    @Override
    public boolean exists(String key) {
        try {
            return redisUtil.hasKey(key);
        } catch (Exception e) {
            log.error("检查缓存是否存在异常: {}", key, e);
            return false;
        }
    }
}
