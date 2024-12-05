/**
 * @Project:
 * @Author:
 * @Date: 2023年07月10日
 */
package org.example.musk.auth.web.loginChain.entity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * 登录请求链
 * ClassName: LoginChainRequest
 *
 * @author
 * @Description:
 * @date 2023年07月10日
 */
@Data
public class LoginChainRequest {
    /**
     * 请求
     */
    private HttpServletRequest request;
    /**
     * 用户名
     */
    @NotNull
    @Length(min = 4,max = 50)
    private String username;
    /**
     * 密码
     */
    @NotNull
    @Length(min = 4,max = 50)
    private String password;
    /**
     * 验证码输入
     */
    private String validateCodeInput;
    /**
     * 跳过验证码标志
     */
    private boolean skipValidateCode;

}
