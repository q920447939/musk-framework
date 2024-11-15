package org.example.musk.auth.web.aop;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.example.musk.middleware.redis.RedisUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * 登录频率限制
 */
@Aspect
@Component
@Slf4j
@Order(60)
public class LimitRateLoginAspect {

    @Resource
    private RedisUtil redisUtil;

    @Pointcut("@annotation(org.example.musk.auth.web.anno.LimitRateLogin)")
    public void aop() {
    }

    @Before("aop()")
    public void before(JoinPoint joinPoint) {
        /*Object[] args = joinPoint.getArgs();
        if (null == args) {
            //为空，交给具体的controller处理
            return;
        }
        HttpServletRequest request = (HttpServletRequest) args[0];
        LoginRequestDTO loginRequestDTO = (LoginRequestDTO) args[1];
        if (null == loginRequestDTO || null == loginRequestDTO.getUserName()) {
            //为空，交给具体的controller处理
            return;
        }
        String key = String.format(RedisCacheTenantConstant.TENANT_CONTEXT_REDIS_CACHE_TYPE_REQUEST_LIMIT_RATE, ThreadLocalTenantContext.getTenantId(), JakartaServletUtil.getClientIP(request));
        Object o = redisUtil.get(key);

        AppParamsConfigDO appParamsConfigDO = AppParamConfigUtil.queryAppParamsConfigByType(ThreadLocalTenantContext.getTenantId(),AppParamsConfigTypeEnums.LOGIN_LIMIT_RATE);
        int minute = Integer.parseInt(appParamsConfigDO.getValue1());


        LocalDateTime now = LocalDateTimeUtil.now();
        if (o == null) {
            setLoginLimit(key, now,1,minute);
            return;
        }

        LoginLimitRate loginLimitRate = (LoginLimitRate) o;

        long interval = LocalDateTimeUtil.between(loginLimitRate.getFirstLoginTime(), now, ChronoUnit.MINUTES);
        if (interval > minute) {
            //已经超过5 分钟，重新计数
            setLoginLimit(key, now,1,minute);
            return;
        }
        //否则， 查看统计次数，如果次数超过约定的值，那么抛出异常
        if (loginLimitRate.getLoginCount() >= Integer.parseInt(appParamsConfigDO.getValue2())) {
            throw new LoginLimitRateException();
        }
        //否则 记录登录次数
        setLoginLimit(key, loginLimitRate.getFirstLoginTime(),loginLimitRate.getLoginCount() + 1,minute);*/
    }

    private void setLoginLimit(String key, LocalDateTime now, int value, int ttlMinute) {
/*        LoginLimitRate loginLimitRate = new LoginLimitRate();
        loginLimitRate.setFirstLoginTime(now);
        loginLimitRate.setLoginCount(value);
        redisUtil.set(key, loginLimitRate, ttlMinute * 60L);*/
    }
}
