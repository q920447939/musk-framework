# 导航模块 (musk-functions-navigation)

## 简介

导航模块是一个通用的导航管理功能模块，支持多租户、多层级的导航系统。可以用于不同域（如APP、WEB）的导航管理。

## 主要功能

1. 导航的增删改查
2. 导航树的构建和查询
3. 基于租户的导航管理
4. 支持不同域（如APP、WEB）的导航配置
5. 支持多层级导航结构
6. 支持多平台（APP、WEB等）

## 技术特点

1. 基于 MyBatis-Plus 实现数据访问
2. 使用 Redis 缓存导航树，提高查询性能
3. 支持多租户隔离
4. 支持多层级导航结构
5. 支持多平台导航配置

## 数据库设计

导航配置表 (`app_navigation_config`) 包含以下主要字段：

- `id`: 导航ID
- `tenant_id`: 租户ID
- `domain_id`: 所属域(1:APP)
- `navigator_name`: 导航名称
- `navigator_icon_id`: 导航图标ID
- `display_index`: 显示顺序
- `target_uri`: 目标地址
- `uri_params`: 地址参数
- `remark`: 备注
- `status`: 状态（0正常 1停用）
- `platform_type`: 平台类型（1:APP 2:WEB）
- `navigator_level`: 导航层级(0表示首层)
- `parent_navigator_id`: 父导航ID(如果层级为0,那么父导航ID为null)

## API接口

### 创建导航

- 请求方式：POST
- 请求路径：/api/navigation/create
- 请求参数：NavigationConfigCreateReqVO

### 更新导航

- 请求方式：PUT
- 请求路径：/api/navigation/update
- 请求参数：NavigationConfigUpdateReqVO

### 删除导航

- 请求方式：DELETE
- 请求路径：/api/navigation/delete
- 请求参数：id (导航ID)

### 获取导航

- 请求方式：GET
- 请求路径：/api/navigation/get
- 请求参数：id (导航ID)

### 获取指定域和平台下的所有导航

- 请求方式：GET
- 请求路径：/api/navigation/list-by-domain-platform
- 请求参数：domain (域), platformType (平台类型)

### 获取指定父导航下的子导航

- 请求方式：GET
- 请求路径：/api/navigation/list-by-parent
- 请求参数：parentId (父导航ID)

### 获取导航树

- 请求方式：GET
- 请求路径：/api/navigation/tree
- 请求参数：domain (域), platformType (平台类型)

### 获取当前租户的导航树

- 请求方式：GET
- 请求路径：/api/navigation/tree/current-tenant
- 请求参数：domain (域), platformType (平台类型)

## 使用示例

```java
@Resource
private NavigationConfigService navigationConfigService;

// 创建导航
NavigationConfigDO navigation = new NavigationConfigDO();
navigation.setDomainId(NavigationDomainEnum.APP.getDomain());
navigation.setPlatformType(NavigationPlatformEnum.APP.getType());
navigation.setNavigatorName("首页");
navigation.setNavigatorLevel(0);
navigation.setNavigatorIconId(1);
navigation.setDisplayIndex((short) 1);
navigation.setTargetUri("/home");
navigation.setStatus(NavigatorStatusEnum.ENABLE.getCode());
Integer navigationId = navigationConfigService.createNavigationConfig(navigation);

// 获取导航树
List<NavigationTreeVO> navigationTree = navigationConfigService.getNavigationTree(
        NavigationDomainEnum.APP.getDomain(), 
        NavigationPlatformEnum.APP.getType());
```

## 注意事项

1. 导航层级与父导航ID必须匹配，一级导航的父导航ID必须为空，非一级导航的父导航ID不能为空
2. 删除导航前，需要确保该导航没有子导航
3. 导航树会被缓存，以提高查询性能
4. 平台类型和域ID必须正确设置，以确保导航在正确的平台和域下显示
