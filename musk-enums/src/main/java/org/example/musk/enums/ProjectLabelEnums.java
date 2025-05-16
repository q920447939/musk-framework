package org.example.musk.enums;


import lombok.Getter;

@Getter
public enum ProjectLabelEnums {
    ALLOW_USE_VOUCHER("可使用代金券"),
    GIVE_VOUCHER("赠送代金券"),
    GIVE_INTEGRAL("赠送积分"),
    GIVE_EXPERIENCE("经验值奖励"),
    BROKERAGE_ENABLE("分佣奖励"),
    TEAM_ENABLE("团队长奖励"),
    ;

    private String label;


    ProjectLabelEnums(String label) {
        this.label = label;
    }
}
