# 图标模块 (musk-functions-icon)

## 简介

图标模块是一个通用的图标管理功能模块，支持多租户、多域的图标系统。可以用于不同平台（如APP、WEB）的图标管理，为各个系统/模块提供统一的图标管理和维护功能。

## 主要功能

1. 图标的增删改查
2. 图标分类管理
3. 图标资源管理（支持多平台、多分辨率）
4. 基于租户和域的图标隔离
5. 支持不同平台（APP、WEB等）的图标配置

## 技术特点

1. 基于 MyBatis-Plus 实现数据访问
2. 使用 Redis 缓存图标数据，提高查询性能
3. 支持多租户隔离
4. 支持多域隔离
5. 支持多平台图标配置

## 数据库设计

### 图标主表 (system_icon)

- `id`: 图标ID
- `tenant_id`: 租户ID
- `domain_id`: 域ID
- `icon_name`: 图标名称
- `icon_code`: 图标编码（唯一标识）
- `category_id`: 分类ID
- `description`: 图标描述
- `status`: 状态（0正常 1停用）
- `remark`: 备注

### 图标资源表 (system_icon_resource)

- `id`: 资源ID
- `icon_id`: 图标ID
- `platform_type`: 平台类型（1:APP 2:WEB 3:通用）
- `resource_type`: 资源类型（1:URL 2:Base64 3:字体图标）
- `resource_url`: 资源URL
- `resource_content`: 资源内容（Base64或字体图标代码）
- `width`: 宽度（像素）
- `height`: 高度（像素）
- `version`: 版本号
- `is_default`: 是否默认资源

### 图标分类表 (system_icon_category)

- `id`: 分类ID
- `tenant_id`: 租户ID
- `domain_id`: 域ID
- `category_name`: 分类名称
- `category_code`: 分类编码
- `parent_id`: 父分类ID
- `display_order`: 显示顺序
- `status`: 状态（0正常 1停用）
- `remark`: 备注

## API接口

### 图标管理

- 创建图标: POST /api/icon/create
- 更新图标: PUT /api/icon/update
- 删除图标: DELETE /api/icon/delete
- 获取图标: GET /api/icon/get
- 根据编码获取图标: GET /api/icon/get-by-code
- 获取分类下的图标: GET /api/icon/list-by-category
- 搜索图标: GET /api/icon/search

### 图标资源管理

- 创建图标资源: POST /api/icon-resource/create
- 更新图标资源: PUT /api/icon-resource/update
- 删除图标资源: DELETE /api/icon-resource/delete
- 获取图标资源: GET /api/icon-resource/get
- 获取图标的所有资源: GET /api/icon/get-resources
- 获取图标在特定平台的资源: GET /api/icon/get-platform-resources
- 获取图标的默认资源: GET /api/icon/get-default-resource

### 图标分类管理

- 创建分类: POST /api/icon-category/create
- 更新分类: PUT /api/icon-category/update
- 删除分类: DELETE /api/icon-category/delete
- 获取分类: GET /api/icon-category/get
- 获取顶级分类: GET /api/icon-category/list-root
- 获取子分类: GET /api/icon-category/list-children
- 获取分类树: GET /api/icon-category/tree

## 使用示例

```java
@Resource
private SystemIconService systemIconService;

@Resource
private SystemIconResourceService systemIconResourceService;

// 创建图标
SystemIconDO icon = new SystemIconDO();
icon.setIconName("首页图标");
icon.setIconCode("home_icon");
icon.setCategoryId(1);
icon.setDescription("首页导航图标");
icon.setStatus(0); // 正常状态
Integer iconId = systemIconService.createIcon(icon);

// 创建图标资源
SystemIconResourceDO resource = new SystemIconResourceDO();
resource.setIconId(iconId);
resource.setPlatformType(1); // APP平台
resource.setResourceType(1); // URL类型
resource.setResourceUrl("https://example.com/icons/home.png");
resource.setWidth(24);
resource.setHeight(24);
resource.setIsDefault(true);
Integer resourceId = systemIconResourceService.createIconResource(resource);

// 获取图标的默认资源
SystemIconResourceDO defaultResource = systemIconResourceService.getDefaultResource(iconId, 1);
```

## 注意事项

1. 图标编码在同一租户和域下必须唯一
2. 删除图标前，需要确保该图标没有被其他模块引用
3. 图标资源支持多种类型，包括URL、Base64和字体图标
4. 图标数据会被缓存，以提高查询性能
