package org.example.musk.auth.web.config.filter;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.example.musk.auth.web.config.filter.wrapper.XssHttpServletRequestWrapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * xss过滤器
 */
@Component
@Order(40)
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // String servletPath = httpServletRequest.getServletPath();

        chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
    }

}
