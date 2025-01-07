package org.example.musk.plugin.web.validate.password;

import org.example.musk.common.exception.IBaseErrorInfo;
import org.example.musk.plugin.web.validate.exception.PasswordValidExceptionEnum;

public interface PasswordCheckNode {
    boolean check(String password);

    IBaseErrorInfo getPasswordValidException();
}
