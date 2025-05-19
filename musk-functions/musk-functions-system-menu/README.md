# 菜单模块 (musk-functions-menu)

## 简介

菜单模块是一个通用的菜单管理功能模块，支持多租户、多层级的菜单系统。可以用于不同域（如APP）的菜单管理。

## 主要功能

1. 菜单的增删改查
2. 菜单树的构建和查询
3. 基于租户的菜单管理
4. 支持不同域（如APP）的菜单配置

## 技术特点

1. 基于 MyBatis-Plus 实现数据访问
2. 使用 Redis 缓存菜单树，提高查询性能
3. 支持多租户隔离
4. 支持多层级菜单结构

## 数据库设计

菜单表 (`menu`) 包含以下主要字段：

- `id`: 菜单ID
- `tenant_id`: 租户ID
- `domain`: 所属域(1:APP)
- `menu_name`: 菜单名称
- `menu_level`: 菜单层级(0表示首层)
- `parent_menu_id`: 父菜单ID(如果层级为0,那么父菜单ID为null)
- `menu_relation_icon_id`: 菜单关联ICON
- `menu_icon_width`: 菜单ICON宽度
- `menu_icon_height`: 菜单ICON高度
- `menu_on_click_path`: 菜单点击跳转路径
- `params`: 携带参数(JSON格式)

## API接口

### 创建菜单

- 请求方式：POST
- 请求路径：/api/menu/create
- 请求参数：MenuCreateReqVO

### 更新菜单

- 请求方式：PUT
- 请求路径：/api/menu/update
- 请求参数：MenuUpdateReqVO

### 删除菜单

- 请求方式：DELETE
- 请求路径：/api/menu/delete
- 请求参数：id (菜单ID)

### 获取菜单

- 请求方式：GET
- 请求路径：/api/menu/get
- 请求参数：id (菜单ID)

### 获取指定域下的所有菜单

- 请求方式：GET
- 请求路径：/api/menu/list-by-domain
- 请求参数：domain (域)

### 获取指定父菜单下的子菜单

- 请求方式：GET
- 请求路径：/api/menu/list-by-parent
- 请求参数：parentId (父菜单ID)

### 获取菜单树

- 请求方式：GET
- 请求路径：/api/menu/tree
- 请求参数：domain (域)

### 获取当前租户的菜单树

- 请求方式：GET
- 请求路径：/api/menu/tree/current-tenant
- 请求参数：domain (域)

## 使用示例

```java
@Resource
private MenuService menuService;

// 创建菜单
MenuDO menu = new MenuDO();
menu.setDomain(MenuDomainEnum.APP.getDomain());
menu.setMenuName("首页");
menu.setMenuLevel(0);
menu.setMenuRelationIconId(1);
menu.setMenuIconWidth(24.0);
menu.setMenuIconHeight(24.0);
menu.setMenuOnClickPath("/home");
Long menuId = menuService.createMenu(menu);

// 获取菜单树
List<MenuTreeVO> menuTree = menuService.getMenuTree(MenuDomainEnum.APP.getDomain());
```

## 注意事项

1. 菜单层级与父菜单ID必须匹配，一级菜单的父菜单ID必须为空，非一级菜单的父菜单ID不能为空
2. 删除菜单前，需要确保该菜单没有子菜单
3. 菜单树会被缓存，以提高查询性能
