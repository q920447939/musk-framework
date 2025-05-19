# 领域权限控制模块 (musk-framework-permission)

## 简介

领域权限控制模块是一个通用的权限管理功能模块，支持多租户、多领域的权限控制系统。可以用于控制不同领域（如A、B）对同一资源的不同操作权限。

## 主要功能

1. 基于租户和领域的权限控制
2. 支持资源类型和资源实例级别的权限控制
3. 支持不同操作类型（创建、读取、更新、删除）的权限控制
4. 提供注解和AOP方式进行权限检查
5. 支持系统参数的分组权限控制

## 技术特点

1. 基于 Spring AOP 实现权限拦截
2. 使用 Redis 缓存权限检查结果，提高性能
3. 支持多租户隔离
4. 支持细粒度权限控制

## 数据库设计

领域权限表 (`domain_permission`) 包含以下主要字段：

- `id`: 权限ID
- `tenant_id`: 租户ID
- `domain_id`: 领域ID
- `resource_type`: 资源类型（如MENU、SYSTEM_PARAMS等）
- `resource_id`: 资源ID（可以为空表示整个资源类型）
- `operation_type`: 操作类型（如CREATE、READ、UPDATE、DELETE）
- `is_allowed`: 是否允许（1表示允许，0表示不允许）
- `description`: 权限描述

## 使用方式

### 1. 配置领域ID

在请求头中添加领域ID：

```
X-Domain-Id: 1
```

### 2. 使用注解方式进行权限控制

```java
@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Resource
    private MenuService menuService;
    
    @GetMapping("/get")
    @RequirePermission(resourceType = ResourceTypeEnum.MENU, operationType = OperationTypeEnum.READ)
    public CommonResult<MenuRespVO> getMenu(@RequestParam("id") Long id) {
        MenuDO menu = menuService.getMenu(id);
        return CommonResult.success(BeanUtils.toBean(menu, MenuRespVO.class));
    }
}
```

### 3. 使用服务方式进行权限检查

```java
@Service
public class YourService {

    @Resource
    private DomainPermissionService domainPermissionService;
    
    public void someMethod(String paramKey) {
        // 检查当前上下文是否有权限
        if (!domainPermissionService.hasSystemParamPermission(paramKey, OperationTypeEnum.UPDATE.getCode())) {
            throw new BusinessException("没有权限更新此配置项");
        }
        
        // 执行业务逻辑
        // ...
    }
}
```

## 权限检查流程

1. 首先检查特定资源实例的权限
2. 如果未找到特定资源实例的权限，则检查资源类型的权限
3. 对于系统参数，还会检查参数所属分组的权限

## 权限缓存

权限检查结果会被缓存，以提高性能：

- 缓存键格式：`musk:permission:{tenantId}:{domainId}:{resourceType}:{resourceId}:{operationType}`
- 缓存过期时间：1小时

## 注意事项

1. 确保在使用权限检查前，已经设置了租户ID和领域ID
2. 权限检查的优先级：特定资源 > 资源分组 > 资源类型
3. 更新权限后，相关的缓存会被自动清除
