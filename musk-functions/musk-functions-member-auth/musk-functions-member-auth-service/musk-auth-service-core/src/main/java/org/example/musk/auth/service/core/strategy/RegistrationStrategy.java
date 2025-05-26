package org.example.musk.auth.service.core.strategy;

import org.example.musk.auth.enums.auth.AuthTypeEnum;
import org.example.musk.auth.vo.req.register.BaseRegistrationRequest;
import org.example.musk.auth.vo.result.RegistrationResult;

/**
 * 注册策略接口
 *
 * @author musk
 */
public interface RegistrationStrategy {

    /**
     * 是否支持指定的注册类型
     *
     * @param authType 注册类型
     * @return true-支持，false-不支持
     */
    boolean supports(AuthTypeEnum authType);

    /**
     * 执行注册
     *
     * @param request 注册请求
     * @return 注册结果
     */
    RegistrationResult register(BaseRegistrationRequest request);

    /**
     * 获取策略名称
     *
     * @return 策略名称
     */
    String getStrategyName();

    /**
     * 获取策略描述
     *
     * @return 策略描述
     */
    default String getStrategyDescription() {
        return getStrategyName();
    }

    /**
     * 策略优先级
     * 数值越小优先级越高
     *
     * @return 优先级
     */
    default int getPriority() {
        return 100;
    }
}
