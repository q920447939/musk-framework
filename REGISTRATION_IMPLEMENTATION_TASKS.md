# 会员注册功能实现任务清单

## 第一阶段：核心枚举和基础结构

### 1.1 扩展认证类型枚举
- [x] 扩展 `AuthTypeEnum`，添加注册相关类型
  - USERNAME_PASSWORD_REGISTER(11, "用户名密码注册")
  - EMAIL_CODE_REGISTER(12, "邮箱验证码注册")
  - SMS_CODE_REGISTER(13, "短信验证码注册")

### 1.2 创建注册请求模型
- [x] 创建 `BaseRegistrationRequest` 基础注册请求类
- [x] 创建 `UsernamePasswordRegisterRequest` 用户名密码注册请求
- [x] 创建 `VerificationCodeRegisterRequest` 验证码注册请求

### 1.3 创建注册响应模型
- [x] 创建 `RegistrationResult` 注册结果类
- [x] 创建 `RegisterResponseDTO` 注册响应DTO

## 第二阶段：注册策略和服务层

### 2.1 注册策略接口和实现
- [x] 创建 `RegistrationStrategy` 注册策略接口
- [x] 创建 `UsernamePasswordRegisterStrategy` 用户名密码注册策略
- [x] 创建 `EmailCodeRegisterStrategy` 邮箱验证码注册策略
- [x] 创建 `SmsCodeRegisterStrategy` 短信验证码注册策略

### 2.2 统一注册链
- [x] 创建 `UnifiedRegistrationChain` 统一注册链服务
- [x] 实现注册策略的自动注入和映射

### 2.3 验证码服务增强
- [x] 扩展 `VerificationCodeService` 支持开发环境配置
- [x] 创建 `CaptchaService` 图形验证码服务
- [x] 实现开发环境验证码便利功能

## 第三阶段：控制器层

### 3.1 注册控制器
- [x] 创建 `MemberRegistrationController` 注册控制器
- [x] 实现用户名密码注册接口 `/register/username`
- [x] 实现邮箱验证码注册接口 `/register/email`
- [x] 实现短信验证码注册接口 `/register/sms`
- [x] 实现图形验证码获取接口 `/captcha/generate`

## 第四阶段：配置和工具类

### 4.1 配置类扩展
- [x] 扩展 `AuthenticationConfig` 添加注册相关配置
- [x] 添加开发环境验证码配置
- [x] 添加图形验证码配置

### 4.2 工具类和辅助服务
- [x] 创建 `PasswordValidator` 密码强度校验工具
- [x] 创建 `RegisterChannelValidator` 注册渠道校验工具
- [x] 扩展 `MemberHelper` 添加注册相关辅助方法（已有getDefaultAvatar方法）

## 第五阶段：安全和监控

### 5.1 安全防护
- [ ] 实现注册频率限制
- [ ] 实现IP防护机制
- [ ] 实现用户名/邮箱/手机号唯一性校验

### 5.2 日志和监控
- [ ] 添加注册相关日志记录
- [ ] 实现注册统计和监控

## 第六阶段：配置文件和文档

### 6.1 配置文件
- [x] 更新 `application-auth.yaml` 添加注册配置
- [x] 创建开发环境配置示例

### 6.2 异常处理
- [x] 扩展 `BusinessPageExceptionEnum` 添加注册相关异常
- [x] 实现统一异常处理

## 实施顺序

1. **第一阶段**：建立基础数据结构和枚举
2. **第二阶段**：实现核心业务逻辑和策略
3. **第三阶段**：实现API接口层
4. **第四阶段**：完善配置和工具支持
5. **第五阶段**：添加安全防护和监控
6. **第六阶段**：完善配置和异常处理

## 关键设计原则

- 复用现有认证基础设施
- 保持与登录功能架构一致性
- 支持开发环境便利功能
- 确保多租户隔离
- 实现完善的安全防护
- 提供良好的扩展性

## 技术要点

- 使用策略模式实现多种注册方式
- 基于MyBatis-Plus进行数据操作
- 利用ThreadLocalTenantContext进行租户隔离
- 使用JSR 303/380进行参数校验
- 采用统一的异常处理机制
- 实现开发环境友好的验证码策略
