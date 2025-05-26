-- 会员认证渠道表
CREATE TABLE member_auth_channel (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    member_id INT NOT NULL COMMENT '会员ID',
    channel_type VARCHAR(20) NOT NULL COMMENT '认证渠道类型：USERNAME,EMAIL,PHONE,WECHAT,QQ,ALIPAY,GITHUB,GOOGLE',
    channel_identifier VARCHAR(200) NOT NULL COMMENT '渠道标识：用户名/邮箱地址/手机号/第三方openId',
    channel_value VARCHAR(500) COMMENT '渠道值：加密后的密码/第三方用户信息JSON等',
    is_verified BIT DEFAULT 0 COMMENT '是否已验证',
    is_primary BIT DEFAULT 0 COMMENT '是否为主要认证方式',
    verified_time DATETIME COMMENT '验证时间',
    last_used_time DATETIME COMMENT '最后使用时间',
    extra_info JSON COMMENT '扩展信息：第三方用户信息、设备指纹等',
    tenant_id INT NOT NULL COMMENT '租户ID',
    domain_id INT NOT NULL COMMENT '域ID',
    creator VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT DEFAULT 0 COMMENT '是否删除',
    
    -- 唯一索引：同一租户域下，同一会员的同一渠道类型的同一标识只能有一条记录
    UNIQUE KEY uk_member_channel (member_id, channel_type, channel_identifier, tenant_id, domain_id),
    
    -- 索引：根据渠道类型和标识查询（用于登录时查找会员）
    INDEX idx_channel_identifier (channel_type, channel_identifier, tenant_id, domain_id),
    
    -- 索引：根据会员ID查询（用于查询会员的所有认证渠道）
    INDEX idx_member_id (member_id, tenant_id, domain_id),
    
    -- 索引：查询主要认证渠道
    INDEX idx_primary (member_id, is_primary, tenant_id, domain_id),
    
    -- 索引：租户域查询
    INDEX idx_tenant_domain (tenant_id, domain_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员认证渠道表';

-- 为Oracle、PostgreSQL、Kingbase、DB2、H2数据库创建序列
-- CREATE SEQUENCE member_auth_channel_seq START WITH 1 INCREMENT BY 1;
