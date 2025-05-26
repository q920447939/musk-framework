package org.example.musk.auth.service.core.strategy.impl;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.entity.member.MemberDO;
import org.example.musk.auth.entity.member.vo.MemberSaveReqVO;
import org.example.musk.auth.enums.MemberStatusEnums;
import org.example.musk.auth.enums.auth.AuthChannelTypeEnum;
import org.example.musk.auth.enums.auth.AuthTypeEnum;
import org.example.musk.auth.enums.code.CodeSceneEnum;
import org.example.musk.auth.service.core.channel.MemberAuthChannelService;
import org.example.musk.auth.service.core.code.VerificationCodeService;
import org.example.musk.auth.service.core.helper.MemberHelper;
import org.example.musk.auth.service.core.helper.member.MemberNickNameHelper;
import org.example.musk.auth.service.core.member.MemberService;
import org.example.musk.auth.service.core.strategy.RegistrationStrategy;
import org.example.musk.auth.vo.req.code.VerifyCodeRequest;
import org.example.musk.auth.vo.req.register.BaseRegistrationRequest;
import org.example.musk.auth.vo.req.register.VerificationCodeRegisterRequest;
import org.example.musk.auth.vo.result.RegistrationResult;
import org.example.musk.common.exception.BusinessPageExceptionEnum;
import org.springframework.stereotype.Component;

/**
 * 短信验证码注册策略
 *
 * @author musk
 */
@Component
@Slf4j
public class SmsCodeRegisterStrategy implements RegistrationStrategy {

    @Resource
    private MemberService memberService;

    @Resource
    private MemberAuthChannelService memberAuthChannelService;

    @Resource
    private VerificationCodeService verificationCodeService;

    @Resource
    private MemberHelper memberHelper;

    @Override
    public boolean supports(AuthTypeEnum authType) {
        return AuthTypeEnum.SMS_CODE_REGISTER == authType;
    }

    @Override
    public RegistrationResult register(BaseRegistrationRequest request) {
        log.info("[短信验证码注册] 开始处理注册请求");

        try {
            // 类型转换
            VerificationCodeRegisterRequest registerRequest = (VerificationCodeRegisterRequest) request;

            // 参数校验
            RegistrationResult validationResult = validateRequest(registerRequest);
            if (!validationResult.isSuccess()) {
                return validationResult;
            }

            // 验证短信验证码
            VerifyCodeRequest verifyRequest = new VerifyCodeRequest();
            verifyRequest.setTarget(registerRequest.getTarget());
            verifyRequest.setCode(registerRequest.getVerificationCode());
            verifyRequest.setChannel(registerRequest.getChannel());
            verifyRequest.setScene(CodeSceneEnum.REGISTER);
            verifyRequest.setClientIp(registerRequest.getClientIp());

            if (!verificationCodeService.verifyCode(verifyRequest)) {
                log.warn("[短信验证码注册] 验证码验证失败，phone={}, code={}",
                        registerRequest.getTarget(), registerRequest.getVerificationCode());
                return RegistrationResult.failure(
                        BusinessPageExceptionEnum.VERIFICATION_CODE_ERROR.getExCode(),
                        "验证码错误或已过期"
                );
            }

            // 检查手机号是否已存在
            if (memberAuthChannelService.isChannelIdentifierUsed(AuthChannelTypeEnum.PHONE, registerRequest.getTarget())) {
                log.warn("[短信验证码注册] 手机号已存在，phone={}", registerRequest.getTarget());
                return RegistrationResult.failure(
                        BusinessPageExceptionEnum.AUTH_CHANNEL_ALREADY_EXISTS.getExCode(),
                        "手机号已被注册"
                );
            }

            // 创建会员
            MemberDO member = createMember(registerRequest);
            if (member == null) {
                log.error("[短信验证码注册] 创建会员失败，phone={}", registerRequest.getTarget());
                return RegistrationResult.failure(
                        BusinessPageExceptionEnum.REGISTER_FAILED.getExCode(),
                        "注册失败，请稍后重试"
                );
            }

            // 添加手机号认证渠道
            memberAuthChannelService.addAuthChannel(
                    member.getId(),
                    AuthChannelTypeEnum.PHONE,
                    registerRequest.getTarget(),
                    registerRequest.getTarget(), // 手机号渠道的值就是手机号本身
                    true // 通过验证码验证，标记为已验证
            );

            log.info("[短信验证码注册] 注册成功，phone={}, memberId={}",
                    registerRequest.getTarget(), member.getId());

            return RegistrationResult.success(member, AuthTypeEnum.SMS_CODE_REGISTER);

        } catch (Exception e) {
            log.error("[短信验证码注册] 注册异常", e);
            return RegistrationResult.failure(
                    BusinessPageExceptionEnum.REGISTER_FAILED.getExCode(),
                    "注册失败，请稍后重试"
            );
        }
    }

    @Override
    public String getStrategyName() {
        return "短信验证码注册策略";
    }

    @Override
    public int getPriority() {
        return 30;
    }

    /**
     * 验证注册请求
     *
     * @param request 注册请求
     * @return 验证结果
     */
    private RegistrationResult validateRequest(VerificationCodeRegisterRequest request) {
        // 验证手机号格式
        if (!request.isValidTarget()) {
            return RegistrationResult.failure(
                    BusinessPageExceptionEnum.PARAMS_ERROR.getExCode(),
                    "手机号格式不正确"
            );
        }

        return RegistrationResult.success(null, AuthTypeEnum.SMS_CODE_REGISTER);
    }

    /**
     * 创建会员
     *
     * @param request 注册请求
     * @return 会员信息
     */
    private MemberDO createMember(VerificationCodeRegisterRequest request) {
        try {
            // 构建会员保存请求
            MemberSaveReqVO memberSaveReqVO = new MemberSaveReqVO();
            memberSaveReqVO.setMemberCode(generateMemberCode(request.getTarget()));
            memberSaveReqVO.setMemberNickName(
                    StrUtil.isNotBlank(request.getNickname()) ?
                    request.getNickname() :
                    MemberNickNameHelper.generatorName()
            );
            memberSaveReqVO.setAvatar(memberHelper.getDefaultAvatar());
            memberSaveReqVO.setStatus(MemberStatusEnums.EFFECTIVE.getStatus());

            // 创建会员
            return memberService.register(memberSaveReqVO);

        } catch (Exception e) {
            log.error("[短信验证码注册] 创建会员异常，phone={}", request.getTarget(), e);
            return null;
        }
    }

    /**
     * 生成会员编码
     *
     * @param phone 手机号
     * @return 会员编码
     */
    private String generateMemberCode(String phone) {
        // 使用手机号作为会员编码的基础，添加时间戳确保唯一性
        return "phone_" + phone + "_" + System.currentTimeMillis();
    }
}
