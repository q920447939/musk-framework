package org.example.musk.plugin.web.upload.enums;

import lombok.Getter;

/**
 * ClassName: FileTypeEnums
 * @Description:
 * @author
 * @date 2024年12月10日
 */
@Getter
public enum FileTypeEnums {
    IMAGE(1,"图片"),

    ;


    private int fileType;
    private String describe;

    FileTypeEnums(int fileType, String describe) {
        this.fileType = fileType;
        this.describe = describe;
    }
}
