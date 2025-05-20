-- 创建测试数据库
CREATE DATABASE IF NOT EXISTS musk_test DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用测试数据库
USE musk_test;

-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS system_message_send_record;
DROP TABLE IF EXISTS system_user_message;
DROP TABLE IF EXISTS system_message;
DROP TABLE IF EXISTS system_message_template;

-- 创建消息主表
CREATE TABLE IF NOT EXISTS `system_message` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `message_type` tinyint NOT NULL COMMENT '消息类型(1:文本消息 2:图文消息 3:系统通知 4:更新通知)',
  `title` varchar(255) NOT NULL COMMENT '消息标题',
  `content` text COMMENT '消息内容',
  `image_url` varchar(255) DEFAULT NULL COMMENT '图片URL(图文消息)',
  `priority` tinyint DEFAULT '0' COMMENT '优先级(0:普通 1:重要 2:紧急)',
  `is_forced` bit(1) DEFAULT b'0' COMMENT '是否强制消息',
  `action_type` tinyint DEFAULT '0' COMMENT '操作类型(0:无操作 1:跳转链接 2:打开应用 3:下载更新)',
  `action_url` varchar(255) DEFAULT NULL COMMENT '操作URL',
  `action_params` varchar(255) DEFAULT NULL COMMENT '操作参数(JSON格式)',
  `start_time` datetime DEFAULT NULL COMMENT '生效开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '生效结束时间',
  `status` tinyint DEFAULT '0' COMMENT '状态(0:草稿 1:已发布 2:已过期)',
  `platform_type` tinyint DEFAULT NULL COMMENT '平台类型(1:APP 2:WEB 3:全平台)',
  `template_id` int DEFAULT NULL COMMENT '消息模板ID',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_domain` (`deleted`,`tenant_id`,`domain_id`),
  KEY `idx_status_time` (`status`,`start_time`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息主表';

-- 用户消息关联表
CREATE TABLE IF NOT EXISTS `system_user_message` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `message_id` int NOT NULL COMMENT '消息ID',
  `is_read` bit(1) DEFAULT b'0' COMMENT '是否已读',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `is_deleted_by_user` bit(1) DEFAULT b'0' COMMENT '用户是否删除',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_message` (`user_id`,`message_id`),
  KEY `idx_user_read` (`user_id`,`is_read`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户消息关联表';

-- 消息模板表
CREATE TABLE IF NOT EXISTS `system_message_template` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `template_code` varchar(64) NOT NULL COMMENT '模板编码',
  `template_name` varchar(128) NOT NULL COMMENT '模板名称',
  `template_type` tinyint NOT NULL COMMENT '模板类型(1:文本消息 2:图文消息 3:系统通知 4:更新通知)',
  `title_template` varchar(255) NOT NULL COMMENT '标题模板',
  `content_template` text NOT NULL COMMENT '内容模板',
  `image_url` varchar(255) DEFAULT NULL COMMENT '图片URL(图文消息)',
  `priority` tinyint DEFAULT '0' COMMENT '优先级(0:普通 1:重要 2:紧急)',
  `is_forced` bit(1) DEFAULT b'0' COMMENT '是否强制消息',
  `action_type` tinyint DEFAULT '0' COMMENT '操作类型(0:无操作 1:跳转链接 2:打开应用 3:下载更新)',
  `action_url` varchar(255) DEFAULT NULL COMMENT '操作URL',
  `action_params` varchar(255) DEFAULT NULL COMMENT '操作参数(JSON格式)',
  `platform_type` tinyint DEFAULT NULL COMMENT '平台类型(1:APP 2:WEB 3:全平台)',
  `status` tinyint DEFAULT '1' COMMENT '状态(0:禁用 1:启用)',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_template_code` (`tenant_id`,`domain_id`,`template_code`,`deleted`),
  KEY `idx_tenant_domain` (`deleted`,`tenant_id`,`domain_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息模板表';

-- 消息发送记录表
CREATE TABLE IF NOT EXISTS `system_message_send_record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `message_id` int NOT NULL COMMENT '消息ID',
  `target_type` tinyint NOT NULL COMMENT '目标类型(1:单个用户 2:用户组 3:全部用户)',
  `target_id` varchar(255) DEFAULT NULL COMMENT '目标ID(用户ID或用户组ID)',
  `send_time` datetime NOT NULL COMMENT '发送时间',
  `send_status` tinyint NOT NULL COMMENT '发送状态(0:待发送 1:发送中 2:发送成功 3:发送失败)',
  `error_message` varchar(512) DEFAULT NULL COMMENT '错误信息',
  `retry_count` int DEFAULT '0' COMMENT '重试次数',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_message_id` (`message_id`),
  KEY `idx_send_status` (`send_status`,`send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息发送记录表';

-- 插入测试消息模板
INSERT INTO `system_message_template` (
    `tenant_id`, `domain_id`, `template_code`, `template_name`, `template_type`, 
    `title_template`, `content_template`, `image_url`, `priority`, `is_forced`, 
    `action_type`, `action_url`, `action_params`, `platform_type`, `status`
) VALUES
-- 系统通知模板
(1, 1, 'system_welcome', '系统欢迎通知', 3, 
 '欢迎使用系统', '尊敬的${userName}，欢迎使用我们的系统！', NULL, 0, b'0', 
 0, NULL, NULL, 3, 1),

-- 更新通知模板
(1, 1, 'app_update', '应用更新通知', 4, 
 '新版本${version}发布', '尊敬的用户，我们的应用已更新到${version}版本，新版本包含以下改进：\n${updateContent}', NULL, 1, b'0', 
 3, 'https://example.com/download', '{"version":"${version}"}', 1, 1),

-- 图文消息模板
(1, 1, 'promotion_notice', '促销活动通知', 2, 
 '${activityName}活动开始啦', '${activityDesc}\n活动时间：${startTime}至${endTime}', 'https://example.com/images/promotion.jpg', 1, b'0', 
 1, 'https://example.com/activity/${activityId}', '{"activityId":"${activityId}"}', 3, 1),

-- 文本消息模板
(1, 1, 'payment_success', '支付成功通知', 1, 
 '支付成功通知', '尊敬的${userName}，您已成功支付${amount}元。订单号：${orderNo}', NULL, 0, b'0', 
 1, 'https://example.com/order/${orderNo}', '{"orderNo":"${orderNo}"}', 3, 1);

-- 插入测试消息
INSERT INTO `system_message` (
    `tenant_id`, `domain_id`, `message_type`, `title`, `content`, 
    `image_url`, `priority`, `is_forced`, `action_type`, `action_url`, 
    `action_params`, `start_time`, `end_time`, `status`, `platform_type`, `template_id`
) VALUES
-- 系统通知
(1, 1, 3, '系统维护通知', '尊敬的用户，系统将于2024-08-15 23:00-24:00进行维护升级，届时系统将暂停服务，请提前做好准备。', 
 NULL, 2, b'1', 0, NULL, 
 NULL, '2024-08-10 00:00:00', '2024-08-15 23:00:00', 1, 3, NULL),

-- 图文消息
(1, 1, 2, '夏季促销活动', '夏季大促销，全场商品5折起！\n活动时间：2024-08-01至2024-08-31', 
 'https://example.com/images/summer_sale.jpg', 1, b'0', 1, 'https://example.com/activity/summer2024', 
 '{"activityId":"summer2024"}', '2024-08-01 00:00:00', '2024-08-31 23:59:59', 1, 3, NULL),

-- 更新通知
(1, 1, 4, '应用版本2.0发布', '我们的应用已更新到2.0版本，新版本包含以下改进：\n1. 全新UI设计\n2. 性能优化\n3. 新增功能', 
 NULL, 1, b'1', 3, 'https://example.com/download', 
 '{"version":"2.0"}', '2024-08-01 00:00:00', NULL, 1, 1, NULL),

-- 文本消息
(1, 1, 1, '账户安全提醒', '为了保障您的账户安全，请定期修改密码并开启双因素认证。', 
 NULL, 0, b'0', 0, NULL, 
 NULL, '2024-08-01 00:00:00', NULL, 1, 3, NULL),

-- 草稿消息
(1, 1, 1, '新功能预告', '我们即将推出全新的功能，敬请期待！', 
 NULL, 0, b'0', 0, NULL, 
 NULL, NULL, NULL, 0, 3, NULL);

-- 插入测试用户消息关联
INSERT INTO `system_user_message` (
    `tenant_id`, `domain_id`, `user_id`, `message_id`, 
    `is_read`, `read_time`, `is_deleted_by_user`
) VALUES
-- 用户1的消息
(1, 1, 1001, 1, b'1', '2024-08-10 10:30:00', b'0'),
(1, 1, 1001, 2, b'0', NULL, b'0'),
(1, 1, 1001, 3, b'0', NULL, b'0'),
(1, 1, 1001, 4, b'1', '2024-08-11 15:45:00', b'0'),

-- 用户2的消息
(1, 1, 1002, 1, b'1', '2024-08-10 11:20:00', b'0'),
(1, 1, 1002, 2, b'1', '2024-08-10 14:15:00', b'0'),
(1, 1, 1002, 3, b'0', NULL, b'0'),
(1, 1, 1002, 4, b'0', NULL, b'1');

-- 插入测试消息发送记录
INSERT INTO `system_message_send_record` (
    `tenant_id`, `domain_id`, `message_id`, `target_type`, 
    `target_id`, `send_time`, `send_status`, `error_message`, `retry_count`
) VALUES
-- 发送给单个用户
(1, 1, 1, 1, '1001', '2024-08-10 10:00:00', 2, NULL, 0),
(1, 1, 2, 1, '1001', '2024-08-10 10:05:00', 2, NULL, 0),
(1, 1, 3, 1, '1001', '2024-08-10 10:10:00', 2, NULL, 0),
(1, 1, 4, 1, '1001', '2024-08-10 10:15:00', 2, NULL, 0),

-- 发送给用户2
(1, 1, 1, 1, '1002', '2024-08-10 10:00:00', 2, NULL, 0),
(1, 1, 2, 1, '1002', '2024-08-10 10:05:00', 2, NULL, 0),
(1, 1, 3, 1, '1002', '2024-08-10 10:10:00', 2, NULL, 0),
(1, 1, 4, 1, '1002', '2024-08-10 10:15:00', 2, NULL, 0),

-- 发送给所有用户
(1, 1, 1, 3, NULL, '2024-08-10 09:00:00', 2, NULL, 0);
