package org.example.musk.enums;


import lombok.Getter;

@Getter
public enum NavigationTagEnum {

    BOTTOM_NAVIGATOR("bottom_navigator","底部导航"),
    MEMBER_NAVIGATOR_2("member_navigation_2","用户首页导航2"),
    HOME_TOP_NAVIGATION("home_top_navigation","首页顶部导航"),
    ;

    private String code;
    private String desc;

    NavigationTagEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static NavigationTagEnum getNavigationTagEnumByCode(String code){
        assert  null != code;

        for (NavigationTagEnum value : NavigationTagEnum.values()) {
            if (code.equals(value.code)) {
                return value;
            }
        }
        return null;
    }
}
