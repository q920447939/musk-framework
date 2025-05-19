package org.example.musk.framework.permission.web.interceptor;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.framework.permission.constant.PermissionConstant;
import org.example.musk.framework.permission.exception.PermissionException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 领域拦截器
 * 
 * 用于从请求头中获取领域ID并设置到线程上下文中
 *
 * @author musk-framework-permission
 */
@Order(15) // 在租户拦截器之后，认证拦截器之前
@Slf4j
@Component
public class DomainInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse, Object object) throws Exception {
        String domainIdHeader = httpServletRequest.getHeader(PermissionConstant.DOMAIN_ID_HEADER);
        if (StrUtil.isNotBlank(domainIdHeader) && NumberUtil.isNumber(domainIdHeader)) {
            ThreadLocalTenantContext.setDomainId(Integer.valueOf(domainIdHeader));
            log.debug("设置领域ID: {}", domainIdHeader);
        } else {
            // 设置默认领域
            ThreadLocalTenantContext.setDomainId(PermissionConstant.DEFAULT_DOMAIN_ID);
            log.debug("未找到领域ID，使用默认领域ID: {}", PermissionConstant.DEFAULT_DOMAIN_ID);
        }
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
