package org.example.musk.plugin.web.validate.password.impl;

import org.example.musk.common.exception.IBaseErrorInfo;
import org.example.musk.plugin.web.validate.exception.PasswordValidExceptionEnum;
import org.example.musk.plugin.web.validate.password.PasswordCheckNode;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(10)
public class PasswordValidateCheckEmptyNode implements PasswordCheckNode {
    @Override
    public boolean check(String password) {
        return password != null && !password.isEmpty();
    }

    @Override
    public IBaseErrorInfo getPasswordValidException() {
        return PasswordValidExceptionEnum.PASSWORD_IS_EMPTY_ERROR;
    }
}
