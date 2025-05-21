-- 会员等级定义表
CREATE TABLE `member_level_definition` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `level_code` varchar(50) NOT NULL COMMENT '等级编码',
  `level_name` varchar(100) NOT NULL COMMENT '等级名称',
  `level_icon_id` int DEFAULT NULL COMMENT '等级图标ID',
  `level_value` int NOT NULL COMMENT '等级值',
  `growth_value_threshold` int NOT NULL COMMENT '成长值门槛',
  `level_description` varchar(500) DEFAULT NULL COMMENT '等级描述',
  `level_color` varchar(20) DEFAULT NULL COMMENT '等级颜色(十六进制)',
  `display_index` int DEFAULT 0 COMMENT '显示顺序',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态(0:启用 1:禁用)',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_domain_level_code` (`tenant_id`, `domain_id`, `level_code`, `deleted`),
  KEY `idx_tenant_domain` (`tenant_id`, `domain_id`, `deleted`),
  KEY `idx_level_value` (`level_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='会员等级定义表';

-- 会员等级权益表
CREATE TABLE `member_level_benefit` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `level_id` int NOT NULL COMMENT '等级ID',
  `benefit_type` tinyint NOT NULL COMMENT '权益类型(1:折扣率 2:免邮次数 3:生日礼 4:专属客服 5:积分加速 6:自定义权益)',
  `benefit_name` varchar(100) NOT NULL COMMENT '权益名称',
  `benefit_value` varchar(255) DEFAULT NULL COMMENT '权益值',
  `benefit_icon_id` int DEFAULT NULL COMMENT '权益图标ID',
  `benefit_description` varchar(500) DEFAULT NULL COMMENT '权益描述',
  `display_index` int DEFAULT 0 COMMENT '显示顺序',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态(0:启用 1:禁用)',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_level_id` (`level_id`),
  KEY `idx_tenant_domain` (`tenant_id`, `domain_id`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='会员等级权益表';

-- 会员成长值表
CREATE TABLE `member_growth_value` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `member_id` int NOT NULL COMMENT '会员ID',
  `current_level_id` int NOT NULL COMMENT '当前等级ID',
  `total_growth_value` int NOT NULL DEFAULT 0 COMMENT '总成长值',
  `current_period_growth_value` int NOT NULL DEFAULT 0 COMMENT '当前周期成长值',
  `next_level_threshold` int DEFAULT NULL COMMENT '下一等级门槛',
  `period_start_time` datetime DEFAULT NULL COMMENT '周期开始时间',
  `period_end_time` datetime DEFAULT NULL COMMENT '周期结束时间',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_member_id` (`member_id`, `deleted`),
  KEY `idx_tenant_domain` (`tenant_id`, `domain_id`, `deleted`),
  KEY `idx_current_level_id` (`current_level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='会员成长值表';

-- 会员积分表
CREATE TABLE `member_points` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `member_id` int NOT NULL COMMENT '会员ID',
  `available_points` int NOT NULL DEFAULT 0 COMMENT '可用积分',
  `frozen_points` int NOT NULL DEFAULT 0 COMMENT '冻结积分',
  `total_points` int NOT NULL DEFAULT 0 COMMENT '总积分(累计获得)',
  `used_points` int NOT NULL DEFAULT 0 COMMENT '已使用积分',
  `expired_points` int NOT NULL DEFAULT 0 COMMENT '已过期积分',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_member_id` (`member_id`, `deleted`),
  KEY `idx_tenant_domain` (`tenant_id`, `domain_id`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='会员积分表';

-- 成长值变更记录表
CREATE TABLE `member_growth_value_record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `member_id` int NOT NULL COMMENT '会员ID',
  `change_type` tinyint NOT NULL COMMENT '变更类型(1:增加 2:减少)',
  `change_value` int NOT NULL COMMENT '变更值',
  `before_value` int NOT NULL COMMENT '变更前值',
  `after_value` int NOT NULL COMMENT '变更后值',
  `source_type` tinyint NOT NULL COMMENT '来源类型(1:消费 2:活动 3:签到 4:任务 5:管理员调整 6:其他)',
  `source_id` varchar(64) DEFAULT NULL COMMENT '来源ID',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `operator` varchar(64) DEFAULT NULL COMMENT '操作人',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_tenant_domain` (`tenant_id`, `domain_id`, `deleted`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成长值变更记录表';

-- 积分变更记录表
CREATE TABLE `member_points_record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `member_id` int NOT NULL COMMENT '会员ID',
  `change_type` tinyint NOT NULL COMMENT '变更类型(1:增加 2:减少 3:冻结 4:解冻 5:过期)',
  `change_value` int NOT NULL COMMENT '变更值',
  `before_value` int NOT NULL COMMENT '变更前值',
  `after_value` int NOT NULL COMMENT '变更后值',
  `source_type` tinyint NOT NULL COMMENT '来源类型(1:消费 2:活动 3:签到 4:任务 5:兑换 6:退款 7:管理员调整 8:其他)',
  `source_id` varchar(64) DEFAULT NULL COMMENT '来源ID',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `operator` varchar(64) DEFAULT NULL COMMENT '操作人',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_tenant_domain` (`tenant_id`, `domain_id`, `deleted`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='积分变更记录表';

-- 会员等级变更记录表
CREATE TABLE `member_level_change_record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `member_id` int NOT NULL COMMENT '会员ID',
  `old_level_id` int DEFAULT NULL COMMENT '旧等级ID',
  `new_level_id` int NOT NULL COMMENT '新等级ID',
  `change_type` tinyint NOT NULL COMMENT '变更类型(1:升级 2:降级 3:初始化)',
  `change_reason` varchar(255) DEFAULT NULL COMMENT '变更原因',
  `operator` varchar(64) DEFAULT NULL COMMENT '操作人',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_tenant_domain` (`tenant_id`, `domain_id`, `deleted`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='会员等级变更记录表';

-- 积分规则表
CREATE TABLE `member_points_rule` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `domain_id` int DEFAULT NULL COMMENT '域ID',
  `rule_code` varchar(50) NOT NULL COMMENT '规则编码',
  `rule_name` varchar(100) NOT NULL COMMENT '规则名称',
  `rule_type` tinyint NOT NULL COMMENT '规则类型(1:消费积分 2:活动积分 3:签到积分 4:任务积分 5:其他)',
  `points_value` int NOT NULL COMMENT '积分值',
  `growth_value` int DEFAULT 0 COMMENT '成长值',
  `rule_formula` varchar(255) DEFAULT NULL COMMENT '规则公式',
  `rule_description` varchar(500) DEFAULT NULL COMMENT '规则描述',
  `effective_start_time` datetime DEFAULT NULL COMMENT '生效开始时间',
  `effective_end_time` datetime DEFAULT NULL COMMENT '生效结束时间',
  `daily_limit` int DEFAULT NULL COMMENT '每日限制次数',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态(0:启用 1:禁用)',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_domain_rule_code` (`tenant_id`, `domain_id`, `rule_code`, `deleted`),
  KEY `idx_tenant_domain` (`tenant_id`, `domain_id`, `deleted`),
  KEY `idx_rule_type` (`rule_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='积分规则表';
