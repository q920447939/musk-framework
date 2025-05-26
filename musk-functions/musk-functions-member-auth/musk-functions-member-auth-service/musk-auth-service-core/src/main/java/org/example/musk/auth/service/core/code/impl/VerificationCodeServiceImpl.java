package org.example.musk.auth.service.core.code.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.enums.code.CodeChannelEnum;
import org.example.musk.auth.enums.code.CodeSceneEnum;
import org.example.musk.auth.service.core.code.VerificationCodeService;
import org.example.musk.auth.service.core.config.AuthenticationConfig;
import org.example.musk.auth.vo.req.code.SendCodeRequest;
import org.example.musk.auth.vo.req.code.VerifyCodeRequest;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.middleware.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 验证码服务实现
 *
 * @author musk
 */
@Service
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private AuthenticationConfig authConfig;

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    /**
     * 验证码默认长度
     */
    private static final int DEFAULT_CODE_LENGTH = 6;

    /**
     * 验证码默认过期时间（秒）
     */
    private static final int DEFAULT_EXPIRE_SECONDS = 300;

    /**
     * 发送频率限制时间（秒）
     */
    private static final int SEND_FREQUENCY_LIMIT_SECONDS = 60;

    /**
     * 验证码缓存键前缀
     */
    private static final String CODE_CACHE_PREFIX = "verification_code";

    /**
     * 发送频率限制缓存键前缀
     */
    private static final String FREQUENCY_CACHE_PREFIX = "verification_code_frequency";

    @Override
    public boolean sendCode(SendCodeRequest request) {
        log.info("[发送验证码] 开始发送，target={}, channel={}, scene={}",
                request.getTarget(), request.getChannel().getCode(), request.getScene().getCode());

        // 参数校验
        if (!request.isValidTarget()) {
            log.warn("[发送验证码] 目标格式无效，target={}, channel={}",
                    request.getTarget(), request.getChannel().getCode());
            return false;
        }

        // 检查发送频率限制
        if (!checkSendFrequency(request.getTarget(), request.getScene(), request.getChannel())) {
            log.warn("[发送验证码] 发送频率限制，target={}, channel={}, scene={}",
                    request.getTarget(), request.getChannel().getCode(), request.getScene().getCode());
            return false;
        }

        try {
            // 生成验证码
            String code = generateCode();

            // 构建缓存键
            String cacheKey = buildCodeCacheKey(request.getTarget(), request.getScene(), request.getChannel());
            String frequencyKey = buildFrequencyCacheKey(request.getTarget(), request.getScene(), request.getChannel());

            // 获取过期时间
            int expireSeconds = request.getScene().getDefaultExpireSeconds();

            // 存储验证码到Redis //TODO 时间单位
            redisUtil.set(cacheKey, code, expireSeconds, TimeUnit.SECONDS);

            // 设置发送频率限制
            redisUtil.set(frequencyKey, "1", SEND_FREQUENCY_LIMIT_SECONDS, TimeUnit.SECONDS);

            // 根据渠道发送验证码
            boolean sendResult = sendCodeByChannel(request, code);

            if (sendResult) {
                log.info("[发送验证码] 发送成功，target={}, channel={}, scene={}, code={}",
                        request.getTarget(), request.getChannel().getCode(), request.getScene().getCode(), code);
            } else {
                log.error("[发送验证码] 发送失败，target={}, channel={}, scene={}",
                        request.getTarget(), request.getChannel().getCode(), request.getScene().getCode());
                // 发送失败时删除缓存的验证码
                redisUtil.delete(cacheKey);
            }

            return sendResult;
        } catch (Exception e) {
            log.error("[发送验证码] 发送异常，target={}, channel={}, scene={}",
                    request.getTarget(), request.getChannel().getCode(), request.getScene().getCode(), e);
            return false;
        }
    }

    @Override
    public boolean verifyCode(VerifyCodeRequest request) {
        log.debug("[验证验证码] 开始验证，target={}, channel={}, scene={}, code={}",
                request.getTarget(), request.getChannel().getCode(), request.getScene().getCode(), request.getCode());

        // 参数校验
        if (!request.isValidTarget() || StrUtil.isBlank(request.getCode())) {
            log.debug("[验证验证码] 参数无效，target={}, channel={}, code={}",
                    request.getTarget(), request.getChannel().getCode(), request.getCode());
            return false;
        }

        try {
            // 开发环境特殊处理
            if (isDevelopmentMode()) {
                Boolean devResult = handleDevelopmentVerification(request);
                if (devResult != null) {
                    return devResult;
                }
            }

            // 构建缓存键
            String cacheKey = buildCodeCacheKey(request.getTarget(), request.getScene(), request.getChannel());

            // 从Redis获取验证码
            String cachedCode = redisUtil.get(cacheKey);

            if (StrUtil.isBlank(cachedCode)) {
                log.debug("[验证验证码] 验证码不存在或已过期，target={}, channel={}, scene={}",
                        request.getTarget(), request.getChannel().getCode(), request.getScene().getCode());
                return false;
            }

            // 验证验证码
            boolean verified = StrUtil.equals(request.getCode(), cachedCode);

            if (verified) {
                // 验证成功后删除验证码（一次性使用）
                redisUtil.del(cacheKey);
                log.info("[验证验证码] 验证成功，target={}, channel={}, scene={}",
                        request.getTarget(), request.getChannel().getCode(), request.getScene().getCode());
            } else {
                log.debug("[验证验证码] 验证失败，target={}, channel={}, scene={}, expected={}, actual={}",
                        request.getTarget(), request.getChannel().getCode(), request.getScene().getCode(),
                        cachedCode, request.getCode());
            }

            return verified;
        } catch (Exception e) {
            log.error("[验证验证码] 验证异常，target={}, channel={}, scene={}",
                    request.getTarget(), request.getChannel().getCode(), request.getScene().getCode(), e);
            return false;
        }
    }

    @Override
    public boolean invalidateCode(String target, CodeSceneEnum scene, CodeChannelEnum channel) {
        log.info("[使验证码失效] target={}, channel={}, scene={}", target, channel.getCode(), scene.getCode());

        try {
            String cacheKey = buildCodeCacheKey(target, scene, channel);
            redisUtil.delete(cacheKey);
            return true;
        } catch (Exception e) {
            log.error("[使验证码失效] 操作异常，target={}, channel={}, scene={}",
                    target, channel.getCode(), scene.getCode(), e);
            return false;
        }
    }

    @Override
    public boolean checkSendFrequency(String target, CodeSceneEnum scene, CodeChannelEnum channel) {
        try {
            String frequencyKey = buildFrequencyCacheKey(target, scene, channel);
            return !redisUtil.hasKey(frequencyKey);
        } catch (Exception e) {
            log.error("[检查发送频率] 检查异常，target={}, channel={}, scene={}",
                    target, channel.getCode(), scene.getCode(), e);
            // 异常时允许发送
            return true;
        }
    }

    @Override
    public long getCodeRemainingTime(String target, CodeSceneEnum scene, CodeChannelEnum channel) {
        try {
            String cacheKey = buildCodeCacheKey(target, scene, channel);
            return redisUtil.getExpire(cacheKey, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("[获取验证码剩余时间] 获取异常，target={}, channel={}, scene={}",
                    target, channel.getCode(), scene.getCode(), e);
            return 0;
        }
    }

    @Override
    public long getNextSendRemainingTime(String target, CodeSceneEnum scene, CodeChannelEnum channel) {
        try {
            String frequencyKey = buildFrequencyCacheKey(target, scene, channel);
            return redisUtil.getExpire(frequencyKey, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("[获取下次发送剩余时间] 获取异常，target={}, channel={}, scene={}",
                    target, channel.getCode(), scene.getCode(), e);
            return 0;
        }
    }

    @Override
    public String generateCode(int length) {
        if (length <= 0) {
            length = DEFAULT_CODE_LENGTH;
        }
        return RandomUtil.randomNumbers(length);
    }

    @Override
    public String buildCodeCacheKey(String target, CodeSceneEnum scene, CodeChannelEnum channel) {
        return String.format("%s:%s:%s:%s:%d:%d",
                CODE_CACHE_PREFIX,
                channel.getCode(),
                scene.getCode(),
                target,
                ThreadLocalTenantContext.getTenantId(),
                ThreadLocalTenantContext.getDomainId());
    }

    @Override
    public String buildFrequencyCacheKey(String target, CodeSceneEnum scene, CodeChannelEnum channel) {
        return String.format("%s:%s:%s:%s:%d:%d",
                FREQUENCY_CACHE_PREFIX,
                channel.getCode(),
                scene.getCode(),
                target,
                ThreadLocalTenantContext.getTenantId(),
                ThreadLocalTenantContext.getDomainId());
    }

    /**
     * 根据渠道发送验证码
     *
     * @param request 发送请求
     * @param code    验证码
     * @return true-发送成功，false-发送失败
     */
    private boolean sendCodeByChannel(SendCodeRequest request, String code) {
        switch (request.getChannel()) {
            case EMAIL:
                return sendEmailCode(request, code);
            case SMS:
                return sendSmsCode(request, code);
            default:
                log.error("[发送验证码] 不支持的渠道类型，channel={}", request.getChannel().getCode());
                return false;
        }
    }

    /**
     * 发送邮箱验证码
     *
     * @param request 发送请求
     * @param code    验证码
     * @return true-发送成功，false-发送失败
     */
    private boolean sendEmailCode(SendCodeRequest request, String code) {
        log.info("[发送邮箱验证码] 开始发送，email={}, scene={}, code={}",
                request.getTarget(), request.getScene().getCode(), code);

        try {
            // TODO: 集成邮件服务发送验证码
            // 这里可以集成阿里云邮件服务、腾讯云邮件服务等
            // 示例代码：
            // EmailRequest emailRequest = EmailRequest.builder()
            //     .to(request.getTarget())
            //     .subject(getEmailSubject(request.getScene()))
            //     .content(getEmailContent(request.getScene(), code))
            //     .build();
            // return emailService.send(emailRequest);

            // 暂时模拟发送成功
            log.info("[发送邮箱验证码] 模拟发送成功，email={}, code={}", request.getTarget(), code);
            return true;
        } catch (Exception e) {
            log.error("[发送邮箱验证码] 发送失败，email={}, scene={}",
                    request.getTarget(), request.getScene().getCode(), e);
            return false;
        }
    }

    /**
     * 发送短信验证码
     *
     * @param request 发送请求
     * @param code    验证码
     * @return true-发送成功，false-发送失败
     */
    private boolean sendSmsCode(SendCodeRequest request, String code) {
        log.info("[发送短信验证码] 开始发送，phone={}, scene={}, code={}",
                request.getTarget(), request.getScene().getCode(), code);

        try {
            // TODO: 集成短信服务发送验证码
            // 这里可以集成阿里云短信服务、腾讯云短信服务等
            // 示例代码：
            // SmsRequest smsRequest = SmsRequest.builder()
            //     .phone(request.getTarget())
            //     .templateCode(getSmsTemplateCode(request.getScene()))
            //     .templateParams(Map.of("code", code))
            //     .build();
            // return smsService.send(smsRequest);

            // 暂时模拟发送成功
            log.info("[发送短信验证码] 模拟发送成功，phone={}, code={}", request.getTarget(), code);
            return true;
        } catch (Exception e) {
            log.error("[发送短信验证码] 发送失败，phone={}, scene={}",
                    request.getTarget(), request.getScene().getCode(), e);
            return false;
        }
    }

    /**
     * 获取邮件主题
     *
     * @param scene 验证码场景
     * @return 邮件主题
     */
    private String getEmailSubject(CodeSceneEnum scene) {
        switch (scene) {
            case LOGIN:
                return "登录验证码";
            case REGISTER:
                return "注册验证码";
            case BIND:
                return "绑定验证码";
            case RESET_PASSWORD:
                return "重置密码验证码";
            case CHANGE_EMAIL:
                return "更换邮箱验证码";
            case CHANGE_PHONE:
                return "更换手机号验证码";
            default:
                return "验证码";
        }
    }

    /**
     * 获取邮件内容
     *
     * @param scene 验证码场景
     * @param code  验证码
     * @return 邮件内容
     */
    private String getEmailContent(CodeSceneEnum scene, String code) {
        String sceneDesc = scene.getDesc();
        return String.format("您的%s验证码是：%s，有效期为%d分钟，请勿泄露给他人。",
                sceneDesc, code, scene.getDefaultExpireSeconds() / 60);
    }

    /**
     * 获取短信模板代码
     *
     * @param scene 验证码场景
     * @return 短信模板代码
     */
    private String getSmsTemplateCode(CodeSceneEnum scene) {
        // 根据场景返回对应的短信模板代码
        // 这些模板代码需要在短信服务商平台申请
        switch (scene) {
            case LOGIN:
                return "SMS_LOGIN_TEMPLATE";
            case REGISTER:
                return "SMS_REGISTER_TEMPLATE";
            case BIND:
                return "SMS_BIND_TEMPLATE";
            case RESET_PASSWORD:
                return "SMS_RESET_PASSWORD_TEMPLATE";
            case CHANGE_EMAIL:
                return "SMS_CHANGE_EMAIL_TEMPLATE";
            case CHANGE_PHONE:
                return "SMS_CHANGE_PHONE_TEMPLATE";
            default:
                return "SMS_DEFAULT_TEMPLATE";
        }
    }

    /**
     * 是否为开发模式
     *
     * @return true-开发模式，false-非开发模式
     */
    private boolean isDevelopmentMode() {
        return "dev".equals(activeProfile);
    }

    /**
     * 处理开发环境验证
     *
     * @param request 验证请求
     * @return 验证结果，null表示降级到正常验证
     */
    private Boolean handleDevelopmentVerification(VerifyCodeRequest request) {
        try {
            // 根据渠道获取配置
            if (request.getChannel() == CodeChannelEnum.EMAIL) {
                AuthenticationConfig.VerificationCodeConfig.EmailCodeConfig emailConfig =
                        authConfig.getVerificationCode().getEmail();

                // 检查是否跳过验证
                if (emailConfig.getDevMode() != null && emailConfig.getDevMode().getSkipVerification()) {
                    log.info("[验证验证码] 开发环境跳过邮箱验证码验证，target={}", request.getTarget());
                    return true;
                }

                // 检查固定验证码
                if (emailConfig.getDevMode() != null &&
                    StrUtil.isNotBlank(emailConfig.getDevMode().getFixedCode()) &&
                    StrUtil.equals(request.getCode(), emailConfig.getDevMode().getFixedCode())) {
                    log.info("[验证验证码] 开发环境使用固定邮箱验证码验证成功，target={}, code={}",
                            request.getTarget(), request.getCode());
                    return true;
                }

                // 检查是否允许任意验证码
                if (emailConfig.getDevMode() != null && emailConfig.getDevMode().getAllowAnyCode()) {
                    log.info("[验证验证码] 开发环境允许任意邮箱验证码，target={}, code={}",
                            request.getTarget(), request.getCode());
                    return true;
                }
            } else if (request.getChannel() == CodeChannelEnum.SMS) {
                AuthenticationConfig.VerificationCodeConfig.SmsCodeConfig smsConfig =
                        authConfig.getVerificationCode().getSms();

                // 检查是否跳过验证
                if (smsConfig.getDevMode() != null && smsConfig.getDevMode().getSkipVerification()) {
                    log.info("[验证验证码] 开发环境跳过短信验证码验证，target={}", request.getTarget());
                    return true;
                }

                // 检查固定验证码
                if (smsConfig.getDevMode() != null &&
                    StrUtil.isNotBlank(smsConfig.getDevMode().getFixedCode()) &&
                    StrUtil.equals(request.getCode(), smsConfig.getDevMode().getFixedCode())) {
                    log.info("[验证验证码] 开发环境使用固定短信验证码验证成功，target={}, code={}",
                            request.getTarget(), request.getCode());
                    return true;
                }

                // 检查是否允许任意验证码
                if (smsConfig.getDevMode() != null && smsConfig.getDevMode().getAllowAnyCode()) {
                    log.info("[验证验证码] 开发环境允许任意短信验证码，target={}, code={}",
                            request.getTarget(), request.getCode());
                    return true;
                }
            }

            // 降级到正常验证
            return null;

        } catch (Exception e) {
            log.warn("[验证验证码] 开发环境处理异常，降级到正常模式", e);
            return null;
        }
    }
}
