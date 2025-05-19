package org.example.musk.functions.cache.core.impl;

import org.example.musk.functions.cache.core.CacheKeyBuilder;
import org.example.musk.functions.cache.model.CacheNamespace;
import org.springframework.stereotype.Component;

/**
 * 默认缓存键构建器实现
 * <p>
 * 提供标准的缓存键构建逻辑，确保缓存键的一致性和规范性
 * 
 * @author musk-functions-cache
 */
@Component
public class DefaultCacheKeyBuilder implements CacheKeyBuilder {
    
    /**
     * 分隔符
     */
    private static final String SEPARATOR = ":";
    
    @Override
    public String build(CacheNamespace namespace, Object... params) {
        return buildKey(namespace.getPrefix(), null, params);
    }
    
    @Override
    public String build(CacheNamespace namespace, String type, Object... params) {
        return buildKey(namespace.getPrefix(), type, params);
    }
    
    @Override
    public String buildPattern(CacheNamespace namespace, String pattern, Object... params) {
        StringBuilder keyBuilder = new StringBuilder(namespace.getPrefix());
        
        // 添加类型（如果有）
        if (pattern != null && !pattern.isEmpty()) {
            keyBuilder.append(pattern).append(SEPARATOR);
        }
        
        // 添加参数
        for (Object param : params) {
            if (param != null) {
                keyBuilder.append(param).append(SEPARATOR);
            }
        }
        
        // 移除尾部分隔符并添加通配符
        if (keyBuilder.charAt(keyBuilder.length() - 1) == ':') {
            keyBuilder.deleteCharAt(keyBuilder.length() - 1);
        }
        
        // 添加通配符，用于模式匹配
        keyBuilder.append("*");
        
        return keyBuilder.toString();
    }
    
    /**
     * 构建缓存键的内部方法
     * 
     * @param prefix 前缀
     * @param type 类型
     * @param params 参数
     * @return 构建的缓存键
     */
    private String buildKey(String prefix, String type, Object... params) {
        StringBuilder keyBuilder = new StringBuilder(prefix);
        
        // 添加类型（如果有）
        if (type != null && !type.isEmpty()) {
            keyBuilder.append(type).append(SEPARATOR);
        }
        
        // 添加参数
        for (Object param : params) {
            if (param != null) {
                keyBuilder.append(param).append(SEPARATOR);
            }
        }
        
        // 移除尾部分隔符
        if (keyBuilder.charAt(keyBuilder.length() - 1) == ':') {
            keyBuilder.deleteCharAt(keyBuilder.length() - 1);
        }
        
        return keyBuilder.toString();
    }
}
