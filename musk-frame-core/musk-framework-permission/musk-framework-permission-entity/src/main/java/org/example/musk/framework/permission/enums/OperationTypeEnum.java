package org.example.musk.framework.permission.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 操作类型枚举
 * 
 * 定义系统中所有可能的操作类型
 *
 * @author musk-framework-permission
 */
@Getter
public enum OperationTypeEnum {
    
    CREATE("CREATE", "创建操作"),
    READ("READ", "读取操作"),
    UPDATE("UPDATE", "更新操作"),
    DELETE("DELETE", "删除操作"),
    // 可以根据需要扩展更多操作类型
    ;
    
    /**
     * 操作类型编码
     */
    private final String code;
    
    /**
     * 操作类型描述
     */
    private final String desc;
    
    OperationTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    /**
     * 通过编码获取枚举
     *
     * @param code 编码
     * @return 枚举
     */
    public static OperationTypeEnum getByCode(String code) {
        return Arrays.stream(OperationTypeEnum.values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
