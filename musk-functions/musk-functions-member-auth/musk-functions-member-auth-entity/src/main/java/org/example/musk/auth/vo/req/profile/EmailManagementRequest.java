package org.example.musk.auth.vo.req.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.musk.auth.enums.OperationTypeEnum;

/**
 * 邮箱管理请求
 *
 * @author musk
 */
@Data
public class EmailManagementRequest {

    /**
     * 操作类型
     */
    @NotNull(message = "操作类型不能为空")
    private OperationTypeEnum operation;

    /**
     * 新邮箱地址（绑定或更换时必填）
     */
    @Email(message = "邮箱格式不正确")
    private String newEmail;

    /**
     * 当前邮箱地址（更换或解绑时必填）
     */
    @Email(message = "当前邮箱格式不正确")
    private String currentEmail;

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
