package org.example.musk.auth.web.constants.cache;


public class LoginConstant {

    /**
     * 在规定的时间内，允许最大的失败次数
     */
    public static final String LOGIN_STRATEGY_USER_MAX_FAULT_NUM = "LOGIN_STRATEGY_USER_MAX_FAULT_NUM_%s";


    /**
     * 验证代码允许下次登录时间前 key前缀
     */
    public static final String VALIDATE_CODE_ALLOW_NEXT_LOGIN_KEY_PRE = "VALIDATE_CODE_ALLOW_NEXT_LOGIN_KEY_PRE_%s";

    /**
     * 验证码生成key
     */
    public static final String VALIDATE_CODE_GENERATE_KEY = "member:verification_code:%d_%s";
}
