package org.example.musk.auth.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证配置类
 * 
 * @author musk
 */
@Configuration
@ConfigurationProperties(prefix = "musk.auth")
@Data
public class AuthenticationConfig {

    /**
     * 密码策略配置
     */
    private PasswordConfig password = new PasswordConfig();

    /**
     * 验证码配置
     */
    private VerificationCodeConfig verificationCode = new VerificationCodeConfig();

    /**
     * 第三方登录配置
     */
    private ThirdPartyConfig thirdParty = new ThirdPartyConfig();

    /**
     * 安全策略配置
     */
    private SecurityConfig security = new SecurityConfig();

    /**
     * 密码策略配置
     */
    @Data
    public static class PasswordConfig {
        /**
         * 最小长度
         */
        private Integer minLength = 8;

        /**
         * 是否要求大写字母
         */
        private Boolean requireUppercase = false;

        /**
         * 是否要求小写字母
         */
        private Boolean requireLowercase = false;

        /**
         * 是否要求数字
         */
        private Boolean requireDigit = false;

        /**
         * 是否要求特殊字符
         */
        private Boolean requireSpecialChar = false;
    }

    /**
     * 验证码配置
     */
    @Data
    public static class VerificationCodeConfig {
        /**
         * 邮箱验证码配置
         */
        private EmailCodeConfig email = new EmailCodeConfig();

        /**
         * 短信验证码配置
         */
        private SmsCodeConfig sms = new SmsCodeConfig();

        /**
         * 邮箱验证码配置
         */
        @Data
        public static class EmailCodeConfig {
            /**
             * 是否启用
             */
            private Boolean enabled = true;

            /**
             * 过期时间（秒）
             */
            private Integer expireSeconds = 300;

            /**
             * 邮件模板ID
             */
            private String templateId = "email_login_template";

            /**
             * 发送频率限制（秒）
             */
            private Integer frequencyLimitSeconds = 60;
        }

        /**
         * 短信验证码配置
         */
        @Data
        public static class SmsCodeConfig {
            /**
             * 是否启用
             */
            private Boolean enabled = true;

            /**
             * 过期时间（秒）
             */
            private Integer expireSeconds = 300;

            /**
             * 短信服务提供商
             */
            private String provider = "aliyun";

            /**
             * 发送频率限制（秒）
             */
            private Integer frequencyLimitSeconds = 60;
        }
    }

    /**
     * 第三方登录配置
     */
    @Data
    public static class ThirdPartyConfig {
        /**
         * 微信配置
         */
        private WechatConfig wechat = new WechatConfig();

        /**
         * QQ配置
         */
        private QqConfig qq = new QqConfig();

        /**
         * 支付宝配置
         */
        private AlipayConfig alipay = new AlipayConfig();

        /**
         * GitHub配置
         */
        private GithubConfig github = new GithubConfig();

        /**
         * Google配置
         */
        private GoogleConfig google = new GoogleConfig();

        /**
         * 微信配置
         */
        @Data
        public static class WechatConfig {
            private Boolean enabled = false;
            private String appId;
            private String appSecret;
            private String redirectUri;
        }

        /**
         * QQ配置
         */
        @Data
        public static class QqConfig {
            private Boolean enabled = false;
            private String appId;
            private String appKey;
            private String redirectUri;
        }

        /**
         * 支付宝配置
         */
        @Data
        public static class AlipayConfig {
            private Boolean enabled = false;
            private String appId;
            private String privateKey;
            private String publicKey;
            private String redirectUri;
        }

        /**
         * GitHub配置
         */
        @Data
        public static class GithubConfig {
            private Boolean enabled = false;
            private String clientId;
            private String clientSecret;
            private String redirectUri;
        }

        /**
         * Google配置
         */
        @Data
        public static class GoogleConfig {
            private Boolean enabled = false;
            private String clientId;
            private String clientSecret;
            private String redirectUri;
        }
    }

    /**
     * 安全策略配置
     */
    @Data
    public static class SecurityConfig {
        /**
         * 最大登录尝试次数
         */
        private Integer maxLoginAttempts = 5;

        /**
         * 账户锁定时长（秒）
         */
        private Integer accountLockDuration = 1800;

        /**
         * 会话超时时间（秒）
         */
        private Integer sessionTimeout = 7200;

        /**
         * 是否启用设备绑定
         */
        private Boolean enableDeviceBinding = false;

        /**
         * 是否启用多因子认证
         */
        private Boolean enableMfa = false;

        /**
         * 是否启用异地登录检测
         */
        private Boolean enableLocationCheck = false;
    }

    /**
     * 获取第三方配置
     *
     * @param platform 平台名称
     * @return 配置信息
     */
    public Object getThirdPartyConfig(String platform) {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("wechat", thirdParty.getWechat());
        configMap.put("qq", thirdParty.getQq());
        configMap.put("alipay", thirdParty.getAlipay());
        configMap.put("github", thirdParty.getGithub());
        configMap.put("google", thirdParty.getGoogle());
        
        return configMap.get(platform.toLowerCase());
    }

    /**
     * 检查第三方平台是否启用
     *
     * @param platform 平台名称
     * @return true-启用，false-未启用
     */
    public boolean isThirdPartyEnabled(String platform) {
        Object config = getThirdPartyConfig(platform);
        if (config == null) {
            return false;
        }
        
        try {
            // 使用反射获取enabled字段
            return (Boolean) config.getClass().getMethod("getEnabled").invoke(config);
        } catch (Exception e) {
            return false;
        }
    }
}
