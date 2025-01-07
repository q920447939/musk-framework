package org.example.musk.plugin.web.validate.config;

import cn.hutool.extra.spring.SpringUtil;
import org.example.musk.plugin.web.validate.password.PasswordCheckNode;
import org.example.musk.plugin.web.validate.password.impl.PasswordValidateCheckEmptyNode;
import org.example.musk.plugin.web.validate.password.impl.PasswordValidateCheckLengthNode;
import org.example.musk.plugin.web.validate.password.impl.PasswordValidateCheckSimpleWordNode;
import org.example.musk.plugin.web.validate.password.impl.PasswordValidateCheckSpecialCharNode;
import org.example.musk.plugin.web.validate.properties.PasswordValidateProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;


@AutoConfiguration
@EnableConfigurationProperties({PasswordValidateProperties.class})
//@ConditionalOnProperty(prefix = "musk.plugin.web.validate.password", name = "enable", havingValue = "true",matchIfMissing = true)
public class MuskPasswordValidateConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "passwordCheckNodeList")
    public List<PasswordCheckNode> passwordCheckNodeList() {
        return Arrays.asList(SpringUtil.getBean(PasswordValidateCheckEmptyNode.class)
                , SpringUtil.getBean(PasswordValidateCheckLengthNode.class)
                , SpringUtil.getBean(PasswordValidateCheckSimpleWordNode.class)
                /*, SpringUtil.getBean(PasswordValidateCheckSpecialCharNode.class)*/
        );
    }
}
