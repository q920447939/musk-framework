package org.example.musk.auth.web.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.service.core.member.MemberService;
import org.example.musk.auth.web.anno.PassToken;
import org.example.musk.auth.web.config.exception.IllegalityTokenException;
import org.example.musk.auth.web.config.exception.NoTokenException;
import org.example.musk.auth.web.interceptor.decorator.FilterAuthDecorator;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

@Order(10)
@Slf4j
@Service
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Resource
    private FilterAuthDecorator filterAuthDecorator;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        //option预检查，直接通过请求
        if ("OPTIONS".equals(httpServletRequest.getMethod())) {
            return true;
        }

        Method method = handlerMethod.getMethod();
        Class<?> clz = object.getClass();
        // 检查是否有passtoken注释，有则跳过认证
        if (clz.isAnnotationPresent(PassToken.class) || method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.pass()) {
                return true;
            }
        }

        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        if ( filterAuthDecorator.match(httpServletRequest, object)) {
            return true;
        }
        // 执行认证
        if (token == null) {
            throw new NoTokenException("50007", "无token，请重新登录");
        }
        try {
            StpUtil.checkLogin();
        } catch (Exception e) {
            throw new IllegalityTokenException("50008", "非法的token值");
        }
        ThreadLocalTenantContext.setMemberThread((int) StpUtil.getLoginIdAsLong());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView)
            throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        ThreadLocalTenantContext.memberThreadClean();

    }
}
