package org.example.musk.auth.service.core.strategy.impl;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.entity.member.MemberDO;
import org.example.musk.auth.entity.member.vo.MemberSaveReqVO;
import org.example.musk.auth.enums.MemberStatusEnums;
import org.example.musk.auth.enums.auth.AuthChannelTypeEnum;
import org.example.musk.auth.enums.auth.AuthTypeEnum;
import org.example.musk.auth.service.core.channel.MemberAuthChannelService;
import org.example.musk.auth.service.core.code.CaptchaService;
import org.example.musk.auth.service.core.helper.MemberHelper;
import org.example.musk.auth.service.core.helper.member.MemberNickNameHelper;
import org.example.musk.auth.service.core.member.MemberService;
import org.example.musk.auth.service.core.strategy.RegistrationStrategy;
import org.example.musk.auth.service.core.util.PasswordValidator;
import org.example.musk.auth.vo.req.register.BaseRegistrationRequest;
import org.example.musk.auth.vo.req.register.UsernamePasswordRegisterRequest;
import org.example.musk.auth.vo.result.RegistrationResult;
import org.example.musk.common.exception.BusinessPageExceptionEnum;
import org.example.musk.utils.aes.AESKeyEnum;
import org.example.musk.utils.aes.AESUtils;
import org.springframework.stereotype.Component;

/**
 * 用户名密码注册策略
 *
 * @author musk
 */
@Component
@Slf4j
public class UsernamePasswordRegisterStrategy implements RegistrationStrategy {

    @Resource
    private MemberService memberService;

    @Resource
    private MemberAuthChannelService memberAuthChannelService;

    @Resource
    private CaptchaService captchaService;

    @Resource
    private PasswordValidator passwordValidator;

    @Resource
    private MemberHelper memberHelper;

    @Override
    public boolean supports(AuthTypeEnum authType) {
        return AuthTypeEnum.USERNAME_PASSWORD_REGISTER == authType;
    }

    @Override
    public RegistrationResult register(BaseRegistrationRequest request) {
        log.info("[用户名密码注册] 开始处理注册请求");

        try {
            // 类型转换
            UsernamePasswordRegisterRequest registerRequest = (UsernamePasswordRegisterRequest) request;

            // 参数校验
            RegistrationResult validationResult = validateRequest(registerRequest);
            if (!validationResult.isSuccess()) {
                return validationResult;
            }

            // 验证图形验证码
            if (!captchaService.verifyCaptcha(registerRequest.getCaptchaSessionId(), registerRequest.getCaptcha())) {
                log.warn("[用户名密码注册] 图形验证码验证失败，username={}", registerRequest.getUsername());
                return RegistrationResult.failure(
                        BusinessPageExceptionEnum.CAPTCHA_VERIFY_FAILED.getExCode(),
                        "图形验证码验证失败"
                );
            }

            // 检查用户名是否已存在
            if (memberAuthChannelService.isChannelIdentifierUsed(AuthChannelTypeEnum.USERNAME, registerRequest.getUsername())) {
                log.warn("[用户名密码注册] 用户名已存在，username={}", registerRequest.getUsername());
                return RegistrationResult.failure(
                        BusinessPageExceptionEnum.REGISTER_USERNAME_EXISTS.getExCode(),
                        "用户名已存在"
                );
            }

            // 创建会员
            MemberDO member = createMember(registerRequest);
            if (member == null) {
                log.error("[用户名密码注册] 创建会员失败，username={}", registerRequest.getUsername());
                return RegistrationResult.failure(
                        BusinessPageExceptionEnum.REGISTER_FAILED.getExCode(),
                        "注册失败，请稍后重试"
                );
            }

            // 添加用户名认证渠道
            String encryptedPassword = AESUtils.encryptHex(registerRequest.getPassword(), AESKeyEnum.PASSWORD_KEY);
            memberAuthChannelService.addAuthChannel(
                    member.getId(),
                    AuthChannelTypeEnum.USERNAME,
                    registerRequest.getUsername(),
                    encryptedPassword,
                    true // 用户名密码注册默认已验证
            );

            log.info("[用户名密码注册] 注册成功，username={}, memberId={}",
                    registerRequest.getUsername(), member.getId());

            return RegistrationResult.success(member, AuthTypeEnum.USERNAME_PASSWORD_REGISTER);

        } catch (Exception e) {
            log.error("[用户名密码注册] 注册异常", e);
            return RegistrationResult.failure(
                    BusinessPageExceptionEnum.REGISTER_FAILED.getExCode(),
                    "注册失败，请稍后重试"
            );
        }
    }

    @Override
    public String getStrategyName() {
        return "用户名密码注册策略";
    }

    @Override
    public int getPriority() {
        return 10;
    }

    /**
     * 验证注册请求
     *
     * @param request 注册请求
     * @return 验证结果
     */
    private RegistrationResult validateRequest(UsernamePasswordRegisterRequest request) {
        // 验证密码一致性
        if (!request.isPasswordMatch()) {
            return RegistrationResult.failure(
                    BusinessPageExceptionEnum.PASSWORD_NOT_MATCH.getExCode(),
                    "两次输入的密码不一致"
            );
        }

        // 验证用户名格式
        if (!request.isValidUsername()) {
            return RegistrationResult.failure(
                    BusinessPageExceptionEnum.USERNAME_FORMAT_ERROR.getExCode(),
                    "用户名格式不正确"
            );
        }

        // 验证密码强度
        if (!passwordValidator.isValidPassword(request.getPassword())) {
            return RegistrationResult.failure(
                    BusinessPageExceptionEnum.PASSWORD_WEAK.getExCode(),
                    passwordValidator.getPasswordRequirements()
            );
        }

        return RegistrationResult.success(null, AuthTypeEnum.USERNAME_PASSWORD_REGISTER);
    }

    /**
     * 创建会员
     *
     * @param request 注册请求
     * @return 会员信息
     */
    private MemberDO createMember(UsernamePasswordRegisterRequest request) {
        try {
            // 构建会员保存请求
            MemberSaveReqVO memberSaveReqVO = new MemberSaveReqVO();
            memberSaveReqVO.setMemberCode(generateMemberCode(request.getUsername()));
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
            log.error("[用户名密码注册] 创建会员异常，username={}", request.getUsername(), e);
            return null;
        }
    }

    /**
     * 生成会员编码
     *
     * @param username 用户名
     * @return 会员编码
     */
    private String generateMemberCode(String username) {
        // 使用用户名作为会员编码的基础，添加时间戳确保唯一性
        return username + "_" + System.currentTimeMillis();
    }
}
