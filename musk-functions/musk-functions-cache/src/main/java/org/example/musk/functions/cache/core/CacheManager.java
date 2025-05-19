package org.example.musk.functions.cache.core;

import org.example.musk.functions.cache.model.CacheOptions;

import java.util.function.Supplier;

/**
 * 缓存管理器接口
 * <p>
 * 提供统一的缓存操作接口，包括获取、设置、删除缓存等功能
 * 
 * @author musk-functions-cache
 */
public interface CacheManager {
    
    /**
     * 从缓存获取值，如果不存在，则计算并缓存
     * 
     * @param key 缓存键
     * @param valueLoader 值加载器，用于在缓存未命中时计算值
     * @param options 缓存选项
     * @param <T> 值类型
     * @return 缓存值或计算的值
     */
    <T> T getOrCompute(String key, Supplier<T> valueLoader, CacheOptions options);
    
    /**
     * 从缓存获取值，如果不存在，则计算并使用默认选项缓存
     * 
     * @param key 缓存键
     * @param valueLoader 值加载器，用于在缓存未命中时计算值
     * @param <T> 值类型
     * @return 缓存值或计算的值
     */
    <T> T getOrCompute(String key, Supplier<T> valueLoader);
    
    /**
     * 从缓存获取值
     * 
     * @param key 缓存键
     * @param type 值类型的Class
     * @param <T> 值类型
     * @return 缓存值，如果不存在则返回null
     */
    <T> T get(String key, Class<T> type);
    
    /**
     * 将值放入缓存
     * 
     * @param key 缓存键
     * @param value 缓存值
     * @param options 缓存选项
     * @param <T> 值类型
     */
    <T> void put(String key, T value, CacheOptions options);
    
    /**
     * 将值放入缓存，使用默认选项
     * 
     * @param key 缓存键
     * @param value 缓存值
     * @param <T> 值类型
     */
    <T> void put(String key, T value);
    
    /**
     * 从缓存中移除值
     * 
     * @param key 缓存键
     */
    void remove(String key);
    
    /**
     * 通过模式移除多个缓存值
     * 
     * @param pattern 缓存键模式，支持通配符
     */
    void removeByPattern(String pattern);
    
    /**
     * 检查缓存键是否存在
     * 
     * @param key 缓存键
     * @return 如果存在返回true，否则返回false
     */
    boolean exists(String key);
}
