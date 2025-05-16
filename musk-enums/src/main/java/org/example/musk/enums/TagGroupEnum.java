package org.example.musk.enums;


import lombok.Getter;

@Getter
public enum TagGroupEnum {

    TAG_SVG("tag_svg","默认图标组"),
    TAG_NAVIGATION("tag_navigation","导航图标组"),
    ;

    private String code;
    private String desc;

    TagGroupEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
