-- 导航配置表
CREATE TABLE `app_navigation_config` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `navigator_name` varchar(255) DEFAULT NULL COMMENT '导航名称',
  `navigator_icon_id` int DEFAULT NULL COMMENT '导航图标ID',
  `display_index` smallint DEFAULT NULL COMMENT '显示顺序',
  `target_uri` varchar(255) DEFAULT NULL COMMENT '目标地址',
  `uri_params` varchar(255) DEFAULT NULL COMMENT '地址参数',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` int DEFAULT NULL COMMENT '状态（0正常 1停用）',
  `platform_type` int DEFAULT NULL COMMENT '平台类型（1:APP 2:WEB）',
  `navigator_level` int DEFAULT NULL COMMENT '导航层级(0表示首层)',
  `parent_navigator_id` int DEFAULT NULL COMMENT '父导航ID(如果层级为0,那么父导航ID为null)',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_package_id` (`deleted`,`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='导航配置表';

-- 添加权限资源类型
INSERT INTO `system_resource_type` (`code`, `description`, `create_time`, `update_time`, `creator`, `updater`, `deleted`)
VALUES ('NAVIGATION', '导航模块', NOW(), NOW(), 'system', 'system', 0);
