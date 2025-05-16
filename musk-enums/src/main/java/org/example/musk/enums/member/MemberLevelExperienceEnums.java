package org.example.musk.enums.member;

/**
 * 会员等级 经验值 增加或减少
 */
public enum MemberLevelExperienceEnums {
    ADD(1, "增加"),
    REDUCE(2, "减少"),
    ;


    private final Integer value;

    private final String name;

    MemberLevelExperienceEnums(Integer value, String name) {
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

