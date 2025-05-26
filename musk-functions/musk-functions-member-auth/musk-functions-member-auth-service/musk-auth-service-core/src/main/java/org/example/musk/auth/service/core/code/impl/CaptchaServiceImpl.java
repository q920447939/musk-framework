package org.example.musk.auth.service.core.code.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.service.core.code.CaptchaService;
import org.example.musk.auth.service.core.config.AuthenticationConfig;
import org.example.musk.middleware.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 图形验证码服务实现
 *
 * @author musk
 */
@Service
@Slf4j
public class CaptchaServiceImpl implements CaptchaService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private AuthenticationConfig authConfig;

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    /**
     * 验证码缓存键前缀
     */
    private static final String CAPTCHA_CACHE_PREFIX = "captcha";

    /**
     * 默认过期时间（秒）
     */
    private static final int DEFAULT_EXPIRE_SECONDS = 300;

    /**
     * 验证码图片宽度
     */
    private static final int CAPTCHA_WIDTH = 130;

    /**
     * 验证码图片高度
     */
    private static final int CAPTCHA_HEIGHT = 48;

    /**
     * 验证码字符数
     */
    private static final int CAPTCHA_CODE_COUNT = 4;

    /**
     * 干扰线数量
     */
    private static final int CAPTCHA_LINE_COUNT = 150;

    @Override
    public CaptchaInfo generateCaptcha() {
        try {
            // 生成会话ID
            String sessionId = IdUtil.simpleUUID();

            // 开发环境特殊处理
            if (isDevelopmentMode()) {
                return handleDevelopmentMode(sessionId);
            }

            // 生成图形验证码
            LineCaptcha captcha = CaptchaUtil.createLineCaptcha(
                    CAPTCHA_WIDTH,
                    CAPTCHA_HEIGHT,
                    CAPTCHA_CODE_COUNT,
                    CAPTCHA_LINE_COUNT
            );

            // 获取验证码文本
            String code = captcha.getCode();

            // 获取图片Base64
            String imageBase64 = captcha.getImageBase64Data();

            // 存储到Redis
            String cacheKey = buildCacheKey(sessionId);
            int expireSeconds = getExpireSeconds();
            redisUtil.set(cacheKey, code.toLowerCase(), expireSeconds, TimeUnit.SECONDS);

            log.debug("[图形验证码] 生成成功，sessionId={}, code={}", sessionId, code);

            return new CaptchaInfo(sessionId, imageBase64, expireSeconds);

        } catch (Exception e) {
            log.error("[图形验证码] 生成异常", e);
            throw new RuntimeException("生成验证码失败", e);
        }
    }

    @Override
    public boolean verifyCaptcha(String sessionId, String code) {
        if (StrUtil.isBlank(sessionId) || StrUtil.isBlank(code)) {
            log.debug("[图形验证码] 验证参数为空，sessionId={}, code={}", sessionId, code);
            return false;
        }

        try {
            // 开发环境特殊处理
            if (isDevelopmentMode()) {
                return handleDevelopmentVerification(code);
            }

            // 构建缓存键
            String cacheKey = buildCacheKey(sessionId);

            // 从Redis获取验证码
            String cachedCode = redisUtil.get(cacheKey);

            if (StrUtil.isBlank(cachedCode)) {
                log.debug("[图形验证码] 验证码不存在或已过期，sessionId={}", sessionId);
                return false;
            }

            // 验证验证码（忽略大小写）
            boolean verified = StrUtil.equalsIgnoreCase(code, cachedCode);

            if (verified) {
                // 验证成功后删除验证码（一次性使用）
                redisUtil.del(cacheKey);
                log.info("[图形验证码] 验证成功，sessionId={}", sessionId);
            } else {
                log.debug("[图形验证码] 验证失败，sessionId={}, expected={}, actual={}",
                        sessionId, cachedCode, code);
            }

            return verified;

        } catch (Exception e) {
            log.error("[图形验证码] 验证异常，sessionId={}, code={}", sessionId, code, e);
            return false;
        }
    }

    @Override
    public void clearCaptcha(String sessionId) {
        if (StrUtil.isBlank(sessionId)) {
            return;
        }

        try {
            String cacheKey = buildCacheKey(sessionId);
            redisUtil.del(cacheKey);
            log.debug("[图形验证码] 清除成功，sessionId={}", sessionId);
        } catch (Exception e) {
            log.error("[图形验证码] 清除异常，sessionId={}", sessionId, e);
        }
    }

    /**
     * 构建缓存键
     *
     * @param sessionId 会话ID
     * @return 缓存键
     */
    private String buildCacheKey(String sessionId) {
        return CAPTCHA_CACHE_PREFIX + ":" + sessionId;
    }

    /**
     * 获取过期时间
     *
     * @return 过期时间（秒）
     */
    private int getExpireSeconds() {
        try {
            return authConfig.getVerificationCode().getCaptcha().getExpireSeconds();
        } catch (Exception e) {
            log.warn("[图形验证码] 获取配置失败，使用默认过期时间", e);
            return DEFAULT_EXPIRE_SECONDS;
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
     * 处理开发环境模式
     *
     * @param sessionId 会话ID
     * @return 验证码信息
     */
    private CaptchaInfo handleDevelopmentMode(String sessionId) {
        try {
            AuthenticationConfig.VerificationCodeConfig.CaptchaConfig captchaConfig = authConfig.getVerificationCode().getCaptcha();

            // 检查是否跳过验证
            if (captchaConfig.getDevMode().getSkipVerification()) {
                log.info("[图形验证码] 开发环境跳过验证码生成");
                return new CaptchaInfo(sessionId, "data:image/png;base64,skip", getExpireSeconds());
            }

            // 使用固定验证码
            String fixedCode = captchaConfig.getDevMode().getFixedCode();
            if (StrUtil.isNotBlank(fixedCode)) {
                // 存储固定验证码到Redis
                String cacheKey = buildCacheKey(sessionId);
                redisUtil.set(cacheKey, fixedCode.toLowerCase(), getExpireSeconds(), TimeUnit.SECONDS);

                log.info("[图形验证码] 开发环境使用固定验证码，sessionId={}, code={}", sessionId, fixedCode);
                return new CaptchaInfo(sessionId, "data:image/png;base64,fixed", getExpireSeconds());
            }

            // 降级到正常生成
            return null;

        } catch (Exception e) {
            log.warn("[图形验证码] 开发环境处理异常，降级到正常模式", e);
            return null;
        }
    }

    /**
     * 处理开发环境验证
     *
     * @param code 验证码
     * @return 验证结果
     */
    private boolean handleDevelopmentVerification(String code) {
        try {
            AuthenticationConfig.VerificationCodeConfig.CaptchaConfig captchaConfig = authConfig.getVerificationCode().getCaptcha();

            // 检查是否跳过验证
            if (captchaConfig.getDevMode().getSkipVerification()) {
                log.info("[图形验证码] 开发环境跳过验证码验证");
                return true;
            }

            // 检查是否允许任意验证码
            if (captchaConfig.getDevMode().getAllowAnyCode()) {
                log.info("[图形验证码] 开发环境允许任意验证码，code={}", code);
                return true;
            }

            // 降级到正常验证
            return false;

        } catch (Exception e) {
            log.warn("[图形验证码] 开发环境验证处理异常，降级到正常模式", e);
            return false;
        }
    }
}
