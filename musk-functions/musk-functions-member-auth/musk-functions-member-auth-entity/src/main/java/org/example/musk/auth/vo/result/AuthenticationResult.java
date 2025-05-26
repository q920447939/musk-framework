package org.example.musk.auth.vo.result;

import lombok.Builder;
import lombok.Data;
import org.example.musk.auth.entity.member.MemberDO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证结果
 *
 * @author musk
 */
@Data
@Builder
public class AuthenticationResult {

    /**
     * 认证是否成功
     */
    private boolean success;

    /**
     * 会员信息
     */
    private MemberDO member;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 认证时间
     */
    private LocalDateTime authTime;

    /**
     * 扩展信息
     */
    private Map<String, Object> extraInfo;

    /**
     * 创建成功结果
     *
     * @param member 会员信息
     * @return 认证结果
     */
    public static AuthenticationResult success(MemberDO member) {
        return AuthenticationResult.builder()
                .success(true)
                .member(member)
                .authTime(LocalDateTime.now())
                .extraInfo(new HashMap<>())
                .build();
    }

    /**
     * 创建失败结果
     *
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     * @return 认证结果
     */
    public static AuthenticationResult failure(String errorCode, String errorMessage) {
        return AuthenticationResult.builder()
                .success(false)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .authTime(LocalDateTime.now())
                .extraInfo(new HashMap<>())
                .build();
    }

    /**
     * 获取扩展信息
     *
     * @return 扩展信息，如果为null则返回空Map
     */
    public Map<String, Object> getExtraInfo() {
        if (extraInfo == null) {
            extraInfo = new HashMap<>();
        }
        return extraInfo;
    }

    /**
     * 设置扩展信息
     *
     * @param key 键
     * @param value 值
     */
    public void setExtraInfo(String key, Object value) {
        getExtraInfo().put(key, value);
    }

    /**
     * 获取扩展信息
     *
     * @param key 键
     * @return 值
     */
    public Object getExtraInfo(String key) {
        return getExtraInfo().get(key);
    }

    /**
     * 获取扩展信息（指定类型）
     *
     * @param key 键
     * @param clazz 值类型
     * @param <T> 泛型类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T getExtraInfo(String key, Class<T> clazz) {
        Object value = getExtraInfo(key);
        if (value != null && clazz.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
}
