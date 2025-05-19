-- 菜单表
CREATE TABLE `menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `domain` int NOT NULL COMMENT '所属域(1:APP)',
  `menu_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `menu_level` int NOT NULL COMMENT '菜单层级(0表示首层)',
  `parent_menu_id` bigint DEFAULT NULL COMMENT '父菜单ID(如果层级为0,那么父菜单ID为null)',
  `menu_relation_icon_id` int NOT NULL COMMENT '菜单关联ICON',
  `menu_icon_width` double NOT NULL COMMENT '菜单ICON宽度',
  `menu_icon_height` double NOT NULL COMMENT '菜单ICON高度',
  `menu_on_click_path` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单点击跳转路径',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '携带参数(''JSON格式'')',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE,
  KEY `idx_domain` (`domain`) USING BTREE,
  KEY `idx_parent_menu_id` (`parent_menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';
