package org.example.musk.functions.resource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 资源类型枚举
 *
 * @author musk-functions-resource
 */
@Getter
@AllArgsConstructor
public enum ResourceTypeEnum {

    /**
     * 图片
     */
    IMAGE(1, "图片"),

    /**
     * 文档
     */
    DOCUMENT(2, "文档"),

    /**
     * 视频
     */
    VIDEO(3, "视频"),

    /**
     * 音频
     */
    AUDIO(4, "音频"),

    /**
     * 压缩包
     */
    ARCHIVE(5, "压缩包"),

    /**
     * 其他
     */
    OTHER(6, "其他");

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
    public static ResourceTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ResourceTypeEnum typeEnum : values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }
}
