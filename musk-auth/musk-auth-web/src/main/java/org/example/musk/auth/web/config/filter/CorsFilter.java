package org.example.musk.auth.web.config.filter;

import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.musk.framework.tenant.config.TenantConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(30)
public class CorsFilter implements Filter {
    final static Logger logger = LoggerFactory.getLogger(CorsFilter.class);

    @Resource
    private TenantConfig  tenantConfig;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        //允许所有域名跨域访问该资源，根据项目实际需要可以设置允许特定的域名访问
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        //允许的跨域请求方式
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        /* 每次异步请求都发起预检请求，也就是说，发送两次请求。
           第一次是浏览器使用OPTIONS方法发起一个预检请求，第二次才是真正的异步请求.
           第一次的预检请求获知服务器是否允许该跨域请求：如果允许，才发起第二次真实的请求；
           如果不允许，则拦截第二次请求。
         */
        //Access-Control-Max-Age这个响应首部表示preflight request（预检请求）的返回结果（即 Access-Control-Allow-Methods 和Access-Control-Allow-Headers 提供的信息） 可以被缓存多久。
        response.setHeader("Access-Control-Max-Age", "1800");
        //跨域请求允许包含的头
//        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With,"
                + " If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires,"
                + " Content-Type, X-E4M-With,"+ tenantConfig.getTenantIdKey());
        //是否支持跨域，是否允许请求带有验证信息
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("XDomainRequestAllowed", "1");
        // logger.info("*********************************过滤器被使用**************************");
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}
