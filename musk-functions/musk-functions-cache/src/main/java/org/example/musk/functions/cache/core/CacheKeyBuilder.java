package org.example.musk.functions.cache.core;

import org.example.musk.functions.cache.model.CacheNamespace;

/**
 * 缓存键构建器接口
 * <p>
 * 用于构建统一格式的缓存键，确保缓存键的一致性和规范性
 * 
 * @author musk-functions-cache
 */
public interface CacheKeyBuilder {
    
    /**
     * 使用命名空间和参数构建缓存键
     * 
     * @param namespace 缓存命名空间
     * @param params 缓存键参数，可以是多个对象
     * @return 构建的缓存键
     */
    String build(CacheNamespace namespace, Object... params);
    
    /**
     * 使用命名空间、类型和参数构建缓存键
     * 
     * @param namespace 缓存命名空间
     * @param type 缓存类型，如"list"、"tree"等
     * @param params 缓存键参数，可以是多个对象
     * @return 构建的缓存键
     */
    String build(CacheNamespace namespace, String type, Object... params);
    
    /**
     * 构建模式匹配的缓存键，用于批量删除
     * 
     * @param namespace 缓存命名空间
     * @param pattern 匹配模式，如"*"表示所有
     * @param params 缓存键参数，可以是多个对象
     * @return 构建的模式匹配缓存键
     */
    String buildPattern(CacheNamespace namespace, String pattern, Object... params);
}
