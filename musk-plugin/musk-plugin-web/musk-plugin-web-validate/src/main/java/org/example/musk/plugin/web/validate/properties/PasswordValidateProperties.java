package org.example.musk.plugin.web.validate.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "musk.plugin.web.validate.password")
@Setter
@Getter
public class PasswordValidateProperties {

    private boolean enable = true;


    /**
     * 密码最小长度，默认为8
     */
    private String minLength = "8";

    /**
     * 密码最大长度，默认为20
     */
    private String maxLength = "20";

    /**
     * 特殊符号集合
     */
    private String specialChar = "!\\\"#$%&'()*+,-./:;<=>?@[\\\\]^_`{|}~";

    /**
     * 常用词库
     */
    private String[] simpleWords = {"admin", "szim", "epicrouter", "password", "grouter", "dare", "root", "guest",
            "user", "success", "pussy", "mustang", "fuckme", "jordan", "test", "hunter", "jennifer", "batman", "thomas",
            "soccer", "sexy", "killer", "george", "asshole", "fuckyou", "summer", "hello", "secret", "fucker", "enter",
            "cookie", "administrator",
            // 中国网民常用密码
            "xiaoming", "taobao", "iloveyou", "woaini", "982464",
            // 国外网民常用密码
            "monkey", "letmein", "trustno1", "dragon", "baseball", "master", "sunshine", "ashley", "bailey", "shadow",
            "superman", "football", "michael", "qazwsx"};

}
