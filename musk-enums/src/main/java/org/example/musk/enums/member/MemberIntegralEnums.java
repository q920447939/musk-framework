package org.example.musk.enums.member;

/**
 * 会员积分 增加、减少
 */
public enum MemberIntegralEnums {

    ADD(1, "增加"),
    REDUCE(2, "减少"),
    ;


    private final Integer value;

    private final String name;

    MemberIntegralEnums(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}

