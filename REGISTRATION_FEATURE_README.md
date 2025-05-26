# 会员注册功能使用指南

## 功能概述

本模块实现了完整的会员注册功能，支持多种注册方式，采用统一的注册链架构，确保系统的可扩展性和一致性。

## 支持的注册方式

### 1. 用户名密码注册
- **接口地址**: `POST /api/auth/register/username`
- **认证类型**: `USERNAME_PASSWORD_REGISTER`
- **必需参数**:
  - `username`: 用户名（6-30位，字母数字下划线，不能以数字开头）
  - `password`: 密码（8-20位，支持密码强度配置）
  - `confirmPassword`: 确认密码
  - `captcha`: 图形验证码
  - `captchaSessionId`: 验证码会话ID
  - `registerChannel`: 注册渠道

### 2. 邮箱验证码注册
- **接口地址**: `POST /api/auth/register/email`
- **认证类型**: `EMAIL_CODE_REGISTER`
- **必需参数**:
  - `target`: 邮箱地址
  - `verificationCode`: 邮箱验证码
  - `registerChannel`: 注册渠道

### 3. 短信验证码注册
- **接口地址**: `POST /api/auth/register/sms`
- **认证类型**: `SMS_CODE_REGISTER`
- **必需参数**:
  - `target`: 手机号
  - `verificationCode`: 短信验证码
  - `registerChannel`: 注册渠道

## 辅助接口

### 图形验证码生成
- **接口地址**: `GET /api/auth/register/captcha/generate`
- **返回数据**:
  - `sessionId`: 会话ID
  - `imageBase64`: 图片Base64编码
  - `expireSeconds`: 过期时间

### 发送注册验证码
- **接口地址**: `POST /api/auth/register/code/send`
- **参数**:
  - `target`: 目标（邮箱或手机号）
  - `channel`: 渠道（EMAIL/SMS）

### 获取支持的注册类型
- **接口地址**: `GET /api/auth/register/types`
- **返回**: 支持的注册类型列表

## 开发环境配置

### 验证码便利功能

开发环境提供了多种验证码便利功能，方便快速测试：

#### 1. 固定验证码
```yaml
musk:
  auth:
    verification-code:
      email:
        dev-mode:
          enabled: true
          fixed-code: "123456"  # 固定邮箱验证码
      sms:
        dev-mode:
          enabled: true
          fixed-code: "123456"  # 固定短信验证码
      captcha:
        dev-mode:
          enabled: true
          fixed-code: "1234"    # 固定图形验证码
```

#### 2. 允许任意验证码
```yaml
musk:
  auth:
    verification-code:
      email:
        dev-mode:
          allow-any-code: true  # 允许任意邮箱验证码
      sms:
        dev-mode:
          allow-any-code: true  # 允许任意短信验证码
      captcha:
        dev-mode:
          allow-any-code: true  # 允许任意图形验证码
```

#### 3. 跳过验证码验证
```yaml
musk:
  auth:
    verification-code:
      email:
        dev-mode:
          skip-verification: true  # 跳过邮箱验证码验证
      sms:
        dev-mode:
          skip-verification: true  # 跳过短信验证码验证
      captcha:
        dev-mode:
          skip-verification: true  # 跳过图形验证码验证
```

### 密码策略配置

开发环境可以配置相对宽松的密码策略：

```yaml
musk:
  auth:
    password:
      min-length: 6                    # 最小长度
      require-uppercase: false         # 是否要求大写字母
      require-lowercase: false         # 是否要求小写字母
      require-digit: false             # 是否要求数字
      require-special-char: false      # 是否要求特殊字符
```

## 请求示例

### 用户名密码注册
```json
{
  "username": "testuser123",
  "password": "password123",
  "confirmPassword": "password123",
  "captcha": "1234",
  "captchaSessionId": "uuid-session-id",
  "registerChannel": "WEB",
  "nickname": "测试用户"
}
```

### 邮箱验证码注册
```json
{
  "target": "test@example.com",
  "verificationCode": "123456",
  "registerChannel": "WEB",
  "nickname": "邮箱用户"
}
```

### 短信验证码注册
```json
{
  "target": "13800138000",
  "verificationCode": "123456",
  "registerChannel": "APP",
  "nickname": "手机用户"
}
```

## 响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "memberId": 12345,
    "memberCode": "testuser123_1703123456789",
    "memberNickName": "测试用户",
    "avatar": "https://example.com/default-avatar.png",
    "registerTime": "2023-12-21T10:30:45",
    "autoLogin": false,
    "registerChannel": "用户名密码注册",
    "message": "注册成功"
  }
}
```

### 错误响应
```json
{
  "code": 41200025,
  "message": "图形验证码验证失败",
  "data": null
}
```

## 架构设计

### 核心组件

1. **统一注册链** (`UnifiedRegistrationChain`)
   - 负责注册请求的分发和处理
   - 自动注入和管理注册策略

2. **注册策略** (`RegistrationStrategy`)
   - 用户名密码注册策略
   - 邮箱验证码注册策略
   - 短信验证码注册策略

3. **验证码服务** (`CaptchaService`, `VerificationCodeService`)
   - 图形验证码生成和验证
   - 邮箱/短信验证码发送和验证
   - 开发环境便利功能

4. **工具类**
   - 密码强度校验 (`PasswordValidator`)
   - 注册渠道校验 (`RegisterChannelValidator`)

### 扩展性

系统采用策略模式设计，可以轻松添加新的注册方式：

1. 实现 `RegistrationStrategy` 接口
2. 添加对应的 `AuthTypeEnum` 枚举值
3. 创建相应的请求DTO
4. 系统会自动注册和使用新策略

## 安全特性

1. **密码安全**
   - 密码强度校验
   - AES加密存储
   - 防暴力破解

2. **验证码安全**
   - 一次性使用
   - 过期时间控制
   - 频率限制

3. **注册安全**
   - 渠道唯一性检查
   - IP频率限制
   - 参数严格校验

## 监控和日志

系统提供完整的日志记录和监控：

- 注册成功/失败统计
- 各渠道注册量统计
- 异常注册行为监控
- 详细的操作日志

## 注意事项

1. 开发环境的便利功能仅在 `spring.profiles.active=dev` 时生效
2. 生产环境建议关闭所有开发模式配置
3. 验证码发送需要配置相应的邮件/短信服务
4. 建议定期更新密码策略以提高安全性
