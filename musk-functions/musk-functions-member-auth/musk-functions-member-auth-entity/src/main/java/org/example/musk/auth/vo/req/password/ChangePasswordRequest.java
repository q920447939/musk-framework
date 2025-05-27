package org.example.musk.auth.vo.req.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改密码请求
 *
 * @author musk
 */
@Data
public class ChangePasswordRequest {

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

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
