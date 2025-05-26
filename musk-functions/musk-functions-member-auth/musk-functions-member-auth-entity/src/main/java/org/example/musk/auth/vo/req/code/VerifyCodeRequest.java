package org.example.musk.auth.vo.req.code;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.musk.auth.enums.code.CodeChannelEnum;
import org.example.musk.auth.enums.code.CodeSceneEnum;

/**
 * 验证验证码请求
 *
 * @author musk
 */
@Data
public class VerifyCodeRequest {

    /**
     * 目标（邮箱或手机号）
     */
    @NotBlank(message = "邮箱或手机号不能为空")
    private String target;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 4, max = 6, message = "验证码长度必须在4-6位之间")
    @Pattern(regexp = "^\\d{4,6}$", message = "验证码必须为4-6位数字")
    private String code;

    /**
     * 验证码渠道
     */
    @NotNull(message = "验证码渠道不能为空")
    private CodeChannelEnum channel;

    /**
     * 验证码场景
     */
    @NotNull(message = "验证码场景不能为空")
    private CodeSceneEnum scene;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 验证目标格式
     * 根据渠道类型验证邮箱或手机号格式
     */
    public boolean isValidTarget() {
        if (target == null || target.trim().isEmpty()) {
            return false;
        }

        switch (channel) {
            case EMAIL:
                return isValidEmail(target);
            case SMS:
                return isValidPhone(target);
            default:
                return false;
        }
    }

    /**
     * 验证邮箱格式
     *
     * @param email 邮箱地址
     * @return true-格式正确，false-格式错误
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * 验证手机号格式
     *
     * @param phone 手机号
     * @return true-格式正确，false-格式错误
     */
    private boolean isValidPhone(String phone) {
        String phoneRegex = "^1[3-9]\\d{9}$";
        return phone.matches(phoneRegex);
    }
}
