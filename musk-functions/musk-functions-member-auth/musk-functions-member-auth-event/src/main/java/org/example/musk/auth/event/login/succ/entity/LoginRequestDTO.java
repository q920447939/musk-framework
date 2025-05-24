package org.example.musk.auth.event.login.succ.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDTO {

    private String userName;
    private String password;
    private String verificationCode;

    private String params;
}
