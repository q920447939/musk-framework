package org.example.musk.auth.vo.req.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.musk.auth.enums.code.CodeChannelEnum;

/**
 * 重置密码请求
 *
 * @author musk
 */
@Data
public class ResetPasswordRequest {

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
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 4, max = 8, message = "验证码长度不正确")
    private String verificationCode;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, max = 32, message = "密码长度必须在8-32位之间")
    private String newPassword;

    /**
     * 确认新密码
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}
