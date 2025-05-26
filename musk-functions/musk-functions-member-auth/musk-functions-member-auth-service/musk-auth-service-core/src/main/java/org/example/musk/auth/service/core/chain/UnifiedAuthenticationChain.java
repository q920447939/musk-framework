package org.example.musk.auth.service.core.chain;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.enums.auth.AuthTypeEnum;
import org.example.musk.auth.service.core.strategy.AuthenticationStrategy;
import org.example.musk.auth.vo.req.auth.BaseAuthRequest;
import org.example.musk.auth.vo.result.AuthenticationResult;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.common.exception.BusinessPageExceptionEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 统一认证链
 * 整合所有认证策略，提供统一的认证入口
 *
 * @author musk
 */
@Service
@Slf4j
public class UnifiedAuthenticationChain {

    /**
     * 认证策略映射
     * key: 认证类型，value: 认证策略
     */
    private final Map<AuthTypeEnum, AuthenticationStrategy> strategyMap;

    /**
     * 构造函数，自动注入所有认证策略并构建映射
     *
     * @param strategies 所有认证策略实现
     */
    public UnifiedAuthenticationChain(List<AuthenticationStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        strategy -> {
                            // 找到每个策略支持的认证类型
                            for (AuthTypeEnum authType : AuthTypeEnum.values()) {
                                if (strategy.supports(authType)) {
                                    return authType;
                                }
                            }
                            throw new IllegalStateException("认证策略未声明支持的认证类型: " + strategy.getClass().getName());
                        },
                        Function.identity(),
                        (existing, replacement) -> {
                            log.warn("[统一认证链] 发现重复的认证策略，existing={}, replacement={}",
                                    existing.getClass().getName(), replacement.getClass().getName());
                            return existing; // 保留第一个
                        }
                ));

        log.info("[统一认证链] 初始化完成，支持的认证类型: {}",
                strategyMap.keySet().stream().map(AuthTypeEnum::getDesc).collect(Collectors.toList()));
    }

    /**
     * 执行认证
     *
     * @param request 认证请求
     * @return 认证结果
     */
    @SuppressWarnings("unchecked")
    public AuthenticationResult authenticate(BaseAuthRequest request) {
        log.info("[统一认证链] 开始认证，authType={}", request.getAuthType().getDesc());

        try {
            // 参数校验
            if (request.getAuthType() == null) {
                log.warn("[统一认证链] 认证请求参数无效");
                return AuthenticationResult.failure(
                        BusinessPageExceptionEnum.PARAMS_ERROR.getExCode(),
                        "认证请求参数无效"
                );
            }

            // 获取对应的认证策略
            AuthenticationStrategy strategy = strategyMap.get(request.getAuthType());
            if (strategy == null) {
                log.warn("[统一认证链] 不支持的认证类型，authType={}", request.getAuthType().getDesc());
                return AuthenticationResult.failure(
                        BusinessPageExceptionEnum.AUTH_TYPE_NOT_SUPPORTED.getExCode(),
                        "不支持的认证类型: " + request.getAuthType().getDesc()
                );
            }

            // 记录认证开始
            log.info("[统一认证链] 使用策略进行认证，strategy={}, authType={}",
                    strategy.getStrategyName(), request.getAuthType().getDesc());

            // 执行认证
            AuthenticationResult result = strategy.authenticate(request);

            // 记录认证结果
            if (result.isSuccess()) {
                log.info("[统一认证链] 认证成功，authType={}, memberId={}",
                        request.getAuthType().getDesc(),
                        result.getMember() != null ? result.getMember().getId() : null);
            } else {
                log.warn("[统一认证链] 认证失败，authType={}, errorCode={}, errorMessage={}",
                        request.getAuthType().getDesc(), result.getErrorCode(), result.getErrorMessage());
            }

            return result;

        } catch (BusinessException e) {
            log.error("[统一认证链] 认证业务异常，authType={}, errorCode={}, errorMessage={}",
                    request.getAuthType().getDesc(), e.getErrorCode(), e.getMessage());
            return AuthenticationResult.failure(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            log.error("[统一认证链] 认证系统异常，authType={}", request.getAuthType().getDesc(), e);
            return AuthenticationResult.failure(
                    BusinessPageExceptionEnum.COMMON_RESULT_BOOL_IS_FALSE.getExCode(),
                    "系统异常，请稍后重试"
            );
        }
    }

    /**
     * 检查是否支持指定的认证类型
     *
     * @param authType 认证类型
     * @return true-支持，false-不支持
     */
    public boolean isSupported(AuthTypeEnum authType) {
        return strategyMap.containsKey(authType);
    }

    /**
     * 获取支持的认证类型列表
     *
     * @return 支持的认证类型列表
     */
    public List<AuthTypeEnum> getSupportedAuthTypes() {
        return List.copyOf(strategyMap.keySet());
    }

    /**
     * 获取指定认证类型的策略
     *
     * @param authType 认证类型
     * @return 认证策略，如果不存在则返回null
     */
    public AuthenticationStrategy getStrategy(AuthTypeEnum authType) {
        return strategyMap.get(authType);
    }

    /**
     * 获取策略统计信息
     *
     * @return 策略统计信息
     */
    public Map<String, Object> getStrategyStats() {
        return Map.of(
                "totalStrategies", strategyMap.size(),
                "supportedAuthTypes", strategyMap.keySet().stream()
                        .map(AuthTypeEnum::getDesc)
                        .collect(Collectors.toList()),
                "strategyDetails", strategyMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                entry -> entry.getKey().getDesc(),
                                entry -> entry.getValue().getStrategyName()
                        ))
        );
    }
}
