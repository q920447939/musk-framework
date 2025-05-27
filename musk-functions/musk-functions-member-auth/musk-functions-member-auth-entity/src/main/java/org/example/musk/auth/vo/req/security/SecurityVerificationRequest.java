package org.example.musk.auth.vo.req.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.musk.auth.enums.SecurityOperationEnum;

/**
 * 安全验证请求
 *
 * @author musk
 */
@Data
public class SecurityVerificationRequest {

    /**
     * 操作类型
     */
    @NotNull(message = "操作类型不能为空")
    private SecurityOperationEnum operationType;

    /**
     * 密码（高敏感度操作需要）
     */
    private String password;

    /**
     * 验证码（某些操作需要）
     */
    private String verificationCode;

    /**
     * 图形验证码会话ID
     */
    private String captchaSessionId;

    /**
     * 图形验证码
     */
    private String captchaCode;

    /**
     * 额外验证信息（JSON格式）
     */
    private String extraInfo;
}
