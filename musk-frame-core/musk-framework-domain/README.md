# 域框架

## 简介

域框架用于在多租户系统中进一步细分数据隔离，支持在同一租户下区分不同的应用（如前台系统和后台系统）。

## 功能特性

- 基于 MyBatis Plus 的多租户功能，实现 DB 层面的多域隔离
- 自动为所有查询添加 domain_id 条件
- 支持配置忽略域隔离的表
- 提供域切换工具，方便在后台系统中跨域操作

## 使用方法

### 1. 配置域参数

在 `application.yml` 或 `application.properties` 中添加域相关配置：

```yaml
musk:
  framework:
    domain:
      # 是否启用域隔离，默认为true
      enable: true
      # 需要忽略域隔离的表
      ignore-tables:
        - system_config
        - system_param
      # 域ID列名，默认为domain_id
      domain-id-column: domain_id
      # 配置的默认域ID
      config-default-domain-id: 1
```

### 2. 确保表结构包含域ID字段

所有需要域隔离的表都应该包含 `domain_id` 字段（或自定义的域ID列名）。

### 3. 域拦截器的工作方式

系统提供了 `DomainInterceptor` 拦截器，用于获取域ID并设置到线程上下文中。拦截器按以下优先级获取域ID：

1. **本地配置的默认域ID**：通过 `musk.framework.domain.config-default-domain-id` 配置，适用于客户端应用
2. **请求头中的域ID**：通过 `X-domainId` 请求头传递，适用于管理端应用
3. **会员信息中的域ID**：从当前登录会员信息中获取，作为兜底方案

这种设计使得：
- 客户端应用（如前台系统）可以使用固定的域ID
- 管理端应用可以通过请求头切换不同的域

拦截器实现示例：

```java
@Order(15) // 在租户拦截器之后，认证拦截器之前
@Component
public class DomainInterceptor implements HandlerInterceptor {
    @Resource
    private MemberService memberService;

    @Resource
    private DomainConfig domainConfig;

    private static final String DOMAIN_ID_HEADER = "X-domainId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 优先使用本地配置的默认域ID（客户端）
        Integer domainId = domainConfig.getConfigDefaultDomainId();
        if (domainId != null) {
            ThreadLocalTenantContext.setDomainId(domainId);
            return true;
        }

        // 2. 尝试从请求头中获取域ID（管理端）
        String headerDomainId = request.getHeader(DOMAIN_ID_HEADER);
        if (headerDomainId != null) {
            ThreadLocalTenantContext.setDomainId(Integer.parseInt(headerDomainId));
            return true;
        }

        // 3. 兜底：从会员信息中获取域ID
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        if (memberId != null) {
            domainId = memberService.get(memberId).getDomainId();
            ThreadLocalTenantContext.setDomainId(domainId);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ThreadLocalTenantContext.domainClean();
    }
}
```

### 4. 在后台系统中切换域

在后台系统中，有两种方式可以切换域：

#### 4.1 通过请求头切换域

在发送请求时，添加 `X-domainId` 请求头：

```javascript
// 前端示例代码
axios.get('/api/member/list', {
  headers: {
    'X-domainId': '2'  // 指定域ID为2
  }
})
```

这种方式适用于整个请求的域切换，所有在该请求中执行的操作都会使用指定的域ID。

#### 4.2 通过 DomainOperationUtil 切换域

在代码中，使用 `DomainOperationUtil` 在特定操作中临时切换域：

```java
@PostMapping("/admin/change-member-level")
public CommonResult<Boolean> changeMemberLevel(@RequestParam("memberId") Integer memberId,
                                              @RequestParam("domainId") Integer domainId,
                                              @RequestParam("newLevelId") Integer newLevelId) {
    // 在指定域下执行操作
    Boolean result = DomainOperationUtil.executeWithDomain(domainId, () ->
        memberLevelService.changeMemberLevel(memberId, newLevelId, "管理员调整", "ADMIN")
    );
    return success(result);
}
```

这种方式适用于在同一请求中需要临时切换域执行特定操作的场景，操作完成后会自动恢复原来的域ID。

## 注意事项

1. 域隔离是在租户隔离的基础上进行的，确保系统已正确配置租户隔离
2. 所有需要域隔离的表都应该包含域ID字段
3. 在使用域切换功能时，确保操作完成后正确恢复原来的域ID（DomainOperationUtil 会自动处理）
4. 对于需要跨域查询的场景，可以使用 DomainOperationUtil 在不同域下执行查询，然后合并结果
