package org.example.musk.auth.service.core.password.impl;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.enums.auth.AuthChannelTypeEnum;
import org.example.musk.auth.enums.code.CodeChannelEnum;
import org.example.musk.auth.enums.code.CodeSceneEnum;
import org.example.musk.auth.service.core.channel.MemberAuthChannelService;
import org.example.musk.auth.service.core.code.CaptchaService;
import org.example.musk.auth.service.core.code.VerificationCodeService;
import org.example.musk.auth.service.core.config.AuthenticationConfig;
import org.example.musk.auth.service.core.helper.PasswordValidator;
import org.example.musk.auth.service.core.member.MemberService;
import org.example.musk.auth.service.core.password.MemberPasswordHistoryService;
import org.example.musk.auth.service.core.password.MemberPasswordService;
import org.example.musk.auth.vo.req.code.SendCodeRequest;
import org.example.musk.auth.vo.req.code.VerifyCodeRequest;
import org.example.musk.auth.vo.req.password.ChangePasswordRequest;
import org.example.musk.auth.vo.req.password.ForgotPasswordRequest;
import org.example.musk.auth.vo.req.password.ResetPasswordRequest;
import org.example.musk.auth.vo.result.PasswordValidationResult;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.common.exception.BusinessPageExceptionEnum;
import org.example.musk.common.threadVirtual.ThreadVirtualUtils;
import org.example.musk.middleware.redis.RedisUtil;
import org.example.musk.utils.aes.AESKeyEnum;
import org.example.musk.utils.aes.AESUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * 会员密码管理服务实现
 *
 * @author musk
 */
@Service
@Slf4j
public class MemberPasswordServiceImpl implements MemberPasswordService {

    @Resource
    private MemberAuthChannelService memberAuthChannelService;

    @Resource
    private MemberPasswordHistoryService passwordHistoryService;

    @Resource
    private PasswordValidator passwordValidator;

    @Resource
    private CaptchaService captchaService;

    @Resource
    private VerificationCodeService verificationCodeService;

    @Resource
    private AuthenticationConfig authConfig;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private MemberService memberService;

    /**
     * 密码重置频率限制缓存键前缀
     */
    private static final String RESET_FREQUENCY_PREFIX = "password_reset_frequency:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changePassword(Integer memberId, ChangePasswordRequest request) {
        log.info("[修改密码] 开始修改密码，memberId={}", memberId);

        try {
            // 1. 验证图形验证码
            if (!captchaService.verifyCaptcha(request.getCaptchaSessionId(), request.getCaptchaCode())) {
                log.warn("[修改密码] 图形验证码验证失败，memberId={}", memberId);
                throw new BusinessException(BusinessPageExceptionEnum.CAPTCHA_CODE_ERROR);
            }

            // 2. 验证新密码和确认密码是否一致
            if (!StrUtil.equals(request.getNewPassword(), request.getConfirmPassword())) {
                log.warn("[修改密码] 新密码和确认密码不一致，memberId={}", memberId);
                throw new BusinessException(BusinessPageExceptionEnum.PASSWORD_CONFIRM_NOT_MATCH);
            }

            // 3. 验证密码强度
            PasswordValidationResult validationResult = passwordValidator.validatePassword(request.getNewPassword());
            if (!validationResult.getValid()) {
                log.warn("[修改密码] 密码强度验证失败，memberId={}, reasons={}",
                        memberId, validationResult.getFailureReasons());
                throw new BusinessException(BusinessPageExceptionEnum.PASSWORD_STRENGTH_NOT_ENOUGH);
            }

            // 4. 验证旧密码
            String oldPasswordHash = AESUtils.encryptHex(request.getOldPassword(), AESKeyEnum.PASSWORD_KEY);
            if (!memberAuthChannelService.verifyAuthChannel(AuthChannelTypeEnum.USERNAME,
                    getMemberUsername(memberId), oldPasswordHash)) {
                log.warn("[修改密码] 旧密码验证失败，memberId={}", memberId);
                throw new BusinessException(BusinessPageExceptionEnum.OLD_PASSWORD_ERROR);
            }

            // 5. 检查新密码是否在历史记录中
            String newPasswordHash = AESUtils.encryptHex(request.getNewPassword(), AESKeyEnum.PASSWORD_KEY);
            if (isPasswordInHistory(memberId, newPasswordHash)) {
                log.warn("[修改密码] 新密码在历史记录中，memberId={}", memberId);
                throw new BusinessException(BusinessPageExceptionEnum.PASSWORD_IN_HISTORY);
            }

            // 6. 更新密码
            boolean updateResult = memberAuthChannelService.updateChannelValue(
                    AuthChannelTypeEnum.USERNAME, getMemberUsername(memberId), newPasswordHash);

            if (!updateResult) {
                log.error("[修改密码] 更新密码失败，memberId={}", memberId);
                throw new BusinessException(BusinessPageExceptionEnum.PASSWORD_UPDATE_FAILED);
            }

            // 7. 保存密码历史记录
            savePasswordHistory(memberId, newPasswordHash);

            // 8. 清理过期的密码历史记录
            cleanExpiredPasswordHistory(memberId);

            log.info("[修改密码] 密码修改成功，memberId={}", memberId);
            return true;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("[修改密码] 修改密码异常，memberId={}", memberId, e);
            throw new BusinessException(BusinessPageExceptionEnum.PASSWORD_CHANGE_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(ResetPasswordRequest request) {
        log.info("[重置密码] 开始重置密码，identifier={}, channel={}",
                request.getIdentifier(), request.getChannel().getCode());

        try {
            // 1. 验证新密码和确认密码是否一致
            if (!StrUtil.equals(request.getNewPassword(), request.getConfirmPassword())) {
                log.warn("[重置密码] 新密码和确认密码不一致，identifier={}", request.getIdentifier());
                throw new BusinessException(BusinessPageExceptionEnum.PASSWORD_CONFIRM_NOT_MATCH);
            }

            // 2. 验证密码强度
            PasswordValidationResult validationResult = passwordValidator.validatePassword(request.getNewPassword());
            if (!validationResult.getValid()) {
                log.warn("[重置密码] 密码强度验证失败，identifier={}, reasons={}",
                        request.getIdentifier(), validationResult.getFailureReasons());
                throw new BusinessException(BusinessPageExceptionEnum.PASSWORD_STRENGTH_NOT_ENOUGH);
            }

            // 3. 验证验证码
            CodeSceneEnum scene = CodeSceneEnum.RESET_PASSWORD;
            VerifyCodeRequest verifyCodeRequest = new VerifyCodeRequest();
            verifyCodeRequest.setTarget(request.getIdentifier());
            verifyCodeRequest.setCode(request.getVerificationCode());
            verifyCodeRequest.setScene(scene);
            verifyCodeRequest.setChannel(request.getChannel());

            boolean codeValid = verificationCodeService.verifyCode(verifyCodeRequest);

            if (!codeValid) {
                log.warn("[重置密码] 验证码验证失败，identifier={}, channel={}",
                        request.getIdentifier(), request.getChannel().getCode());
                throw new BusinessException(BusinessPageExceptionEnum.VERIFICATION_CODE_ERROR);
            }

            // 4. 根据渠道类型查找认证渠道
            AuthChannelTypeEnum channelType = request.getChannel() == CodeChannelEnum.EMAIL ?
                    AuthChannelTypeEnum.EMAIL : AuthChannelTypeEnum.PHONE;

            // 5. 查找会员
            var member = memberAuthChannelService.findMemberByChannel(channelType, request.getIdentifier());
            if (member == null) {
                log.warn("[重置密码] 未找到对应的会员，identifier={}, channelType={}",
                        request.getIdentifier(), channelType.getCode());
                throw new BusinessException(BusinessPageExceptionEnum.MEMBER_NOT_EXISTS);
            }

            // 6. 检查新密码是否在历史记录中
            String newPasswordHash = AESUtils.encryptHex(request.getNewPassword(), AESKeyEnum.PASSWORD_KEY);
            if (isPasswordInHistory(member.getId(), newPasswordHash)) {
                log.warn("[重置密码] 新密码在历史记录中，memberId={}", member.getId());
                throw new BusinessException(BusinessPageExceptionEnum.PASSWORD_IN_HISTORY);
            }

            // 7. 更新密码（通过用户名渠道）
            String memberUsername = getMemberUsername(member.getId());
            boolean updateResult = memberAuthChannelService.updateChannelValue(
                    AuthChannelTypeEnum.USERNAME, memberUsername, newPasswordHash);

            if (!updateResult) {
                log.error("[重置密码] 更新密码失败，memberId={}", member.getId());
                throw new BusinessException(BusinessPageExceptionEnum.PASSWORD_UPDATE_FAILED);
            }

            // 8. 保存密码历史记录
            savePasswordHistory(member.getId(), newPasswordHash);

            // 9. 清理过期的密码历史记录
            cleanExpiredPasswordHistory(member.getId());

            log.info("[重置密码] 密码重置成功，memberId={}", member.getId());
            return true;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("[重置密码] 重置密码异常，identifier={}", request.getIdentifier(), e);
            throw new BusinessException(BusinessPageExceptionEnum.PASSWORD_RESET_FAILED);
        }
    }

    @Override
    public boolean forgotPassword(ForgotPasswordRequest request) {
        log.info("[忘记密码] 开始处理忘记密码请求，identifier={}, channel={}",
                request.getIdentifier(), request.getChannel().getCode());

        try {
            // 1. 验证图形验证码
            if (!captchaService.verifyCaptcha(request.getCaptchaSessionId(), request.getCaptchaCode())) {
                log.warn("[忘记密码] 图形验证码验证失败，identifier={}", request.getIdentifier());
                throw new BusinessException(BusinessPageExceptionEnum.CAPTCHA_CODE_ERROR);
            }

            // 2. 检查重置频率限制
            if (!checkResetFrequencyLimit(request.getIdentifier())) {
                log.warn("[忘记密码] 重置频率超限，identifier={}", request.getIdentifier());
                throw new BusinessException(BusinessPageExceptionEnum.PASSWORD_RESET_FREQUENCY_LIMIT);
            }

            // 3. 验证渠道是否存在
            AuthChannelTypeEnum channelType = request.getChannel() == CodeChannelEnum.EMAIL ?
                    AuthChannelTypeEnum.EMAIL : AuthChannelTypeEnum.PHONE;

            var member = memberAuthChannelService.findMemberByChannel(channelType, request.getIdentifier());
            if (member == null) {
                log.warn("[忘记密码] 未找到对应的会员，identifier={}, channelType={}",
                        request.getIdentifier(), channelType.getCode());
                throw new BusinessException(BusinessPageExceptionEnum.MEMBER_NOT_EXISTS);
            }

            // 4. 发送验证码
            SendCodeRequest sendCodeRequest = new SendCodeRequest();
            sendCodeRequest.setTarget(request.getIdentifier());
            sendCodeRequest.setChannel(request.getChannel());
            sendCodeRequest.setScene(CodeSceneEnum.RESET_PASSWORD);

            boolean sendResult = verificationCodeService.sendCode(sendCodeRequest);
            if (!sendResult) {
                log.error("[忘记密码] 发送验证码失败，identifier={}", request.getIdentifier());
                throw new BusinessException(BusinessPageExceptionEnum.VERIFICATION_CODE_SEND_FAILED);
            }

            // 5. 记录重置尝试
            recordResetAttempt(request.getIdentifier());

            log.info("[忘记密码] 验证码发送成功，identifier={}", request.getIdentifier());
            return true;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("[忘记密码] 处理异常，identifier={}", request.getIdentifier(), e);
            throw new BusinessException(BusinessPageExceptionEnum.FORGOT_PASSWORD_FAILED);
        }
    }

    @Override
    public PasswordValidationResult validatePasswordStrength(String password) {
        log.debug("[密码验证] 验证密码强度");
        return passwordValidator.validatePassword(password);
    }

    @Override
    public boolean isPasswordInHistory(Integer memberId, String password) {
        String passwordHash = AESUtils.encryptHex(password, AESKeyEnum.PASSWORD_KEY);
        Integer checkCount = authConfig.getPassword().getHistoryCount();
        return passwordHistoryService.isPasswordInHistory(memberId, passwordHash, checkCount);
    }

    @Override
    public void savePasswordHistory(Integer memberId, String passwordHash) {
        passwordHistoryService.savePasswordHistory(memberId, passwordHash, "密码修改");
    }

    @Override
    public void cleanExpiredPasswordHistory(Integer memberId) {
        Integer keepCount = authConfig.getPassword().getHistoryCount();
        passwordHistoryService.cleanExpiredHistory(memberId, keepCount);
    }

    @Override
    public boolean checkResetFrequencyLimit(String identifier) {
        String cacheKey = RESET_FREQUENCY_PREFIX + identifier;
        String attempts = redisUtil.get(cacheKey);

        if (StrUtil.isBlank(attempts)) {
            return true;
        }

        int attemptCount = Integer.parseInt(attempts);
        int maxAttempts = authConfig.getPassword().getResetAttemptLimit();

        return attemptCount < maxAttempts;
    }

    @Override
    public void recordResetAttempt(String identifier) {
        String cacheKey = RESET_FREQUENCY_PREFIX + identifier;
        String attempts = redisUtil.get(cacheKey);

        int attemptCount = StrUtil.isBlank(attempts) ? 0 : Integer.parseInt(attempts);
        attemptCount++;

        int windowHours = authConfig.getPassword().getResetAttemptWindowHours();
        redisUtil.set(cacheKey, String.valueOf(attemptCount), windowHours, TimeUnit.HOURS);
    }

    /**
     * 获取会员用户名
     *
     * @param memberId 会员ID
     * @return 用户名
     */
    private String getMemberUsername(Integer memberId) {
        try {
            var member = memberService.getMemberInfoByMemberId(memberId);
            return member.getMemberCode();
        } catch (Exception e) {
            log.error("[获取会员用户名] 获取失败，memberId={}", memberId, e);
            throw new BusinessException(BusinessPageExceptionEnum.MEMBER_NOT_EXISTS);
        }
    }
}
