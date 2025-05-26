package org.example.musk.auth.vo.req;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequestDTO {

    private String userName;

    private String password;

    private String nickName;


    private int inviteMemberSimpleId;

    @NotBlank(message = "验证码不能为空")
    @Size(min = 4,max = 4)
    private String verificationCode;
}
