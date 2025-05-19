-- 创建领域权限表
CREATE TABLE domain_permission (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
  tenant_id INT NOT NULL COMMENT '租户ID',
  domain_id INT NOT NULL COMMENT '领域ID',
  resource_type VARCHAR(50) NOT NULL COMMENT '资源类型，如MENU、SYSTEM_PARAMS等',
  resource_id VARCHAR(100) COMMENT '资源ID，可以为空表示整个资源类型',
  operation_type VARCHAR(20) NOT NULL COMMENT '操作类型，如CREATE、READ、UPDATE、DELETE',
  is_allowed TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否允许，1表示允许，0表示不允许',
  description VARCHAR(255) COMMENT '权限描述',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  creator VARCHAR(64) COMMENT '创建者',
  updater VARCHAR(64) COMMENT '更新者',
  deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_permission (tenant_id, domain_id, resource_type, resource_id, operation_type)
) COMMENT='领域权限表';

-- 插入示例数据

-- 租户1，领域A的权限（完全权限）
INSERT INTO domain_permission (tenant_id, domain_id, resource_type, resource_id, operation_type, is_allowed, description)
VALUES (1, 1, 'MENU', NULL, 'CREATE', 1, '领域A可以创建任何菜单');

INSERT INTO domain_permission (tenant_id, domain_id, resource_type, resource_id, operation_type, is_allowed, description)
VALUES (1, 1, 'MENU', NULL, 'READ', 1, '领域A可以查看任何菜单');

INSERT INTO domain_permission (tenant_id, domain_id, resource_type, resource_id, operation_type, is_allowed, description)
VALUES (1, 1, 'MENU', NULL, 'UPDATE', 1, '领域A可以更新任何菜单');

INSERT INTO domain_permission (tenant_id, domain_id, resource_type, resource_id, operation_type, is_allowed, description)
VALUES (1, 1, 'MENU', NULL, 'DELETE', 1, '领域A可以删除任何菜单');

-- 租户1，领域B的权限（只读权限）
INSERT INTO domain_permission (tenant_id, domain_id, resource_type, resource_id, operation_type, is_allowed, description)
VALUES (1, 2, 'MENU', NULL, 'READ', 1, '领域B只能查看菜单');

INSERT INTO domain_permission (tenant_id, domain_id, resource_type, resource_id, operation_type, is_allowed, description)
VALUES (1, 2, 'MENU', NULL, 'CREATE', 0, '领域B不能创建菜单');

INSERT INTO domain_permission (tenant_id, domain_id, resource_type, resource_id, operation_type, is_allowed, description)
VALUES (1, 2, 'MENU', NULL, 'UPDATE', 0, '领域B不能更新菜单');

INSERT INTO domain_permission (tenant_id, domain_id, resource_type, resource_id, operation_type, is_allowed, description)
VALUES (1, 2, 'MENU', NULL, 'DELETE', 0, '领域B不能删除菜单');

-- 系统参数权限示例
INSERT INTO domain_permission (tenant_id, domain_id, resource_type, resource_id, operation_type, is_allowed, description)
VALUES (1, 1, 'SYSTEM_PARAMS', NULL, 'READ', 1, '领域A可以查看任何系统参数');

INSERT INTO domain_permission (tenant_id, domain_id, resource_type, resource_id, operation_type, is_allowed, description)
VALUES (1, 1, 'SYSTEM_PARAMS', NULL, 'UPDATE', 1, '领域A可以更新任何系统参数');

INSERT INTO domain_permission (tenant_id, domain_id, resource_type, resource_id, operation_type, is_allowed, description)
VALUES (1, 2, 'SYSTEM_PARAMS', NULL, 'READ', 1, '领域B可以查看任何系统参数');

INSERT INTO domain_permission (tenant_id, domain_id, resource_type, resource_id, operation_type, is_allowed, description)
VALUES (1, 2, 'SYSTEM_PARAMS', 'app_log_url', 'UPDATE', 0, '领域B不能更新应用日志URL');

-- 特定资源权限示例
INSERT INTO domain_permission (tenant_id, domain_id, resource_type, resource_id, operation_type, is_allowed, description)
VALUES (1, 2, 'MENU', '1001', 'UPDATE', 1, '领域B可以更新ID为1001的菜单');
