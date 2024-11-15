package org.example.musk.utils.aes;

import lombok.Getter;


/**
 * 項目状态枚举
 */
@Getter
public enum AESKeyEnum {
    TENANT_KEY("c70d9add55ce4b90bfe1a289fd2cc4cf","AES加密-租户ID"),
    PASSWORD_KEY("bc4322c22608830ea887535b5dada522","AES加密-密码"),
    ;

    private String key;
    private String desc;

    AESKeyEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
