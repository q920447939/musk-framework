package org.example.musk.auth.service.core.strategy;

import org.example.musk.auth.entity.channel.MemberAuthChannelDO;
import org.example.musk.auth.entity.member.MemberDO;
import org.example.musk.auth.enums.auth.AuthTypeEnum;
import org.example.musk.auth.vo.req.auth.BaseAuthRequest;
import org.example.musk.auth.vo.result.AuthenticationResult;

/**
 * 认证策略接口
 *
 * @param <T> 认证请求类型
 * @author musk
 */
public interface AuthenticationStrategy<T extends BaseAuthRequest> {

    /**
     * 是否支持该认证类型
     *
     * @param authType 认证类型
     * @return true-支持，false-不支持
     */
    boolean supports(AuthTypeEnum authType);

    /**
     * 执行认证
     *
     * @param request 认证请求
     * @return 认证结果
     */
    AuthenticationResult authenticate(T request);

    /**
     * 获取或创建会员
     * 如果会员不存在，则根据认证信息创建新会员
     *
     * @param request 认证请求
     * @param authChannel 认证渠道信息
     * @return 会员信息
     */
    MemberDO getOrCreateMember(T request, MemberAuthChannelDO authChannel);

    /**
     * 认证前置处理
     * 可用于参数校验、限流检查等
     *
     * @param request 认证请求
     * @return true-继续执行，false-终止认证
     */
    default boolean preProcess(T request) {
        return true;
    }

    /**
     * 认证后置处理
     * 可用于更新最后登录时间、记录日志等
     *
     * @param request 认证请求
     * @param result 认证结果
     */
    default void postProcess(T request, AuthenticationResult result) {
        // 默认空实现
    }

    /**
     * 获取策略名称
     * 用于日志记录和调试
     *
     * @return 策略名称
     */
    default String getStrategyName() {
        return this.getClass().getSimpleName();
    }
}
