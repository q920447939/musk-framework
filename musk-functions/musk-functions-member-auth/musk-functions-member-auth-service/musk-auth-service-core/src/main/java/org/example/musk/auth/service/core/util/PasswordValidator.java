package org.example.musk.auth.service.core.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.service.core.config.AuthenticationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 密码强度校验工具
 *
 * @author musk
 */
@Component
@Slf4j
public class PasswordValidator {

    @Autowired
    private AuthenticationConfig authConfig;

    /**
     * 验证密码是否符合要求
     *
     * @param password 密码
     * @return true-符合要求，false-不符合要求
     */
    public boolean isValidPassword(String password) {
        if (StrUtil.isBlank(password)) {
            return false;
        }

        AuthenticationConfig.PasswordConfig config = authConfig.getPassword();

        // 检查长度
        if (password.length() < config.getMinLength()) {
            return false;
        }

        // 检查是否包含大写字母
        if (config.getRequireUppercase() && !containsUppercase(password)) {
            return false;
        }

        // 检查是否包含小写字母
        if (config.getRequireLowercase() && !containsLowercase(password)) {
            return false;
        }

        // 检查是否包含数字
        if (config.getRequireDigit() && !containsDigit(password)) {
            return false;
        }

        // 检查是否包含特殊字符
        if (config.getRequireSpecialChar() && !containsSpecialChar(password)) {
            return false;
        }

        return true;
    }

    /**
     * 获取密码要求描述
     *
     * @return 密码要求描述
     */
    public String getPasswordRequirements() {
        AuthenticationConfig.PasswordConfig config = authConfig.getPassword();
        List<String> requirements = new ArrayList<>();

        requirements.add("密码长度至少" + config.getMinLength() + "位");

        if (config.getRequireUppercase()) {
            requirements.add("包含大写字母");
        }

        if (config.getRequireLowercase()) {
            requirements.add("包含小写字母");
        }

        if (config.getRequireDigit()) {
            requirements.add("包含数字");
        }

        if (config.getRequireSpecialChar()) {
            requirements.add("包含特殊字符");
        }

        return "密码必须" + String.join("、", requirements);
    }

    /**
     * 检查是否包含大写字母
     *
     * @param password 密码
     * @return true-包含，false-不包含
     */
    private boolean containsUppercase(String password) {
        return Pattern.compile("[A-Z]").matcher(password).find();
    }

    /**
     * 检查是否包含小写字母
     *
     * @param password 密码
     * @return true-包含，false-不包含
     */
    private boolean containsLowercase(String password) {
        return Pattern.compile("[a-z]").matcher(password).find();
    }

    /**
     * 检查是否包含数字
     *
     * @param password 密码
     * @return true-包含，false-不包含
     */
    private boolean containsDigit(String password) {
        return Pattern.compile("\\d").matcher(password).find();
    }

    /**
     * 检查是否包含特殊字符
     *
     * @param password 密码
     * @return true-包含，false-不包含
     */
    private boolean containsSpecialChar(String password) {
        return Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]").matcher(password).find();
    }

    /**
     * 获取密码强度等级
     *
     * @param password 密码
     * @return 强度等级 1-弱 2-中 3-强
     */
    public int getPasswordStrength(String password) {
        if (StrUtil.isBlank(password)) {
            return 0;
        }

        int score = 0;

        // 长度评分
        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;

        // 字符类型评分
        if (containsUppercase(password)) score++;
        if (containsLowercase(password)) score++;
        if (containsDigit(password)) score++;
        if (containsSpecialChar(password)) score++;

        // 转换为1-3等级
        if (score <= 2) return 1; // 弱
        if (score <= 4) return 2; // 中
        return 3; // 强
    }
}
