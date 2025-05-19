package org.example.musk.functions.cache.example;

import lombok.extern.slf4j.Slf4j;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.cache.core.CacheKeyBuilder;
import org.example.musk.functions.cache.core.CacheManager;
import org.example.musk.functions.cache.model.CacheNamespace;
import org.example.musk.functions.cache.model.CacheOptions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存使用示例类
 * <p>
 * 本类仅用于展示缓存模块的使用方式，不参与实际业务逻辑
 * 
 * @author musk-functions-cache
 */
@Slf4j
public class CacheUsageExample {
    
    @Autowired
    private CacheManager cacheManager;
    
    @Autowired
    private CacheKeyBuilder cacheKeyBuilder;
    
    /**
     * 编程式缓存示例 - 获取数据
     */
    public List<String> getDataWithProgrammaticCache(Integer tenantId, Integer domain) {
        // 构建缓存键
        String cacheKey = cacheKeyBuilder.build(CacheNamespace.TENANT, "data", tenantId, domain);
        
        // 从缓存获取或计算
        return cacheManager.getOrCompute(cacheKey, () -> {
            log.info("从数据源加载数据: tenantId={}, domain={}", tenantId, domain);
            // 模拟从数据库获取数据
            return fetchDataFromDatabase(tenantId, domain);
        }, CacheOptions.ofExpireSeconds(1800)); // 自定义过期时间为30分钟
    }
    
    /**
     * 编程式缓存示例 - 清除缓存
     */
    public void clearDataCache(Integer tenantId, Integer domain) {
        // 构建缓存键
        String cacheKey = cacheKeyBuilder.build(CacheNamespace.TENANT, "data", tenantId, domain);
        
        // 删除缓存
        cacheManager.remove(cacheKey);
        log.info("已清除缓存: {}", cacheKey);
    }
    
    /**
     * 编程式缓存示例 - 批量清除缓存
     */
    public void clearAllDataCacheForTenant(Integer tenantId) {
        // 构建模式匹配的缓存键
        String pattern = cacheKeyBuilder.buildPattern(CacheNamespace.TENANT, "data", tenantId);
        
        // 批量删除缓存
        cacheManager.removeByPattern(pattern);
        log.info("已批量清除缓存: {}", pattern);
    }
    
    /**
     * 注解式缓存示例 - 获取数据
     */
    @Cacheable(namespace = "TENANT", key = "'data:' + #tenantId + ':' + #domain", expireSeconds = 1800)
    public List<String> getDataWithAnnotationCache(Integer tenantId, Integer domain) {
        log.info("从数据源加载数据: tenantId={}, domain={}", tenantId, domain);
        // 模拟从数据库获取数据
        return fetchDataFromDatabase(tenantId, domain);
    }
    
    /**
     * 注解式缓存示例 - 清除缓存
     */
    @CacheEvict(namespace = "TENANT", key = "'data:' + #tenantId + ':' + #domain")
    public void clearDataCacheWithAnnotation(Integer tenantId, Integer domain) {
        log.info("更新数据并清除缓存: tenantId={}, domain={}", tenantId, domain);
        // 模拟更新数据库
        updateDatabase(tenantId, domain);
    }
    
    /**
     * 注解式缓存示例 - 批量清除缓存
     */
    @CacheEvict(namespace = "TENANT", pattern = "'data:' + #tenantId + ':*'")
    public void clearAllDataCacheForTenantWithAnnotation(Integer tenantId) {
        log.info("批量清除租户的所有缓存: tenantId={}", tenantId);
        // 模拟批量更新数据库
        batchUpdateDatabase(tenantId);
    }
    
    /**
     * 注解式缓存示例 - 条件缓存
     */
    @Cacheable(
            namespace = "TENANT", 
            key = "'data:' + #tenantId + ':' + #domain", 
            condition = "#domain != null && #domain > 0"
    )
    public List<String> getDataWithCondition(Integer tenantId, Integer domain) {
        log.info("从数据源加载数据: tenantId={}, domain={}", tenantId, domain);
        // 模拟从数据库获取数据
        return fetchDataFromDatabase(tenantId, domain);
    }
    
    /**
     * 模拟从数据库获取数据
     */
    private List<String> fetchDataFromDatabase(Integer tenantId, Integer domain) {
        // 模拟数据库查询延迟
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 模拟数据
        List<String> data = new ArrayList<>();
        data.add("数据项1 - 租户" + tenantId + " - 域" + domain);
        data.add("数据项2 - 租户" + tenantId + " - 域" + domain);
        data.add("数据项3 - 租户" + tenantId + " - 域" + domain);
        return data;
    }
    
    /**
     * 模拟更新数据库
     */
    private void updateDatabase(Integer tenantId, Integer domain) {
        // 模拟数据库更新
        log.info("数据库更新成功: tenantId={}, domain={}", tenantId, domain);
    }
    
    /**
     * 模拟批量更新数据库
     */
    private void batchUpdateDatabase(Integer tenantId) {
        // 模拟批量数据库更新
        log.info("批量数据库更新成功: tenantId={}", tenantId);
    }
}
