package org.example.musk.functions.resource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 存储类型枚举
 *
 * @author musk-functions-resource
 */
@Getter
@AllArgsConstructor
public enum StorageTypeEnum {

    /**
     * 本地存储
     */
    LOCAL(1, "本地存储"),

    /**
     * 阿里云OSS
     */
    ALIYUN_OSS(2, "阿里云OSS"),

    /**
     * 腾讯云COS
     */
    TENCENT_COS(3, "腾讯云COS");

    /**
     * 类型编码
     */
    private final Integer code;

    /**
     * 类型描述
     */
    private final String description;

    /**
     * 根据编码获取枚举
     *
     * @param code 编码
     * @return 枚举
     */
    public static StorageTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (StorageTypeEnum typeEnum : values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }
}
