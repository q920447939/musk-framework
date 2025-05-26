package org.example.musk.auth.vo.req.register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户名密码注册请求
 *
 * @author musk
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UsernamePasswordRegisterRequest extends BaseRegistrationRequest {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 6, max = 30, message = "用户名长度必须在6-30位之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度必须在8-20位之间")
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    /**
     * 图形验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 4, max = 6, message = "验证码长度必须在4-6位之间")
    private String captcha;

    /**
     * 验证码会话ID
     */
    @NotBlank(message = "验证码会话ID不能为空")
    private String captchaSessionId;

    /**
     * 验证密码一致性
     *
     * @return true-密码一致，false-密码不一致
     */
    public boolean isPasswordMatch() {
        return password != null && password.equals(confirmPassword);
    }

    /**
     * 验证用户名格式
     *
     * @return true-格式正确，false-格式错误
     */
    public boolean isValidUsername() {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        // 用户名不能以数字开头
        return !Character.isDigit(username.charAt(0));
    }
}
