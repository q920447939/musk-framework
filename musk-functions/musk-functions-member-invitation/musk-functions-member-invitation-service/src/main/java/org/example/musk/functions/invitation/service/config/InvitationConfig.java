package org.example.musk.functions.invitation.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邀请系统配置
 *
 * @author musk-functions-member-invitation
 */
@Component
@ConfigurationProperties(prefix = "musk.invitation")
@Data
public class InvitationConfig {

    /**
     * 是否启用邀请功能
     */
    private Boolean enabled = true;

    /**
     * Web配置
     */
    private WebConfig web = new WebConfig();

    /**
     * APP配置
     */
    private AppConfig app = new AppConfig();

    /**
     * H5配置
     */
    private H5Config h5 = new H5Config();

    /**
     * 二维码配置
     */
    private QrCodeConfig qrcode = new QrCodeConfig();

    /**
     * 安全配置
     */
    private SecurityConfig security = new SecurityConfig();

    @Data
    public static class WebConfig {
        /**
         * Web基础URL
         */
        private String baseUrl = "http://localhost:8080";
    }

    @Data
    public static class AppConfig {
        /**
         * APP基础URL
         */
        private String baseUrl = "app://invitation";
    }

    @Data
    public static class H5Config {
        /**
         * H5基础URL
         */
        private String baseUrl = "http://localhost:8080/h5";
    }

    @Data
    public static class QrCodeConfig {
        /**
         * 二维码存储路径
         */
        private String storagePath = "/tmp/qrcode";

        /**
         * 二维码访问基础URL
         */
        private String baseUrl = "http://localhost:8080/qrcode";

        /**
         * 默认尺寸
         */
        private Integer defaultSize = 300;
    }

    @Data
    public static class SecurityConfig {
        /**
         * 是否启用IP限制
         */
        private Boolean enableIpLimit = true;

        /**
         * 每日每IP最大注册次数
         */
        private Integer maxRegistrationsPerIpPerDay = 10;

        /**
         * 是否启用设备指纹检测
         */
        private Boolean enableDeviceFingerprint = false;
    }

}
