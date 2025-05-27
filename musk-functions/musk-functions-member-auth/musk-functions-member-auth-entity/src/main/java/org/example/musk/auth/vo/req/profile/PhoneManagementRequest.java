package org.example.musk.auth.vo.req.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.musk.auth.enums.OperationTypeEnum;

/**
 * 手机号管理请求
 *
 * @author musk
 */
@Data
public class PhoneManagementRequest {

    /**
     * 操作类型
     */
    @NotNull(message = "操作类型不能为空")
    private OperationTypeEnum operation;

    /**
     * 新手机号（绑定或更换时必填）
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String newPhone;

    /**
     * 当前手机号（更换或解绑时必填）
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "当前手机号格式不正确")
    private String currentPhone;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 4, max = 8, message = "验证码长度不正确")
    private String verificationCode;

    /**
     * 密码（敏感操作需要密码确认）
     */
    private String password;

    /**
     * 图形验证码会话ID
     */
    private String captchaSessionId;

    /**
     * 图形验证码
     */
    private String captchaCode;
}
