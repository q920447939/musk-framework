package org.example.musk.framework.permission.web.interceptor;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.service.core.member.MemberService;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.framework.domain.config.DomainConfig;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 领域拦截器
 * <p>
 * 用于获取领域ID并设置到线程上下文中
 * 优先级：
 * 1. 本地配置的默认域ID（客户端）
 * 2. 请求头中的X-domainId（管理端）
 * 3. 会员信息中的域ID（兜底）
 *
 * @author musk-framework-permission
 */
@Order(15) // 在租户拦截器之后，认证拦截器之前
@Slf4j
@Component
public class DomainInterceptor implements HandlerInterceptor {
    @Resource
    private MemberService memberService;

    @Resource
    private DomainConfig domainConfig;

    /**
     * 请求头中域ID的键名
     */
    private static final String DOMAIN_ID_HEADER = "X-domainId";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 1. 优先使用本地配置的默认域ID（客户端）
        Integer domainId = domainConfig.getConfigDefaultDomainId();
        if (domainId != null) {
            log.debug("使用本地配置的默认域ID: {}", domainId);
            ThreadLocalTenantContext.setDomainId(domainId);
            return true;
        }

        // 2. 尝试从请求头中获取域ID（管理端）
        String headerDomainId = httpServletRequest.getHeader(DOMAIN_ID_HEADER);
        if (StrUtil.isNotBlank(headerDomainId)) {
            try {
                domainId = Integer.parseInt(headerDomainId);
                log.debug("使用请求头中的域ID: {}", domainId);
                ThreadLocalTenantContext.setDomainId(domainId);
                return true;
            } catch (NumberFormatException e) {
                log.warn("请求头中的域ID格式不正确: {}", headerDomainId);
            }
        }

        // 3. 兜底：从会员信息中获取域ID
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        if (memberId != null) {
            try {
                domainId = memberService.get(memberId).getDomainId();
                if (domainId != null) {
                    log.debug("使用会员信息中的域ID: {}", domainId);
                    ThreadLocalTenantContext.setDomainId(domainId);
                    return true;
                }
            } catch (Exception e) {
                log.warn("获取会员域ID失败: {}", e.getMessage());
            }
        }

        log.warn("无法获取域ID，请检查配置或请求头");
        return false;
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
