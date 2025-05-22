# 通用缓存模块 (musk-functions-cache)

## 简介

通用缓存模块是一个提供统一缓存管理功能的模块，旨在简化各业务模块的缓存实现，提高代码复用性和一致性。该模块基于Redis实现，提供了简单易用的API和注解，支持多种缓存操作场景。

## 主要功能

1. 统一的缓存键生成策略
2. 缓存的获取、设置、删除操作
3. 基于注解的声明式缓存
4. 支持SpEL表达式的缓存键和条件
5. 支持模式匹配的批量缓存清除
6. 多租户、多域的缓存隔离

## 技术特点

1. 基于Redis实现高性能缓存
2. 使用AOP实现声明式缓存
3. 支持自定义缓存过期时间
4. 支持缓存空值（可配置）
5. 提供优雅的降级机制，缓存异常不影响业务功能

## 使用方式

### 1. 编程式使用

```java
@Autowired
private CacheManager cacheManager;

@Autowired
private CacheKeyBuilder cacheKeyBuilder;

// 获取或计算缓存
public List<NavigationTreeVO> getNavigationTree(Integer tenantId, Integer domain, Integer platformType) {
    String cacheKey = cacheKeyBuilder.build(CacheNamespace.NAVIGATION, "tree", tenantId, domain, platformType);

    return cacheManager.getOrCompute(cacheKey, () -> {
        // 从数据库获取并构建树
        List<NavigationConfigDO> navigations = fetchFromDatabase(tenantId, domain, platformType);
        return buildNavigationTree(navigations, null);
    }, CacheOptions.builder().expireSeconds(3600).build());
}

// 清除缓存
public void clearNavigationCache(Integer tenantId, Integer domain, Integer platformType) {
    String pattern = cacheKeyBuilder.buildPattern(CacheNamespace.NAVIGATION, "*", tenantId, domain, platformType);
    cacheManager.removeByPattern(pattern);
}
```

### 2. 注解式使用

```java
@Service
public class MenuServiceImpl implements MenuService {

    // 基本用法 - 手动指定租户ID和域ID
    @Override
    @Cacheable(namespace = "MENU", key = "'tree:' + #tenantId + ':' + #domain", expireSeconds = 3600)
    public List<SystemMenuTreeVO> getMenuTreeByTenant(Integer tenantId, Integer domain) {
        // 从数据库获取并构建树
        List<SystemMenuDO> menus = fetchFromDatabase(tenantId, domain);
        return buildMenuTree(menus, null);
    }

    // 自动添加租户ID和域ID前缀
    @Override
    @Cacheable(namespace = "MENU", key = "'tree'", expireSeconds = 3600)
    public List<SystemMenuTreeVO> getMenuTree() {
        // 从线程上下文获取租户ID和域ID
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();

        // 从数据库获取并构建树
        List<SystemMenuDO> menus = fetchFromDatabase(tenantId, domainId);
        return buildMenuTree(menus, null);
    }

    // 只添加租户ID前缀，不添加域ID前缀
    @Override
    @Cacheable(namespace = "MENU", key = "'tree:' + #domain", expireSeconds = 3600, autoTenantPrefix = true, autoDomainPrefix = false)
    public List<SystemMenuTreeVO> getMenuTreeByDomain(Integer domain) {
        // 从线程上下文获取租户ID
        Integer tenantId = ThreadLocalTenantContext.getTenantId();

        // 从数据库获取并构建树
        List<SystemMenuDO> menus = fetchFromDatabase(tenantId, domain);
        return buildMenuTree(menus, null);
    }

    // 清除缓存 - 手动指定租户ID和域ID
    @Override
    @CacheEvict(namespace = "MENU", pattern = "'tree:' + #menu.tenantId + ':' + #menu.domainId")
    public void updateMenu(SystemMenuDO menu) {
        // 在数据库中更新
        updateInDatabase(menu);
    }

    // 清除缓存 - 自动添加租户ID和域ID前缀
    @Override
    @CacheEvict(namespace = "MENU", pattern = "'tree:*'")
    public void clearMenuCache() {
        log.info("清除菜单缓存");
    }
}
```

## 核心组件

### CacheManager

缓存管理器接口，提供缓存的基本操作，包括获取、设置、删除缓存等。

### CacheKeyBuilder

缓存键构建器，用于构建统一格式的缓存键，确保缓存键的一致性和规范性。

### CacheNamespace

缓存命名空间枚举，用于区分不同模块的缓存，避免缓存键冲突。

### CacheOptions

缓存选项配置类，用于配置缓存的过期时间、是否缓存空值等选项。

### @Cacheable

缓存注解，用于标记方法的返回值需要被缓存。支持以下属性：

- `namespace`: 缓存命名空间
- `key`: 缓存键，支持SpEL表达式
- `expireSeconds`: 缓存过期时间（秒）
- `cacheNullValues`: 是否缓存空值
- `condition`: 条件表达式，只有当表达式结果为true时才进行缓存
- `autoTenantPrefix`: 是否自动添加租户ID前缀，默认为true
- `autoDomainPrefix`: 是否自动添加域ID前缀，默认为true

### @CacheEvict

缓存清除注解，用于标记方法执行后需要清除缓存。支持以下属性：

- `namespace`: 缓存命名空间
- `key`: 缓存键，支持SpEL表达式
- `pattern`: 缓存键模式，用于批量删除缓存
- `allEntries`: 是否清除命名空间下的所有缓存
- `condition`: 条件表达式，只有当表达式结果为true时才清除缓存
- `beforeInvocation`: 是否在方法执行前清除缓存
- `autoTenantPrefix`: 是否自动添加租户ID前缀，默认为true
- `autoDomainPrefix`: 是否自动添加域ID前缀，默认为true

## 注意事项

1. 缓存键应该具有唯一性，避免不同数据使用相同的缓存键
2. 合理设置缓存过期时间，避免缓存过期时间过长导致数据不一致
3. 在数据更新时及时清除相关缓存
4. 对于频繁变化的数据，应谨慎使用缓存或设置较短的过期时间
5. 缓存的数据应该是可序列化的，以支持Redis存储
