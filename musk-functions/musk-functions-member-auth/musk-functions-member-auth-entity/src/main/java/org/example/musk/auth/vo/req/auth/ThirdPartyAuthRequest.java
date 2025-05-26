package org.example.musk.auth.vo.req.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.musk.auth.enums.thirdparty.ThirdPartyTypeEnum;

/**
 * 第三方认证请求
 *
 * @author musk
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ThirdPartyAuthRequest extends BaseAuthRequest {

    /**
     * 第三方类型
     */
    @NotNull(message = "第三方类型不能为空")
    private ThirdPartyTypeEnum thirdPartyType;

    /**
     * 授权码
     * 第三方平台返回的授权码
     */
    @NotBlank(message = "授权码不能为空")
    private String authCode;

    /**
     * 状态码
     * 防CSRF攻击的状态码
     */
    @NotBlank(message = "状态码不能为空")
    private String state;

    /**
     * 重定向URI
     * 第三方登录成功后的重定向地址
     */
    @NotBlank(message = "重定向URI不能为空")
    private String redirectUri;
}
