package org.example.musk.plugin.web.validate.password.impl;

import jakarta.annotation.Resource;
import org.example.musk.common.exception.IBaseErrorInfo;
import org.example.musk.plugin.web.validate.exception.PasswordValidExceptionEnum;
import org.example.musk.plugin.web.validate.password.PasswordCheckNode;
import org.example.musk.plugin.web.validate.properties.PasswordValidateProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
public class PasswordValidateCheckLengthNode implements PasswordCheckNode {

    @Resource
    private PasswordValidateProperties passwordValidateProperties;

    @Override
    public boolean check(String password) {
        return checkPasswordLength(password);
    }

    /**
     * @return 符合长度要求 返回true
     * @brief 检测密码中字符长度
     * @param[in] password            密码字符串
     */
    public  boolean checkPasswordLength(String password) {
        if ("-1".equals(passwordValidateProperties.getMaxLength())) {
            return password.length() >= Integer.parseInt(passwordValidateProperties.getMinLength());
        } else {
            return password.length() >= Integer.parseInt(passwordValidateProperties.getMinLength()) && password.length() <= Integer
                    .parseInt(passwordValidateProperties.getMaxLength());
        }
    }

    @Override
    public IBaseErrorInfo getPasswordValidException() {
        return new IBaseErrorInfo() {
            @Override
            public String getResultCode() {
                return "4111101";
            }

            @Override
            public String getResultMsg() {
                if ("-1".equals(passwordValidateProperties.getMaxLength())) {
                    return String.format("密码长度不满足要求,密码大于%s个字符",passwordValidateProperties.getMinLength());
                }
                return String.format("密码长度不满足要求,密码大于%s个字符,小于%s个字符",passwordValidateProperties.getMinLength(),passwordValidateProperties.getMaxLength());
            }
        };
    }
}
