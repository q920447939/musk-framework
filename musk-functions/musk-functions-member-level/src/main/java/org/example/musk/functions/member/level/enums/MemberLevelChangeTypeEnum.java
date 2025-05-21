package org.example.musk.functions.member.level.enums;

import lombok.Getter;

/**
 * 会员等级变更类型枚举
 *
 * @author musk-functions-member-level
 */
@Getter
public enum MemberLevelChangeTypeEnum {
    
    /**
     * 升级
     */
    UPGRADE(1, "升级"),
    
    /**
     * 降级
     */
    DOWNGRADE(2, "降级"),
    
    /**
     * 初始化
     */
    INITIALIZE(3, "初始化"),
    
    /**
     * 手动设置
     */
    MANUAL_SET(4, "手动设置");
    
    private final Integer value;
    private final String name;
    
    MemberLevelChangeTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }
    
    /**
     * 通过值获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static MemberLevelChangeTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (MemberLevelChangeTypeEnum type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
