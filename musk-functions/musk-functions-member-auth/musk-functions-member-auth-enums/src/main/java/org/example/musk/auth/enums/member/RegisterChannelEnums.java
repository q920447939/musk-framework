package org.example.musk.auth.enums.member;


/**
 * 注册渠道枚举
 */
public enum RegisterChannelEnums {

    /**
     * PC
     */
    PC(1, "PC"),
    /**
     * H5
     */
    H5(2, "H5"),
    /**
     * IOS
     */
    IOS(3, "IOS"),
    /**
     * ANDROID
     */
    ANDROID(4, "ANDROID"),
    /**
     * 小程序
     */
    MINI_PROGRAM(5, "小程序"),
    /**
     * 其他
     */
    OTHER(6, "其他");

    /**
     * 渠道值
     */
    private final Integer value;
    /**
     * 渠道名
     */
    private final String name;

    RegisterChannelEnums(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    /**
     * 通过渠道值，获得对应的枚举
     *
     * @param value 渠道值
     * @return 枚举
     */
    public static RegisterChannelEnums fromValue(Integer value) {
        for (RegisterChannelEnums registerChannel : values()) {
            if (registerChannel.getValue().equals(value)) {
                return registerChannel;
            }
        }
        return null;
    }

}
