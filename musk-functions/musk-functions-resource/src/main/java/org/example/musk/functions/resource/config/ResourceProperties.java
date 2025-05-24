package org.example.musk.functions.resource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源模块配置属性
 *
 * @author musk-functions-resource
 */
@ConfigurationProperties(prefix = "musk.functions.resource")
@Data
public class ResourceProperties {

    /**
     * 存储配置
     */
    private StorageConfig storage = new StorageConfig();

    /**
     * 安全配置
     */
    private SecurityConfig security = new SecurityConfig();

    /**
     * 处理配置
     */
    private ProcessConfig process = new ProcessConfig();

    /**
     * 缓存配置
     */
    private CacheConfig cache = new CacheConfig();

    /**
     * 限制配置
     */
    private LimitConfig limit = new LimitConfig();

    /**
     * 存储配置
     */
    @Data
    public static class StorageConfig {
        /**
         * 默认存储类型：1-本地存储，2-阿里云OSS，3-腾讯云COS
         */
        private Integer defaultType = 1;

        /**
         * 本地存储配置
         */
        private LocalStorageConfig local = new LocalStorageConfig();

        /**
         * 阿里云OSS配置
         */
        private AliyunOssConfig aliyun = new AliyunOssConfig();

        /**
         * 本地存储配置
         */
        @Data
        public static class LocalStorageConfig {
            /**
             * 基础路径
             */
            private String basePath = System.getProperty("user.dir") + "/upload";

            /**
             * 访问域名
             */
            private String domain = "http://localhost:8080";
        }

        /**
         * 阿里云OSS配置
         */
        @Data
        public static class AliyunOssConfig {
            /**
             * 端点
             */
            private String endpoint;

            /**
             * 存储桶
             */
            private String bucket;

            /**
             * 访问密钥ID
             */
            private String accessKeyId;

            /**
             * 访问密钥密码
             */
            private String accessKeySecret;
        }
    }

    /**
     * 安全配置
     */
    @Data
    public static class SecurityConfig {
        /**
         * 允许的文件类型（为空表示允许所有非黑名单类型）
         */
        private List<String> allowedTypes = new ArrayList<>();

        /**
         * 禁止的文件类型
         */
        private List<String> blockedTypes = new ArrayList<>();

        /**
         * 最大文件大小（字节）
         */
        private Long maxFileSize = 104857600L; // 默认100MB

        /**
         * URL签名配置
         */
        private SignatureConfig signature = new SignatureConfig();

        /**
         * 防盗链配置
         */
        private AntiLeechConfig antiLeech = new AntiLeechConfig();

        /**
         * URL签名配置
         */
        @Data
        public static class SignatureConfig {
            /**
             * 是否启用URL签名
             */
            private Boolean enabled = true;

            /**
             * 签名密钥
             */
            private String secret = "default-signature-secret";

            /**
             * 签名过期时间（秒）
             */
            private Integer expireSeconds = 3600;
        }

        /**
         * 防盗链配置
         */
        @Data
        public static class AntiLeechConfig {
            /**
             * 是否启用防盗链
             */
            private Boolean enabled = true;

            /**
             * 允许的Referer（为空表示不检查Referer）
             */
            private List<String> allowedReferers = new ArrayList<>();

            /**
             * 是否允许空Referer
             */
            private Boolean allowEmptyReferer = false;
        }
    }

    /**
     * 处理配置
     */
    @Data
    public static class ProcessConfig {
        /**
         * 图片处理配置
         */
        private ImageProcessConfig image = new ImageProcessConfig();

        /**
         * 视频处理配置
         */
        private VideoProcessConfig video = new VideoProcessConfig();

        /**
         * 图片处理配置
         */
        @Data
        public static class ImageProcessConfig {
            /**
             * 是否启用图片压缩
             */
            private Boolean compressEnabled = true;

            /**
             * 压缩质量（1-100）
             */
            private Integer compressQuality = 85;

            /**
             * 是否生成缩略图
             */
            private Boolean thumbnailEnabled = true;

            /**
             * 缩略图尺寸
             */
            private String thumbnailSize = "200x200";
        }

        /**
         * 视频处理配置
         */
        @Data
        public static class VideoProcessConfig {
            /**
             * 是否启用视频转码
             */
            private Boolean transcodeEnabled = false;
        }
    }

    /**
     * 缓存配置
     */
    @Data
    public static class CacheConfig {
        /**
         * 元数据缓存过期时间（秒）
         */
        private Integer metadataExpireSeconds = 3600;

        /**
         * 访问URL缓存过期时间（秒）
         */
        private Integer urlExpireSeconds = 86400;
    }

    /**
     * 限制配置
     */
    @Data
    public static class LimitConfig {
        /**
         * 租户级别限制
         */
        private TenantLimitConfig tenant = new TenantLimitConfig();

        /**
         * 用户级别限制
         */
        private UserLimitConfig user = new UserLimitConfig();

        /**
         * 租户级别限制
         */
        @Data
        public static class TenantLimitConfig {
            /**
             * 最大文件数量
             */
            private Integer maxFileCount = 10000;

            /**
             * 最大存储空间（字节）
             */
            private Long maxStorageSize = 10737418240L; // 默认10GB
        }

        /**
         * 用户级别限制
         */
        @Data
        public static class UserLimitConfig {
            /**
             * 最大文件数量
             */
            private Integer maxFileCount = 1000;

            /**
             * 最大存储空间（字节）
             */
            private Long maxStorageSize = 1073741824L; // 默认1GB
        }
    }
}
