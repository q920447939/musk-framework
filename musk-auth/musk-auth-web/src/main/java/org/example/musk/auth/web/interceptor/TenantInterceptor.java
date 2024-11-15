package org.example.musk.auth.web.interceptor;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.web.config.exception.IllegalityTenantException;
import org.example.musk.auth.web.config.exception.NotTenantException;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.framework.tenant.TenantConfig;
import org.example.musk.utils.aes.AESKeyEnum;
import org.example.musk.utils.aes.AESUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Order(5)
@Component
@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    @Resource
    private TenantConfig tenantConfig;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse, Object object) throws Exception {
        String tenantIdKey = tenantConfig.getTenantIdKey();
        String encryptTenantId = httpServletRequest.getHeader(tenantIdKey);
        if (StrUtil.isBlank(encryptTenantId)) {
            //TODO 记录日志
            throw new NotTenantException(tenantIdKey);
        }
        String decryptTenantId;
        try {
            decryptTenantId = AESUtils.decryptStr(encryptTenantId ,AESKeyEnum.TENANT_KEY);
        } catch (Exception e) {
            log.error("【租户套餐解密】 解密失败，原值:{}", encryptTenantId);
            throw new IllegalityTenantException(tenantIdKey);
        }
        if (!NumberUtil.isNumber(decryptTenantId)) {
            //TODO 记录日志
            throw new IllegalityTenantException(tenantIdKey);
        }
        ThreadLocalTenantContext.setTenantId(Integer.valueOf(decryptTenantId));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse, Object o, Exception e) {
        ThreadLocalTenantContext.tenantClean();
    }


}
