package org.example.musk.plugin.web.validate.password.impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.example.musk.common.exception.IBaseErrorInfo;
import org.example.musk.plugin.web.validate.exception.PasswordValidExceptionEnum;
import org.example.musk.plugin.web.validate.password.PasswordCheckNode;
import org.example.musk.plugin.web.validate.properties.PasswordValidateProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(30)
public class PasswordValidateCheckSimpleWordNode implements PasswordCheckNode {

    @Resource
    private PasswordValidateProperties passwordValidateProperties;

    List<String> simpleWords;


    @PostConstruct
    public void init(){
        simpleWords = Arrays.asList(passwordValidateProperties.getSimpleWords());
    }

    @Override
    public boolean check(String password) {
        return checkSimpleWord(password);
    }

    /**
     * 检测常用词库
     *
     * @param password
     * @return
     */
    public  boolean checkSimpleWord(String password) {
        return !simpleWords.contains(password.toLowerCase());
    }

    @Override
    public IBaseErrorInfo getPasswordValidException() {
        return PasswordValidExceptionEnum.PASSWORD_IS_SIMPLE_WORD_ERROR;
    }
}
