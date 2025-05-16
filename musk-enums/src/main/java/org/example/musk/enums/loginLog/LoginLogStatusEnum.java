package org.example.musk.enums.loginLog;


import lombok.Getter;

@Getter
public enum LoginLogStatusEnum {

    SUCC(1,"登录成功"),
    FAIL(2,"登录失败"),
    ;

    private Integer status;
    private String desc;

    LoginLogStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
