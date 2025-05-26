-- 会员表字段调整
-- 注意：这个脚本会删除现有的密码字段，请在执行前做好数据备份和迁移

-- 1. 添加新字段
ALTER TABLE member ADD COLUMN last_login_time DATETIME COMMENT '最后登录时间';
ALTER TABLE member ADD COLUMN last_login_ip VARCHAR(50) COMMENT '最后登录IP';

-- 2. 如果需要保留现有密码数据，请先执行数据迁移
-- 将现有的用户名密码数据迁移到member_auth_channel表
INSERT INTO member_auth_channel (
    member_id, 
    channel_type, 
    channel_identifier, 
    channel_value, 
    is_verified, 
    is_primary, 
    verified_time,
    tenant_id, 
    domain_id, 
    creator, 
    create_time, 
    updater, 
    update_time
)
SELECT 
    id as member_id,
    'USERNAME' as channel_type,
    member_code as channel_identifier,
    password as channel_value,
    1 as is_verified,
    1 as is_primary,
    create_time as verified_time,
    tenant_id,
    domain_id,
    creator,
    create_time,
    updater,
    update_time
FROM member 
WHERE password IS NOT NULL 
  AND password != '' 
  AND deleted = 0;

-- 3. 删除不再需要的字段（请谨慎执行）
-- ALTER TABLE member DROP COLUMN password;

-- 4. 修改member_code字段注释
ALTER TABLE member MODIFY COLUMN member_code VARCHAR(50) NOT NULL COMMENT '会员编码（系统生成，用于内部标识）';

-- 5. 添加索引优化
ALTER TABLE member ADD INDEX idx_last_login (last_login_time);
ALTER TABLE member ADD INDEX idx_member_code (member_code, tenant_id, domain_id);

-- 6. 更新表注释
ALTER TABLE member COMMENT = '会员基础信息表（认证信息已迁移到member_auth_channel表）';
