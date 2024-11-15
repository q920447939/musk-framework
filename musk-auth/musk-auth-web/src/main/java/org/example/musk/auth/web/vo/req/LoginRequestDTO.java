package org.example.musk.auth.web.vo.req;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDTO {

    private String userName;
    private String password;

    @NotBlank(message = "验证码不能为空")
    @Min(4)
    @Max(5)
    private String verificationCode;
}
