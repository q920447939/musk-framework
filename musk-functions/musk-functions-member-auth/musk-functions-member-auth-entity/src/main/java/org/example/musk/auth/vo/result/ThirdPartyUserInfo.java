package org.example.musk.auth.vo.result;

import lombok.Builder;
import lombok.Data;
import org.example.musk.auth.enums.thirdparty.ThirdPartyTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 第三方用户信息
 *
 * @author musk
 */
@Data
@Builder
public class ThirdPartyUserInfo {

    /**
     * 第三方类型
     */
    private ThirdPartyTypeEnum thirdPartyType;

    /**
     * 第三方用户唯一标识
     */
    private String openId;

    /**
     * 第三方用户联合标识
     * 用于同一开发者账号下的不同应用
     */
    private String unionId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别
     * 1-男，2-女，0-未知
     */
    private Integer gender;

    /**
     * 原始用户信息
     * 第三方平台返回的完整用户信息JSON
     */
    private Map<String, Object> rawUserInfo;

    /**
     * 获取原始用户信息
     *
     * @return 原始用户信息，如果为null则返回空Map
     */
    public Map<String, Object> getRawUserInfo() {
        if (rawUserInfo == null) {
            rawUserInfo = new HashMap<>();
        }
        return rawUserInfo;
    }

    /**
     * 设置原始用户信息
     *
     * @param key 键
     * @param value 值
     */
    public void setRawUserInfo(String key, Object value) {
        getRawUserInfo().put(key, value);
    }

    /**
     * 获取原始用户信息
     *
     * @param key 键
     * @return 值
     */
    public Object getRawUserInfo(String key) {
        return getRawUserInfo().get(key);
    }

    /**
     * 获取原始用户信息（指定类型）
     *
     * @param key 键
     * @param clazz 值类型
     * @param <T> 泛型类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T getRawUserInfo(String key, Class<T> clazz) {
        Object value = getRawUserInfo(key);
        if (value != null && clazz.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    /**
     * 获取显示名称
     * 优先使用昵称，如果昵称为空则使用openId
     *
     * @return 显示名称
     */
    public String getDisplayName() {
        if (nickname != null && !nickname.trim().isEmpty()) {
            return nickname;
        }
        return openId;
    }

    /**
     * 判断是否有邮箱信息
     *
     * @return true-有邮箱，false-无邮箱
     */
    public boolean hasEmail() {
        return email != null && !email.trim().isEmpty();
    }

    /**
     * 判断是否有手机号信息
     *
     * @return true-有手机号，false-无手机号
     */
    public boolean hasPhone() {
        return phone != null && !phone.trim().isEmpty();
    }
}
