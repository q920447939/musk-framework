# 会员等级模块 (musk-functions-member-level)

## 简介

会员等级模块是一个通用的会员等级管理功能模块，支持多租户、多域的会员等级系统。可以用于不同平台（如APP、WEB）的会员等级管理，为各个系统/模块提供统一的会员等级管理和维护功能。

## 主要功能

1. 会员等级的增删改查
2. 会员等级权益管理
3. 会员积分和成长值管理
4. 会员等级升降级管理
5. 积分规则管理
6. 会员等级变更历史记录
7. 积分和成长值变更历史记录
8. 基于租户和域的会员等级隔离
9. 支持多平台（APP、WEB等）

## 技术特点

1. 基于 MyBatis-Plus 实现数据访问
2. 使用 Redis 缓存会员等级数据，提高查询性能
3. 支持多租户隔离
4. 支持多域隔离
5. 支持多平台配置
6. 基于事件驱动的会员等级变更通知

## 数据库设计

### 会员等级定义表 (member_level_definition)

- `id`: 等级ID
- `tenant_id`: 租户ID
- `domain_id`: 域ID
- `level_code`: 等级编码
- `level_name`: 等级名称
- `level_icon_id`: 等级图标ID
- `level_value`: 等级值
- `growth_value_threshold`: 成长值门槛
- `level_description`: 等级描述
- `level_color`: 等级颜色(十六进制)
- `display_index`: 显示顺序
- `status`: 状态(0:启用 1:禁用)
- `remark`: 备注

### 会员等级权益表 (member_level_benefit)

- `id`: 权益ID
- `tenant_id`: 租户ID
- `domain_id`: 域ID
- `level_id`: 等级ID
- `benefit_type`: 权益类型(1:折扣率 2:免邮次数 3:生日礼 4:专属客服 5:积分加速 6:自定义权益)
- `benefit_name`: 权益名称
- `benefit_value`: 权益值
- `benefit_icon_id`: 权益图标ID
- `benefit_description`: 权益描述
- `display_index`: 显示顺序
- `status`: 状态(0:启用 1:禁用)

### 会员成长值表 (member_growth_value)

- `id`: 成长值ID
- `tenant_id`: 租户ID
- `domain_id`: 域ID
- `member_id`: 会员ID
- `current_level_id`: 当前等级ID
- `total_growth_value`: 总成长值
- `current_period_growth_value`: 当前周期成长值
- `next_level_threshold`: 下一等级门槛
- `period_start_time`: 周期开始时间
- `period_end_time`: 周期结束时间

### 会员积分表 (member_points)

- `id`: 积分ID
- `tenant_id`: 租户ID
- `domain_id`: 域ID
- `member_id`: 会员ID
- `available_points`: 可用积分
- `frozen_points`: 冻结积分
- `total_points`: 总积分(累计获得)
- `used_points`: 已使用积分
- `expired_points`: 已过期积分

### 成长值变更记录表 (member_growth_value_record)

- `id`: 记录ID
- `tenant_id`: 租户ID
- `domain_id`: 域ID
- `member_id`: 会员ID
- `change_type`: 变更类型(1:增加 2:减少)
- `change_value`: 变更值
- `before_value`: 变更前值
- `after_value`: 变更后值
- `source_type`: 来源类型(1:消费 2:活动 3:签到 4:任务 5:管理员调整 6:其他)
- `source_id`: 来源ID
- `description`: 描述
- `operator`: 操作人

### 积分变更记录表 (member_points_record)

- `id`: 记录ID
- `tenant_id`: 租户ID
- `domain_id`: 域ID
- `member_id`: 会员ID
- `change_type`: 变更类型(1:增加 2:减少 3:冻结 4:解冻 5:过期)
- `change_value`: 变更值
- `before_value`: 变更前值
- `after_value`: 变更后值
- `source_type`: 来源类型(1:消费 2:活动 3:签到 4:任务 5:兑换 6:退款 7:管理员调整 8:其他)
- `source_id`: 来源ID
- `description`: 描述
- `expire_time`: 过期时间
- `operator`: 操作人

### 会员等级变更记录表 (member_level_change_record)

- `id`: 记录ID
- `tenant_id`: 租户ID
- `domain_id`: 域ID
- `member_id`: 会员ID
- `old_level_id`: 旧等级ID
- `new_level_id`: 新等级ID
- `change_type`: 变更类型(1:升级 2:降级 3:初始化)
- `change_reason`: 变更原因
- `operator`: 操作人

### 积分规则表 (member_points_rule)

- `id`: 规则ID
- `tenant_id`: 租户ID
- `domain_id`: 域ID
- `rule_code`: 规则编码
- `rule_name`: 规则名称
- `rule_type`: 规则类型(1:消费积分 2:活动积分 3:签到积分 4:任务积分 5:其他)
- `points_value`: 积分值
- `growth_value`: 成长值
- `rule_formula`: 规则公式
- `rule_description`: 规则描述
- `effective_start_time`: 生效开始时间
- `effective_end_time`: 生效结束时间
- `daily_limit`: 每日限制次数
- `status`: 状态(0:启用 1:禁用)

## 使用示例

```java
@Resource
private MemberLevelService memberLevelService;

@Resource
private MemberPointsService memberPointsService;

// 获取会员当前等级
MemberLevelInfoVO levelInfo = memberLevelService.getMemberCurrentLevel(memberId);

// 增加会员积分
memberPointsService.addPoints(memberId, 100, PointsSourceTypeEnum.CONSUMPTION,
    "ORDER123456", "购物奖励积分", "system");

// 增加会员成长值
memberGrowthValueService.addGrowthValue(memberId, 10, GrowthValueSourceTypeEnum.CONSUMPTION,
    "ORDER123456", "购物奖励成长值", "system");
```

## 注意事项

1. 会员等级编码在同一租户和域下必须唯一
2. 删除会员等级前，需要确保该等级没有关联的会员
3. 会员等级数据会被缓存，以提高查询性能
4. 积分和成长值的变更会触发相应的事件，可以通过监听这些事件来实现自定义业务逻辑
