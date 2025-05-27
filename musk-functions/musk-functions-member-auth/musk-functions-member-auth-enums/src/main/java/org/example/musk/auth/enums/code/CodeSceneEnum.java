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
    CHANGE_PHONE("CHANGE_PHONE", "更换手机号", 300),

    /**
     * 修改密码
     */
    CHANGE_PASSWORD("CHANGE_PASSWORD", "修改密码", 300),

    /**
     * 绑定邮箱
     */
    BIND_EMAIL("BIND_EMAIL", "绑定邮箱", 300),

    /**
     * 绑定手机号
     */
    BIND_PHONE("BIND_PHONE", "绑定手机号", 300),

    /**
     * 解绑邮箱
     */
    UNBIND_EMAIL("UNBIND_EMAIL", "解绑邮箱", 300),

    /**
     * 解绑手机号
     */
    UNBIND_PHONE("UNBIND_PHONE", "解绑手机号", 300);

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
        return this == RESET_PASSWORD || this == CHANGE_PASSWORD ||
               this == CHANGE_EMAIL || this == CHANGE_PHONE ||
               this == BIND_EMAIL || this == BIND_PHONE ||
               this == UNBIND_EMAIL || this == UNBIND_PHONE;
    }

    /**
     * 判断是否为密码相关场景
     *
     * @return true-密码相关，false-非密码相关
     */
    public boolean isPasswordRelated() {
        return this == RESET_PASSWORD || this == CHANGE_PASSWORD;
    }

    /**
     * 判断是否为联系方式管理场景
     *
     * @return true-联系方式管理，false-非联系方式管理
     */
    public boolean isContactManagement() {
        return this == CHANGE_EMAIL || this == CHANGE_PHONE ||
               this == BIND_EMAIL || this == BIND_PHONE ||
               this == UNBIND_EMAIL || this == UNBIND_PHONE;
    }

    /**
     * 判断是否为绑定操作场景
     *
     * @return true-绑定操作，false-非绑定操作
     */
    public boolean isBindOperation() {
        return this == BIND_EMAIL || this == BIND_PHONE;
    }

    /**
     * 判断是否为解绑操作场景
     *
     * @return true-解绑操作，false-非解绑操作
     */
    public boolean isUnbindOperation() {
        return this == UNBIND_EMAIL || this == UNBIND_PHONE;
    }
}
