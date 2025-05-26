package org.example.musk.auth.service.core.strategy.impl;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.entity.channel.MemberAuthChannelDO;
import org.example.musk.auth.entity.member.MemberDO;
import org.example.musk.auth.enums.auth.AuthChannelTypeEnum;
import org.example.musk.auth.enums.auth.AuthTypeEnum;
import org.example.musk.auth.service.core.channel.MemberAuthChannelService;
import org.example.musk.auth.service.core.code.CaptchaService;
import org.example.musk.auth.service.core.strategy.AuthenticationStrategy;
import org.example.musk.auth.vo.req.auth.UsernamePasswordAuthRequest;
import org.example.musk.auth.vo.result.AuthenticationResult;
import org.example.musk.common.exception.BusinessPageExceptionEnum;
import org.example.musk.utils.aes.AESKeyEnum;
import org.example.musk.utils.aes.AESUtils;
import org.springframework.stereotype.Component;

/**
 * 用户名密码认证策略
 *
 * @author musk
 */
@Component
@Slf4j
public class UsernamePasswordAuthStrategy implements AuthenticationStrategy<UsernamePasswordAuthRequest> {

    @Resource
    private MemberAuthChannelService memberAuthChannelService;

    @Resource
    private CaptchaService captchaService;

    @Override
    public boolean supports(AuthTypeEnum authType) {
        return AuthTypeEnum.USERNAME_PASSWORD.equals(authType);
    }

    @Override
    public AuthenticationResult authenticate(UsernamePasswordAuthRequest request) {
        log.info("[用户名密码认证] 开始认证，username={}", request.getUsername());

        try {
            // 前置处理
            if (!preProcess(request)) {
                return AuthenticationResult.failure(
                        BusinessPageExceptionEnum.AUTH_PRE_PROCESS_FAILED.getExCode(),
                        "认证前置处理失败"
                );
            }

            // 查找认证渠道
            MemberAuthChannelDO authChannel = memberAuthChannelService.findAuthChannel(
                    AuthChannelTypeEnum.USERNAME, request.getUsername());

            if (authChannel == null) {
                log.warn("[用户名密码认证] 用户不存在，username={}", request.getUsername());
                return AuthenticationResult.failure(
                        BusinessPageExceptionEnum.API_INVALID_USERNAME_CODE.getExCode(),
                        "用户名或密码错误"
                );
            }

            // 验证密码
            String encryptedPassword = AESUtils.encryptHex(request.getPassword(), AESKeyEnum.PASSWORD_KEY);
            if (!StrUtil.equals(encryptedPassword, authChannel.getChannelValue())) {
                log.warn("[用户名密码认证] 密码错误，username={}", request.getUsername());
                return AuthenticationResult.failure(
                        BusinessPageExceptionEnum.API_INVALID_USERNAME_CODE.getExCode(),
                        "用户名或密码错误"
                );
            }

            // 获取会员信息
            MemberDO member = getOrCreateMember(request, authChannel);
            if (member == null) {
                log.error("[用户名密码认证] 获取会员信息失败，username={}", request.getUsername());
                return AuthenticationResult.failure(
                        BusinessPageExceptionEnum.MEMBER_NOT_EXISTS.getExCode(),
                        "会员信息不存在"
                );
            }

            // 检查会员状态
            if (!isMemberActive(member)) {
                log.warn("[用户名密码认证] 会员状态异常，username={}, status={}",
                        request.getUsername(), member.getStatus());
                return AuthenticationResult.failure(
                        BusinessPageExceptionEnum.MEMBER_STATUS_ABNORMAL.getExCode(),
                        "账户状态异常，请联系客服"
                );
            }

            // 认证成功
            AuthenticationResult result = AuthenticationResult.success(member);

            // 后置处理
            postProcess(request, result);

            log.info("[用户名密码认证] 认证成功，username={}, memberId={}",
                    request.getUsername(), member.getId());
            return result;

        } catch (Exception e) {
            log.error("[用户名密码认证] 认证异常，username={}", request.getUsername(), e);
            return AuthenticationResult.failure(
                    BusinessPageExceptionEnum.COMMON_RESULT_BOOL_IS_FALSE.getExCode(),
                    "系统异常，请稍后重试"
            );
        }
    }

    @Override
    public MemberDO getOrCreateMember(UsernamePasswordAuthRequest request, MemberAuthChannelDO authChannel) {
        // 用户名密码认证不创建新会员，只获取已存在的会员
        return memberAuthChannelService.findMemberByChannel(
                AuthChannelTypeEnum.USERNAME, request.getUsername());
    }

    @Override
    public boolean preProcess(UsernamePasswordAuthRequest request) {
        // 参数校验
        if (StrUtil.isBlank(request.getUsername()) || StrUtil.isBlank(request.getPassword())) {
            log.warn("[用户名密码认证] 参数校验失败，username={}", request.getUsername());
            return false;
        }

        // 1. 图形验证码校验
        if (!validateCaptcha(request)) {
            log.warn("[用户名密码认证] 图形验证码校验失败，username={}, captchaSessionId={}",
                    request.getUsername(), request.getCaptchaSessionId());
            return false;
        }

        // TODO: 可以在这里添加其他前置处理逻辑
        // 2. 登录频率限制检查
        // 3. IP黑名单检查
        // 4. 设备指纹验证

        return true;
    }

    @Override
    public void postProcess(UsernamePasswordAuthRequest request, AuthenticationResult result) {
        if (result.isSuccess()) {
            // 更新最后使用时间
            memberAuthChannelService.updateLastUsedTime(
                    AuthChannelTypeEnum.USERNAME, request.getUsername());

            // TODO: 可以在这里添加其他后置处理逻辑
            // 1. 记录登录日志
            // 2. 更新会员最后登录时间和IP
            // 3. 发送登录通知
            // 4. 统计登录次数
        }
    }

    /**
     * 验证图形验证码
     *
     * @param request 认证请求
     * @return true-验证成功，false-验证失败
     */
    private boolean validateCaptcha(UsernamePasswordAuthRequest request) {
        try {
            // 参数校验
            if (StrUtil.isBlank(request.getCaptcha()) || StrUtil.isBlank(request.getCaptchaSessionId())) {
                log.debug("[用户名密码认证] 验证码参数为空，captcha={}, captchaSessionId={}",
                        request.getCaptcha(), request.getCaptchaSessionId());
                return false;
            }

            // 调用验证码服务进行验证
            boolean verified = captchaService.verifyCaptcha(request.getCaptchaSessionId(), request.getCaptcha());

            if (verified) {
                log.debug("[用户名密码认证] 图形验证码验证成功，username={}, captchaSessionId={}",
                        request.getUsername(), request.getCaptchaSessionId());
            } else {
                log.warn("[用户名密码认证] 图形验证码验证失败，username={}, captcha={}, captchaSessionId={}",
                        request.getUsername(), request.getCaptcha(), request.getCaptchaSessionId());
            }

            return verified;

        } catch (Exception e) {
            log.error("[用户名密码认证] 图形验证码验证异常，username={}, captchaSessionId={}",
                    request.getUsername(), request.getCaptchaSessionId(), e);
            return false;
        }
    }

    /**
     * 检查会员是否处于活跃状态
     *
     * @param member 会员信息
     * @return true-活跃，false-非活跃
     */
    private boolean isMemberActive(MemberDO member) {
        // 这里需要根据实际的会员状态枚举来判断
        // 假设状态值：1-正常，2-冻结，3-注销
        return member.getStatus() != null && member.getStatus() == 1;
    }
}
