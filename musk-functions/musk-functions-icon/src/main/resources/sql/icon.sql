-- 图标主表
CREATE TABLE `system_icon` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '图标ID',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `icon_name` varchar(100) NOT NULL COMMENT '图标名称',
  `icon_code` varchar(100) NOT NULL COMMENT '图标编码（唯一标识）',
  `category_id` int DEFAULT NULL COMMENT '分类ID',
  `description` varchar(255) DEFAULT NULL COMMENT '图标描述',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_domain_code` (`tenant_id`, `domain_id`, `icon_code`, `deleted`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_tenant_domain` (`tenant_id`, `domain_id`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图标主表';

-- 图标资源表
CREATE TABLE `system_icon_resource` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `icon_id` int NOT NULL COMMENT '图标ID',
  `resource_type` tinyint NOT NULL COMMENT '资源类型（1:URL 2:Base64 3:字体图标）',
  `resource_url` varchar(500) DEFAULT NULL COMMENT '资源URL',
  `width` int DEFAULT NULL COMMENT '宽度（像素）',
  `height` int DEFAULT NULL COMMENT '高度（像素）',
  `version` varchar(20) DEFAULT NULL COMMENT '版本号',
  `is_default` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否默认资源',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_icon_id` (`icon_id`),
  KEY `idx_tenant_domain` (`tenant_id`, `domain_id`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图标资源表';

-- 图标分类表
CREATE TABLE `system_icon_category` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `category_name` varchar(100) NOT NULL COMMENT '分类名称',
  `category_code` varchar(100) NOT NULL COMMENT '分类编码',
  `parent_id` int DEFAULT NULL COMMENT '父分类ID',
  `display_order` int DEFAULT 0 COMMENT '显示顺序',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_domain_code` (`tenant_id`, `domain_id`, `category_code`, `deleted`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_tenant_domain` (`tenant_id`, `domain_id`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图标分类表';
