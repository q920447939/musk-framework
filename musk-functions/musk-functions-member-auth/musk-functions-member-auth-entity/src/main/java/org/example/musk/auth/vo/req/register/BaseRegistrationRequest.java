package org.example.musk.auth.vo.req.register;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.musk.auth.enums.auth.AuthTypeEnum;
import org.example.musk.auth.enums.member.RegisterChannelEnums;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础注册请求
 *
 * @author musk
 */
@Data
public abstract class BaseRegistrationRequest {

    /**
     * 认证类型
     */
    private AuthTypeEnum authType;

    /**
     * 注册渠道
     */
    @NotNull(message = "注册渠道不能为空")
    private RegisterChannelEnums registerChannel;

    /**
     * 设备信息
     */
    private String deviceInfo;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 会员昵称（可选）
     */
    private String nickname;

    /**
     * 上下文信息
     * 用于传递租户ID、域ID等信息
     */
    private Map<String, Object> context;

    /**
     * 获取上下文信息
     *
     * @return 上下文信息，如果为null则返回空Map
     */
    public Map<String, Object> getContext() {
        if (context == null) {
            context = new HashMap<>();
        }
        return context;
    }

    /**
     * 设置上下文信息
     *
     * @param key 键
     * @param value 值
     */
    public void setContextValue(String key, Object value) {
        getContext().put(key, value);
    }

    /**
     * 获取上下文信息
     *
     * @param key 键
     * @return 值
     */
    public Object getContextValue(String key) {
        return getContext().get(key);
    }

    /**
     * 获取上下文信息（指定类型）
     *
     * @param key 键
     * @param clazz 值类型
     * @param <T> 泛型类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T getContextValue(String key, Class<T> clazz) {
        Object value = getContextValue(key);
        if (value != null && clazz.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
}
