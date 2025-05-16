package org.example.musk.enums.appConfig;

import lombok.Getter;

import java.util.Arrays;


@Getter
public enum AppVideoFileTypeEnums {

    LOCAL((short) 1, "本地视频文件"),
    EXTERNAL((short) 2, "外链视频文件");

    /**
     * 场景
     */
    private final Short fileType;
    /**
     * 状态名
     */
    private final String desc;

    AppVideoFileTypeEnums(Short fileType, String desc) {
        this.fileType = fileType;
        this.desc = desc;
    }

    public static AppVideoFileTypeEnums getEnums(short fileType) {
        return Arrays.stream(AppVideoFileTypeEnums.values()).filter(k -> k.getFileType().equals(fileType)).findFirst().orElseThrow(() -> new RuntimeException("未获取到状态"));
    }

}
