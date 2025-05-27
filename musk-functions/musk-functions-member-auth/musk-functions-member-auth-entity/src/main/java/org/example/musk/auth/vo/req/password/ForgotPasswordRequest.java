package org.example.musk.auth.vo.req.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.musk.auth.enums.code.CodeChannelEnum;

/**
 * 忘记密码请求
 *
 * @author musk
 */
@Data
public class ForgotPasswordRequest {

    /**
     * 标识符（邮箱或手机号）
     */
    @NotBlank(message = "邮箱或手机号不能为空")
    private String identifier;

    /**
     * 验证码渠道
     */
    @NotNull(message = "验证码渠道不能为空")
    private CodeChannelEnum channel;

    /**
     * 图形验证码会话ID
     */
    @NotBlank(message = "验证码会话ID不能为空")
    private String captchaSessionId;

    /**
     * 图形验证码
     */
    @NotBlank(message = "图形验证码不能为空")
    private String captchaCode;
}
