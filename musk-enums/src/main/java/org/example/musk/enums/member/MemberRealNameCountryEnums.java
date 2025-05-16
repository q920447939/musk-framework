package org.example.musk.enums.member;

/**
 * 会员实名认证国家
 */
public enum MemberRealNameCountryEnums {
    CHINA(1, "中国"),
    ;

    /**
     * 状态值
     */
    private final Integer code;
    /**
     * 状态名
     */
    private final String desc;

    MemberRealNameCountryEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static MemberRealNameCountryEnums getByValue(Integer code) {
        for (MemberRealNameCountryEnums enums : MemberRealNameCountryEnums.values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }
        return null;
    }

}
