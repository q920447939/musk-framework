package org.example.musk.enums.notice;


import lombok.Getter;

/**
 * 公告类型
 */
@Getter
public enum NoticeTypeEnum {

    POP_UP(1,"弹窗公告"),
    ROLL(2,"滚动公告"),
    ;

    private Integer status;
    private String desc;

    NoticeTypeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
