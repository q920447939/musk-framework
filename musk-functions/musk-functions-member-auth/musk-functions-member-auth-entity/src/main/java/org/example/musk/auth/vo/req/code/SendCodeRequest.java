package org.example.musk.auth.vo.req.code;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.musk.auth.enums.code.CodeChannelEnum;
import org.example.musk.auth.enums.code.CodeSceneEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送验证码请求
 *
 * @author musk
 */
@Data
public class SendCodeRequest {

    /**
     * 目标（邮箱或手机号）
     */
    @NotBlank(message = "邮箱或手机号不能为空")
    private String target;

    /**
     * 验证码渠道
     */
    @NotNull(message = "验证码渠道不能为空")
    private CodeChannelEnum channel;

    /**
     * 验证码场景
     */
    @NotNull(message = "验证码场景不能为空")
    private CodeSceneEnum scene;

    /**
     * 模板ID
     * 用于指定发送验证码的模板
     */
    private String templateId;

    /**
     * 模板参数
     * 用于模板变量替换
     */
    private Map<String, Object> templateParams;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 获取模板参数
     *
     * @return 模板参数，如果为null则返回空Map
     */
    public Map<String, Object> getTemplateParams() {
        if (templateParams == null) {
            templateParams = new HashMap<>();
        }
        return templateParams;
    }

    /**
     * 设置模板参数
     *
     * @param key 键
     * @param value 值
     */
    public void setTemplateParam(String key, Object value) {
        getTemplateParams().put(key, value);
    }

    /**
     * 获取模板参数
     *
     * @param key 键
     * @return 值
     */
    public Object getTemplateParam(String key) {
        return getTemplateParams().get(key);
    }

    /**
     * 验证目标格式
     * 根据渠道类型验证邮箱或手机号格式
     */
    public boolean isValidTarget() {
        if (target == null || target.trim().isEmpty()) {
            return false;
        }

        switch (channel) {
            case EMAIL:
                return isValidEmail(target);
            case SMS:
                return isValidPhone(target);
            default:
                return false;
        }
    }

    /**
     * 验证邮箱格式
     *
     * @param email 邮箱地址
     * @return true-格式正确，false-格式错误
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * 验证手机号格式
     *
     * @param phone 手机号
     * @return true-格式正确，false-格式错误
     */
    private boolean isValidPhone(String phone) {
        String phoneRegex = "^1[3-9]\\d{9}$";
        return phone.matches(phoneRegex);
    }
}
