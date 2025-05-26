package org.example.musk.auth.vo.req.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户名密码认证请求
 *
 * @author musk
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UsernamePasswordAuthRequest extends BaseAuthRequest {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 6, max = 30, message = "用户名长度必须在6-30位之间")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 图形验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 4, max = 6, message = "验证码长度必须在4-6位之间")
    private String captcha;
}
