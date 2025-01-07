package org.example.musk.plugin.web.validate.password;

import jakarta.annotation.Resource;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.plugin.web.validate.config.MuskPasswordValidateConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PasswordCheckHelper {

    @Resource
    private List<PasswordCheckNode> passwordCheckNodeList;

    public void check(String password) {
        for (PasswordCheckNode passwordCheckNode : passwordCheckNodeList) {
            if (!passwordCheckNode.check(password)) {
                throw new BusinessException(passwordCheckNode.getPasswordValidException());
            }
        }
    }
}
