package org.example.musk.enums;


import lombok.Getter;

@Getter
public enum ArticleStatusEnum {

    RELEASE(1,"发布"),
    DRAFT(2,"草稿"),
    ;

    private Integer status;
    private String desc;

    ArticleStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
