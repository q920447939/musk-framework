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
(`id`, `icon_id`, `platform_type`, `resource_type`, `resource_url`, `resource_content`, `width`, `height`, `version`, `is_default`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES
-- 首页图标资源
(1, 1, 1, 1, 'https://example.com/icons/app/home.png', NULL, 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(2, 1, 2, 1, 'https://example.com/icons/web/home.png', NULL, 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(3, 1, 3, 3, NULL, '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"/></svg>', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 用户中心图标资源
(4, 2, 1, 1, 'https://example.com/icons/app/user.png', NULL, 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(5, 2, 2, 1, 'https://example.com/icons/web/user.png', NULL, 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(6, 2, 3, 3, NULL, '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 消息中心图标资源
(7, 3, 1, 1, 'https://example.com/icons/app/message.png', NULL, 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(8, 3, 2, 1, 'https://example.com/icons/web/message.png', NULL, 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(9, 3, 3, 3, NULL, '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="M20 2H4c-1.1 0-1.99.9-1.99 2L2 22l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-2 12H6v-2h12v2zm0-3H6V9h12v2zm0-3H6V6h12v2z"/></svg>', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 设置图标资源
(10, 4, 1, 1, 'https://example.com/icons/app/setting.png', NULL, 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(11, 4, 2, 1, 'https://example.com/icons/web/setting.png', NULL, 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(12, 4, 3, 3, NULL, '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z"/></svg>', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 添加图标资源
(13, 5, 1, 1, 'https://example.com/icons/app/add.png', NULL, 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(14, 5, 2, 1, 'https://example.com/icons/web/add.png', NULL, 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(15, 5, 3, 3, NULL, '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/></svg>', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 编辑图标资源
(16, 6, 1, 1, 'https://example.com/icons/app/edit.png', NULL, 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(17, 6, 2, 1, 'https://example.com/icons/web/edit.png', NULL, 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(18, 6, 3, 3, NULL, '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/></svg>', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 删除图标资源
(19, 7, 1, 1, 'https://example.com/icons/app/delete.png', NULL, 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(20, 7, 2, 1, 'https://example.com/icons/web/delete.png', NULL, 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(21, 7, 3, 3, NULL, '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/></svg>', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 搜索图标资源
(22, 8, 1, 1, 'https://example.com/icons/app/search.png', NULL, 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(23, 8, 2, 1, 'https://example.com/icons/web/search.png', NULL, 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(24, 8, 3, 3, NULL, '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/></svg>', 24, 24, '1.0', 0, 'admin', NOW(), 'admin', NOW(), 0),

-- 成功图标资源
(25, 9, 1, 2, NULL, 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyNCIgaGVpZ2h0PSIyNCIgdmlld0JveD0iMCAwIDI0IDI0Ij48cGF0aCBkPSJNOSAxNi4yTDQuOCAxMmwtMS40IDEuNEw5IDE5IDIxIDdsLTEuNC0xLjRMOSAxNi4yeiIgZmlsbD0iIzRjYWY1MCIvPjwvc3ZnPg==', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(26, 9, 2, 2, NULL, 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIzMiIgaGVpZ2h0PSIzMiIgdmlld0JveD0iMCAwIDI0IDI0Ij48cGF0aCBkPSJNOSAxNi4yTDQuOCAxMmwtMS40IDEuNEw5IDE5IDIxIDdsLTEuNC0xLjRMOSAxNi4yeiIgZmlsbD0iIzRjYWY1MCIvPjwvc3ZnPg==', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),

-- 失败图标资源
(27, 10, 1, 2, NULL, 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyNCIgaGVpZ2h0PSIyNCIgdmlld0JveD0iMCAwIDI0IDI0Ij48cGF0aCBkPSJNMTkgNi40MUwxNy41OSA1IDEyIDEwLjU5IDYuNDEgNSA1IDYuNDEgMTAuNTkgMTIgNSAxNy41OSA2LjQxIDE5IDEyIDEzLjQxIDE3LjU5IDE5IDE5IDE3LjU5IDEzLjQxIDEyeiIgZmlsbD0iI2YzNjM2MyIvPjwvc3ZnPg==', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(28, 10, 2, 2, NULL, 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIzMiIgaGVpZ2h0PSIzMiIgdmlld0JveD0iMCAwIDI0IDI0Ij48cGF0aCBkPSJNMTkgNi40MUwxNy41OSA1IDEyIDEwLjU5IDYuNDEgNSA1IDYuNDEgMTAuNTkgMTIgNSAxNy41OSA2LjQxIDE5IDEyIDEzLjQxIDE3LjU5IDE5IDE5IDE3LjU5IDEzLjQxIDEyeiIgZmlsbD0iI2YzNjM2MyIvPjwvc3ZnPg==', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),

-- 警告图标资源
(29, 11, 1, 2, NULL, 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyNCIgaGVpZ2h0PSIyNCIgdmlld0JveD0iMCAwIDI0IDI0Ij48cGF0aCBkPSJNMSAyMWgyMkwxMiAyIDEgMjF6bTEyLTNoLTJ2LTJoMnYyem0wLTRoLTJ2LTRoMnY0eiIgZmlsbD0iI2ZmYzEwNyIvPjwvc3ZnPg==', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(30, 11, 2, 2, NULL, 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIzMiIgaGVpZ2h0PSIzMiIgdmlld0JveD0iMCAwIDI0IDI0Ij48cGF0aCBkPSJNMSAyMWgyMkwxMiAyIDEgMjF6bTEyLTNoLTJ2LTJoMnYyem0wLTRoLTJ2LTRoMnY0eiIgZmlsbD0iI2ZmYzEwNyIvPjwvc3ZnPg==', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),

-- 信息图标资源
(31, 12, 1, 2, NULL, 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyNCIgaGVpZ2h0PSIyNCIgdmlld0JveD0iMCAwIDI0IDI0Ij48cGF0aCBkPSJNMTIgMkM2LjQ4IDIgMiA2LjQ4IDIgMTJzNC40OCAxMCAxMCAxMCAxMC00LjQ4IDEwLTEwUzE3LjUyIDIgMTIgMnptMSAxNWgtMnYtNmgydjZ6bTAtOGgtMlY3aDJ2MnoiIGZpbGw9IiMyMTk2ZjMiLz48L3N2Zz4=', 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(32, 12, 2, 2, NULL, 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIzMiIgaGVpZ2h0PSIzMiIgdmlld0JveD0iMCAwIDI0IDI0Ij48cGF0aCBkPSJNMTIgMkM2LjQ4IDIgMiA2LjQ4IDIgMTJzNC40OCAxMCAxMCAxMCAxMC00LjQ4IDEwLTEwUzE3LjUyIDIgMTIgMnptMSAxNWgtMnYtNmgydjZ6bTAtOGgtMlY3aDJ2MnoiIGZpbGw9IiMyMTk2ZjMiLz48L3N2Zz4=', 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0);

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
(`id`, `icon_id`, `platform_type`, `resource_type`, `resource_url`, `resource_content`, `width`, `height`, `version`, `is_default`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES
(101, 101, 1, 1, 'https://example.com/icons/app/home_tenant2.png', NULL, 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(102, 101, 2, 1, 'https://example.com/icons/web/home_tenant2.png', NULL, 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(103, 102, 1, 1, 'https://example.com/icons/app/user_tenant2.png', NULL, 24, 24, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0),
(104, 102, 2, 1, 'https://example.com/icons/web/user_tenant2.png', NULL, 32, 32, '1.0', 1, 'admin', NOW(), 'admin', NOW(), 0);
