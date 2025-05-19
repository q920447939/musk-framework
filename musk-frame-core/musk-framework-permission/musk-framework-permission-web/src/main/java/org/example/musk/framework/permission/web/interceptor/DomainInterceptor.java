package org.example.musk.framework.permission.web.interceptor;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.service.core.member.MemberService;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 领域拦截器
 * <p>
 * 用于从请求头中获取领域ID并设置到线程上下文中
 *
 * @author musk-framework-permission
 */
@Order(15) // 在租户拦截器之后，认证拦截器之前
@Slf4j
@Component
public class DomainInterceptor implements HandlerInterceptor {
    @Resource
    private MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse, Object object) throws Exception {
        ThreadLocalTenantContext.setDomainId(memberService.get(ThreadLocalTenantContext.getMemberId()).getDomainId());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
        // 不需要处理
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse, Object o, Exception e) {
        // 清理领域ID
        ThreadLocalTenantContext.domainClean();
    }
}
