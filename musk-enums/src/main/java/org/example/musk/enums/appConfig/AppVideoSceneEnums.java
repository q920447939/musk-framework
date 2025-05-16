package org.example.musk.enums.appConfig;

import lombok.Getter;

import java.util.Arrays;


@Getter
public enum AppVideoSceneEnums {

    HOME((short) 1, "首页顶部视频");

    /**
     * 场景
     */
    private final Short scene;
    /**
     * 状态名
     */
    private final String desc;

    AppVideoSceneEnums(Short scene, String desc) {
        this.scene = scene;
        this.desc = desc;
    }

    public static AppVideoSceneEnums getEnumsByScene(short scene){
        return Arrays.stream(AppVideoSceneEnums.values()).filter(k->k.getScene().equals(scene) ).findFirst().orElseThrow(()-> new RuntimeException("未获取到状态"));
    }

}
