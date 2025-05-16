package org.example.musk.enums.member;

/**
 * 用户等级经验值来源类型
 *
 * @author
 * @date 2024/07/08
 */
public enum MemberLevelExperienceSourceEnums {

    INVEST_PROJECT_ADD((short)1, "投资奖励"), //快照id
    DRAW_ADD((short)2, "抽奖奖励"),
    INVEST_PROJECT_CANCEL((short)3, "投资取消扣减"),
    ;

    /**
     * 状态值
     */
    private final Short sourceType;
    /**
     * 状态名
     */
    private final String desc;


    MemberLevelExperienceSourceEnums(Short sourceType, String desc) {
        this.sourceType = sourceType;
        this.desc = desc;
    }

    /**
     * 获得源类型
     *
     * @return {@link Short }
     */
    public Short getSourceType() {
        return sourceType;
    }

    /**
     * 获得desc
     *
     * @return {@link String }
     */
    public String getDesc() {
        return desc;
    }
}
