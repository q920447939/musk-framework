package org.example.musk.functions.icon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 图标资源类型枚举
 *
 * @author musk-functions-icon
 */
@Getter
@AllArgsConstructor
public enum IconResourceTypeEnum {

    /**
     * URL类型
     */
    URL(1, "URL类型"),

    /**
     * Base64类型
     */
    BASE64(2, "Base64类型"),

    /**
     * 字体图标类型
     */
    FONT_ICON(3, "字体图标类型");

    /**
     * 资源类型
     */
    private final Integer type;

    /**
     * 资源类型描述
     */
    private final String desc;

    /**
     * 根据类型获取枚举
     *
     * @param type 资源类型
     * @return 枚举
     */
    public static IconResourceTypeEnum getByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (IconResourceTypeEnum typeEnum : IconResourceTypeEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return null;
    }
}
