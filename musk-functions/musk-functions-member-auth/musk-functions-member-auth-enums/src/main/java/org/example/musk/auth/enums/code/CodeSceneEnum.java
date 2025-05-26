package org.example.musk.auth.enums.code;

import lombok.Getter;

/**
 * 验证码场景枚举
 * 
 * @author musk
 */
@Getter
public enum CodeSceneEnum {

    /**
     * 登录
     */
    LOGIN("LOGIN", "登录", 300),

    /**
     * 注册
     */
    REGISTER("REGISTER", "注册", 600),

    /**
     * 绑定
     */
    BIND("BIND", "绑定", 300),

    /**
     * 重置密码
     */
    RESET_PASSWORD("RESET_PASSWORD", "重置密码", 600),

    /**
     * 更换邮箱
     */
    CHANGE_EMAIL("CHANGE_EMAIL", "更换邮箱", 300),

    /**
     * 更换手机号
     */
    CHANGE_PHONE("CHANGE_PHONE", "更换手机号", 300);

    /**
     * 场景编码
     */
    private final String code;

    /**
     * 场景描述
     */
    private final String desc;

    /**
     * 默认过期时间（秒）
     */
    private final Integer defaultExpireSeconds;

    CodeSceneEnum(String code, String desc, Integer defaultExpireSeconds) {
        this.code = code;
        this.desc = desc;
        this.defaultExpireSeconds = defaultExpireSeconds;
    }

    /**
     * 根据编码获取枚举
     *
     * @param code 场景编码
     * @return 对应的枚举，如果不存在则返回null
     */
    public static CodeSceneEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (CodeSceneEnum scene : values()) {
            if (scene.getCode().equals(code)) {
                return scene;
            }
        }
        return null;
    }

    /**
     * 判断是否为注册相关场景
     *
     * @return true-注册相关，false-非注册相关
     */
    public boolean isRegisterRelated() {
        return this == REGISTER;
    }

    /**
     * 判断是否为登录相关场景
     *
     * @return true-登录相关，false-非登录相关
     */
    public boolean isLoginRelated() {
        return this == LOGIN;
    }

    /**
     * 判断是否为安全操作场景
     *
     * @return true-安全操作，false-非安全操作
     */
    public boolean isSecurityOperation() {
        return this == RESET_PASSWORD || this == CHANGE_EMAIL || this == CHANGE_PHONE;
    }
}
