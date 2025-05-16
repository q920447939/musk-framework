package org.example.musk.enums;


import lombok.Getter;

@Getter
public enum ArticleTagGroupEnum {

    TAG_ARTICLE_HOME("tag_article_home","首页文章标签"),
    ;

    private String code;
    private String desc;

    ArticleTagGroupEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
