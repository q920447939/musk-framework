-- 插入图标分类测试数据
INSERT INTO `system_icon_category`
(`id`, `tenant_id`, `domain_id`, `category_name`, `category_code`, `parent_id`, `display_order`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(1, 1, 1, '导航图标', 'navigation', NULL, 1, 0, '导航栏使用的图标', 'admin', NOW(), 'admin', NOW(), 0),
(2, 1, 1, '功能图标', 'function', NULL, 2, 0, '功能按钮使用的图标', 'admin', NOW(), 'admin', NOW(), 0),
(3, 1, 1, '状态图标', 'status', NULL, 3, 0, '表示状态的图标', 'admin', NOW(), 'admin', NOW(), 0),
(4, 1, 1, '通用图标', 'common', NULL, 4, 0, '通用图标集合', 'admin', NOW(), 'admin', NOW(), 0),
(5, 1, 1, '导航-首页', 'navigation-home', 1, 1, 0, '首页导航图标', 'admin', NOW(), 'admin', NOW(), 0),
(6, 1, 1, '导航-用户', 'navigation-user', 1, 2, 0, '用户中心导航图标', 'admin', NOW(), 'admin', NOW(), 0),
(7, 1, 1, '导航-消息', 'navigation-message', 1, 3, 0, '消息中心导航图标', 'admin', NOW(), 'admin', NOW(), 0),
(8, 1, 1, '导航-设置', 'navigation-setting', 1, 4, 0, '设置导航图标', 'admin', NOW(), 'admin', NOW(), 0),
(9, 1, 1, '功能-添加', 'function-add', 2, 1, 0, '添加功能图标', 'admin', NOW(), 'admin', NOW(), 0),
(10, 1, 1, '功能-编辑', 'function-edit', 2, 2, 0, '编辑功能图标', 'admin', NOW(), 'admin', NOW(), 0),
(11, 1, 1, '功能-删除', 'function-delete', 2, 3, 0, '删除功能图标', 'admin', NOW(), 'admin', NOW(), 0),
(12, 1, 1, '功能-搜索', 'function-search', 2, 4, 0, '搜索功能图标', 'admin', NOW(), 'admin', NOW(), 0),
(13, 1, 1, '状态-成功', 'status-success', 3, 1, 0, '成功状态图标', 'admin', NOW(), 'admin', NOW(), 0),
(14, 1, 1, '状态-失败', 'status-fail', 3, 2, 0, '失败状态图标', 'admin', NOW(), 'admin', NOW(), 0),
(15, 1, 1, '状态-警告', 'status-warning', 3, 3, 0, '警告状态图标', 'admin', NOW(), 'admin', NOW(), 0),
(16, 1, 1, '状态-信息', 'status-info', 3, 4, 0, '信息状态图标', 'admin', NOW(), 'admin', NOW(), 0);

-- 插入图标测试数据
INSERT INTO `system_icon`
(`id`, `tenant_id`, `domain_id`, `icon_name`, `icon_code`, `category_id`, `description`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(1, 1, 1, '首页图标', 'icon_home', 5, '首页导航图标', 0, '用于APP和WEB端首页导航', 'admin', NOW(), 'admin', NOW(), 0),
(2, 1, 1, '用户中心图标', 'icon_user', 6, '用户中心导航图标', 0, '用于APP和WEB端用户中心导航', 'admin', NOW(), 'admin', NOW(), 0),
(3, 1, 1, '消息中心图标', 'icon_message', 7, '消息中心导航图标', 0, '用于APP和WEB端消息中心导航', 'admin', NOW(), 'admin', NOW(), 0),
(4, 1, 1, '设置图标', 'icon_setting', 8, '设置导航图标', 0, '用于APP和WEB端设置导航', 'admin', NOW(), 'admin', NOW(), 0),
(5, 1, 1, '添加图标', 'icon_add', 9, '添加功能图标', 0, '用于添加功能按钮', 'admin', NOW(), 'admin', NOW(), 0),
(6, 1, 1, '编辑图标', 'icon_edit', 10, '编辑功能图标', 0, '用于编辑功能按钮', 'admin', NOW(), 'admin', NOW(), 0),
(7, 1, 1, '删除图标', 'icon_delete', 11, '删除功能图标', 0, '用于删除功能按钮', 'admin', NOW(), 'admin', NOW(), 0),
(8, 1, 1, '搜索图标', 'icon_search', 12, '搜索功能图标', 0, '用于搜索功能按钮', 'admin', NOW(), 'admin', NOW(), 0),
(9, 1, 1, '成功图标', 'icon_success', 13, '成功状态图标', 0, '表示操作成功的状态', 'admin', NOW(), 'admin', NOW(), 0),
(10, 1, 1, '失败图标', 'icon_fail', 14, '失败状态图标', 0, '表示操作失败的状态', 'admin', NOW(), 'admin', NOW(), 0),
(11, 1, 1, '警告图标', 'icon_warning', 15, '警告状态图标', 0, '表示警告的状态', 'admin', NOW(), 'admin', NOW(), 0),
(12, 1, 1, '信息图标', 'icon_info', 16, '信息状态图标', 0, '表示提示信息的状态', 'admin', NOW(), 'admin', NOW(), 0);

-- 插入图标资源测试数据
INSERT INTO `system_icon_resource`
(`id`, `tenant_id`, `domain_id`, `icon_id`, `resource_type`, `resource_url`, `width`, `height`, `version`, `is_default`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
-- 首页图标资源
(1, 1, 1, 1, 1, 'https://example.com/icons/app/home.png', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(2, 1, 2, 1, 1, 'https://example.com/icons/web/home.png', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(3, 1, 3, 1, 3, 'https://example.com/icons/common/home.svg', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 用户中心图标资源
(4, 1, 1, 2, 1, 'https://example.com/icons/app/user.png', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(5, 1, 2, 2, 1, 'https://example.com/icons/web/user.png', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(6, 1, 3, 2, 3, 'https://example.com/icons/common/user.svg', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 消息中心图标资源
(7, 1, 1, 3, 1, 'https://example.com/icons/app/message.png', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(8, 1, 2, 3, 1, 'https://example.com/icons/web/message.png', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(9, 1, 3, 3, 3, 'https://example.com/icons/common/message.svg', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 设置图标资源
(10, 1, 1, 4, 1, 'https://example.com/icons/app/setting.png', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(11, 1, 2, 4, 1, 'https://example.com/icons/web/setting.png', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(12, 1, 3, 4, 3, 'https://example.com/icons/common/setting.svg', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 添加图标资源
(13, 1, 1, 5, 1, 'https://example.com/icons/app/add.png', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(14, 1, 2, 5, 1, 'https://example.com/icons/web/add.png', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(15, 1, 3, 5, 3, 'https://example.com/icons/common/add.svg', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 编辑图标资源
(16, 1, 1, 6, 1, 'https://example.com/icons/app/edit.png', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(17, 1, 2, 6, 1, 'https://example.com/icons/web/edit.png', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(18, 1, 3, 6, 3, 'https://example.com/icons/common/edit.svg', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 删除图标资源
(19, 1, 1, 7, 1, 'https://example.com/icons/app/delete.png', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(20, 1, 2, 7, 1, 'https://example.com/icons/web/delete.png', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(21, 1, 3, 7, 3, 'https://example.com/icons/common/delete.svg', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 搜索图标资源
(22, 1, 1, 8, 1, 'https://example.com/icons/app/search.png', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(23, 1, 2, 8, 1, 'https://example.com/icons/web/search.png', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(24, 1, 3, 8, 3, 'https://example.com/icons/common/search.svg', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 成功图标资源
(25, 1, 1, 9, 2, 'https://example.com/icons/app/success.svg', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(26, 1, 2, 9, 2, 'https://example.com/icons/web/success.svg', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),

-- 失败图标资源
(27, 1, 1, 10, 2, 'https://example.com/icons/app/fail.svg', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(28, 1, 2, 10, 2, 'https://example.com/icons/web/fail.svg', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),

-- 警告图标资源
(29, 1, 1, 11, 2, 'https://example.com/icons/app/warning.svg', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(30, 1, 2, 11, 2, 'https://example.com/icons/web/warning.svg', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),

-- 信息图标资源
(31, 1, 1, 12, 2, 'https://example.com/icons/app/info.svg', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(32, 1, 2, 12, 2, 'https://example.com/icons/web/info.svg', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0);

-- 为第二个租户添加一些测试数据
INSERT INTO `system_icon_category`
(`id`, `tenant_id`, `domain_id`, `category_name`, `category_code`, `parent_id`, `display_order`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(101, 2, 1, '导航图标', 'navigation', NULL, 1, 0, '导航栏使用的图标', 'admin', NOW(), 'admin', NOW(), 0),
(102, 2, 1, '功能图标', 'function', NULL, 2, 0, '功能按钮使用的图标', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `system_icon`
(`id`, `tenant_id`, `domain_id`, `icon_name`, `icon_code`, `category_id`, `description`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(101, 2, 1, '首页图标', 'icon_home', 101, '首页导航图标', 0, '用于APP和WEB端首页导航', 'admin', NOW(), 'admin', NOW(), 0),
(102, 2, 1, '用户中心图标', 'icon_user', 101, '用户中心导航图标', 0, '用于APP和WEB端用户中心导航', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `system_icon_resource`
(`id`, `tenant_id`, `domain_id`, `icon_id`, `resource_type`, `resource_url`, `width`, `height`, `version`, `is_default`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(101, 2, 1, 101, 1, 'https://example.com/icons/app/home_tenant2.png', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(102, 2, 2, 101, 1, 'https://example.com/icons/web/home_tenant2.png', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(103, 2, 1, 102, 1, 'https://example.com/icons/app/user_tenant2.png', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(104, 2, 2, 102, 1, 'https://example.com/icons/web/user_tenant2.png', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0);
