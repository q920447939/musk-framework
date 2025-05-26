package org.example.musk.auth.service.core.chain;

import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.enums.auth.AuthTypeEnum;
import org.example.musk.auth.service.core.strategy.RegistrationStrategy;
import org.example.musk.auth.vo.req.register.BaseRegistrationRequest;
import org.example.musk.auth.vo.result.RegistrationResult;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.common.exception.BusinessPageExceptionEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 统一注册链
 *
 * @author musk
 */
@Service
@Slf4j
public class UnifiedRegistrationChain {

    /**
     * 注册策略映射
     */
    private final Map<AuthTypeEnum, RegistrationStrategy> strategyMap;

    /**
     * 构造函数，自动注入所有注册策略并构建映射
     *
     * @param strategies 所有注册策略实现
     */
    public UnifiedRegistrationChain(List<RegistrationStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        strategy -> {
                            // 找到每个策略支持的注册类型
                            for (AuthTypeEnum authType : AuthTypeEnum.values()) {
                                if (strategy.supports(authType)) {
                                    return authType;
                                }
                            }
                            throw new IllegalStateException("注册策略未声明支持的注册类型: " + strategy.getClass().getName());
                        },
                        Function.identity(),
                        (existing, replacement) -> {
                            log.warn("[统一注册链] 发现重复的注册策略，existing={}, replacement={}",
                                    existing.getClass().getName(), replacement.getClass().getName());
                            return existing; // 保留第一个
                        }
                ));

        log.info("[统一注册链] 初始化完成，注册策略数量: {}", strategyMap.size());
        strategyMap.forEach((authType, strategy) -> 
                log.info("[统一注册链] 注册类型: {} -> 策略: {}", authType.getDesc(), strategy.getStrategyName()));
    }

    /**
     * 执行注册
     *
     * @param request 注册请求
     * @return 注册结果
     */
    public RegistrationResult register(BaseRegistrationRequest request) {
        log.info("[统一注册链] 开始处理注册请求，authType={}", 
                request.getAuthType() != null ? request.getAuthType().getDesc() : "null");

        try {
            // 参数校验
            if (request.getAuthType() == null) {
                log.warn("[统一注册链] 注册请求参数无效");
                return RegistrationResult.failure(
                        BusinessPageExceptionEnum.PARAMS_ERROR.getExCode(),
                        "注册请求参数无效"
                );
            }

            // 获取对应的注册策略
            RegistrationStrategy strategy = strategyMap.get(request.getAuthType());
            if (strategy == null) {
                log.warn("[统一注册链] 不支持的注册类型，authType={}", request.getAuthType().getDesc());
                return RegistrationResult.failure(
                        BusinessPageExceptionEnum.AUTH_TYPE_NOT_SUPPORTED.getExCode(),
                        "不支持的注册类型: " + request.getAuthType().getDesc()
                );
            }

            // 记录注册开始
            log.info("[统一注册链] 使用策略进行注册，strategy={}, authType={}",
                    strategy.getStrategyName(), request.getAuthType().getDesc());

            // 执行注册
            RegistrationResult result = strategy.register(request);

            // 记录注册结果
            if (result.isSuccess()) {
                log.info("[统一注册链] 注册成功，authType={}, memberId={}",
                        request.getAuthType().getDesc(), 
                        result.getMember() != null ? result.getMember().getId() : "null");
            } else {
                log.warn("[统一注册链] 注册失败，authType={}, errorCode={}, errorMessage={}",
                        request.getAuthType().getDesc(), result.getErrorCode(), result.getErrorMessage());
            }

            return result;

        } catch (BusinessException e) {
            log.error("[统一注册链] 注册业务异常，authType={}, errorCode={}, errorMessage={}",
                    request.getAuthType().getDesc(), e.getErrorCode(), e.getMessage());
            return RegistrationResult.failure(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            log.error("[统一注册链] 注册系统异常，authType={}", request.getAuthType().getDesc(), e);
            return RegistrationResult.failure(
                    BusinessPageExceptionEnum.COMMON_RESULT_BOOL_IS_FALSE.getExCode(),
                    "系统异常，请稍后重试"
            );
        }
    }

    /**
     * 检查是否支持指定的注册类型
     *
     * @param authType 注册类型
     * @return true-支持，false-不支持
     */
    public boolean isSupported(AuthTypeEnum authType) {
        return strategyMap.containsKey(authType);
    }

    /**
     * 获取支持的注册类型列表
     *
     * @return 支持的注册类型列表
     */
    public List<AuthTypeEnum> getSupportedRegisterTypes() {
        return strategyMap.keySet().stream()
                .filter(AuthTypeEnum::isRegister)
                .collect(Collectors.toList());
    }

    /**
     * 获取注册策略信息
     *
     * @return 策略信息映射
     */
    public Map<AuthTypeEnum, String> getStrategyInfo() {
        return strategyMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getStrategyName()
                ));
    }
}
