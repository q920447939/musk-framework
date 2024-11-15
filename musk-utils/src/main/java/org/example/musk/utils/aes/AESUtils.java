package org.example.musk.utils.aes;


import cn.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.crypto.SecureUtil;
import org.dromara.hutool.crypto.symmetric.AES;
import org.dromara.hutool.crypto.symmetric.SymmetricAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AESUtils {

    private static final Map<AESKeyEnum,AES> AES_KEY_ENUM_AES_MAP = new HashMap<>();
    static {
        for (AESKeyEnum aesKeyEnum : AESKeyEnum.values()) {
            byte[] key = cn.hutool.crypto.SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), "c70d9add55ce4b90bfe1a289fd2cc4cf".getBytes(StandardCharsets.UTF_8)).getEncoded();
            AES aes = SecureUtil.aes(key);
            AES_KEY_ENUM_AES_MAP.put(aesKeyEnum,aes);
        }
    }


    public static String encryptHex(String content,AESKeyEnum aesKeyEnum) {
        if (StrUtil.isBlank(content)) {
            return StrUtil.EMPTY;
        }
        return  AES_KEY_ENUM_AES_MAP.get(aesKeyEnum).encryptHex(content);
    }

    public static String encryptHex(Integer content,AESKeyEnum aesKeyEnum) {
        return  AES_KEY_ENUM_AES_MAP.get(aesKeyEnum).encryptHex(String.valueOf(content));
    }

    public static String decryptStr(String encryptHex,AESKeyEnum aesKeyEnum) {
        return  AES_KEY_ENUM_AES_MAP.get(aesKeyEnum).decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
    }

}
