package org.example.musk.plugin.web.validate.password.impl;

import jakarta.annotation.Resource;
import org.example.musk.common.exception.IBaseErrorInfo;
import org.example.musk.plugin.web.validate.exception.PasswordValidExceptionEnum;
import org.example.musk.plugin.web.validate.password.PasswordCheckNode;
import org.example.musk.plugin.web.validate.properties.PasswordValidateProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(40)
public class PasswordValidateCheckSpecialCharNode implements PasswordCheckNode {


    @Resource
    private PasswordValidateProperties passwordValidateProperties;

    @Override
    public boolean check(String password) {
        return checkContainSpecialChar(password);
    }

    /**
     * @return 包含特殊符号 返回true
     * @brief 检测密码中是否包含特殊符号
     * @param[in] password            密码字符串
     */
    public boolean checkContainSpecialChar(String password) {
        char[] chPass = password.toCharArray();
        int special_count = 0;

        for (char pass : chPass) {
            if (passwordValidateProperties.getSpecialChar().indexOf(pass) != -1) {
                special_count++;
            }
        }

        return special_count < 1;
    }

    @Override
    public IBaseErrorInfo getPasswordValidException() {
        return PasswordValidExceptionEnum.PASSWORD_IS_CONTAIN_SPECIAL_CHAR_ERROR;
    }
}
