package org.example.musk.auth.web.config.filter;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.example.musk.auth.web.utils.RequestUtils;
import org.example.musk.constant.request.RequestConstant;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Order(20)
@Component
public class GlobalFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestId = httpRequest.getParameter("requestId");
        MDC.put(RequestConstant.MDC_TRA_ID, StrUtil.isNotBlank(requestId) ? requestId : IdUtil.simpleUUID());
        MDC.put(RequestConstant.MDC_REQUEST_IP, RequestUtils.getIpAddr(httpRequest));
        chain.doFilter(request, response);
    }

}
