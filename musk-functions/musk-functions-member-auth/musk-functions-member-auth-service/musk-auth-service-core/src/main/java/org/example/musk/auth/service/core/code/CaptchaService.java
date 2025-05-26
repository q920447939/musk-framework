package org.example.musk.auth.service.core.code;

/**
 * 图形验证码服务接口
 *
 * @author musk
 */
public interface CaptchaService {

    /**
     * 生成图形验证码
     *
     * @return 验证码信息（包含会话ID和图片Base64）
     */
    CaptchaInfo generateCaptcha();

    /**
     * 验证图形验证码
     *
     * @param sessionId 会话ID
     * @param code 验证码
     * @return true-验证成功，false-验证失败
     */
    boolean verifyCaptcha(String sessionId, String code);

    /**
     * 清除验证码
     *
     * @param sessionId 会话ID
     */
    void clearCaptcha(String sessionId);

    /**
     * 验证码信息
     */
    class CaptchaInfo {
        /**
         * 会话ID
         */
        private String sessionId;

        /**
         * 图片Base64编码
         */
        private String imageBase64;

        /**
         * 过期时间（秒）
         */
        private int expireSeconds;

        public CaptchaInfo(String sessionId, String imageBase64, int expireSeconds) {
            this.sessionId = sessionId;
            this.imageBase64 = imageBase64;
            this.expireSeconds = expireSeconds;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getImageBase64() {
            return imageBase64;
        }

        public void setImageBase64(String imageBase64) {
            this.imageBase64 = imageBase64;
        }

        public int getExpireSeconds() {
            return expireSeconds;
        }

        public void setExpireSeconds(int expireSeconds) {
            this.expireSeconds = expireSeconds;
        }
    }
}
