package org.example.musk.framework.permission.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 资源类型枚举
 * 
 * 定义系统中所有需要进行权限控制的资源类型
 *
 * @author musk-framework-permission
 */
@Getter
public enum ResourceTypeEnum {
    
    MENU("MENU", "菜单模块"),
    SYSTEM_PARAMS("SYSTEM_PARAMS", "系统参数模块"),
    // 可以根据需要扩展更多资源类型
    ;
    
    /**
     * 资源类型编码
     */
    private final String code;
    
    /**
     * 资源类型描述
     */
    private final String desc;
    
    ResourceTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    /**
     * 通过编码获取枚举
     *
     * @param code 编码
     * @return 枚举
     */
    public static ResourceTypeEnum getByCode(String code) {
        return Arrays.stream(ResourceTypeEnum.values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
