package org.example.musk.auth.service.core.helper;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.service.core.config.AuthenticationConfig;
import org.example.musk.auth.vo.result.PasswordValidationResult;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 密码验证工具类
 *
 * @author musk
 */
@Component
@Slf4j
public class PasswordValidator {

    @Resource
    private AuthenticationConfig authConfig;

    /**
     * 大写字母正则
     */
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");

    /**
     * 小写字母正则
     */
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");

    /**
     * 数字正则
     */
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");

    /**
     * 特殊字符正则
     */
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]");

    /**
     * 验证密码强度
     *
     * @param password 密码
     * @return 验证结果
     */
    public PasswordValidationResult validatePassword(String password) {
        log.debug("[密码验证] 开始验证密码强度");

        if (StrUtil.isBlank(password)) {
            return PasswordValidationResult.failure(
                    List.of("密码不能为空"),
                    List.of("请输入密码")
            );
        }

        AuthenticationConfig.PasswordConfig passwordConfig = authConfig.getPassword();
        List<String> failureReasons = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();

        // 长度检查
        if (password.length() < passwordConfig.getMinLength()) {
            failureReasons.add("密码长度不能少于" + passwordConfig.getMinLength() + "位");
            suggestions.add("请增加密码长度至" + passwordConfig.getMinLength() + "位以上");
        }

        if (password.length() > passwordConfig.getMaxLength()) {
            failureReasons.add("密码长度不能超过" + passwordConfig.getMaxLength() + "位");
            suggestions.add("请减少密码长度至" + passwordConfig.getMaxLength() + "位以内");
        }

        // 大写字母检查
        if (passwordConfig.getRequireUppercase() && !UPPERCASE_PATTERN.matcher(password).find()) {
            failureReasons.add("密码必须包含大写字母");
            suggestions.add("请添加至少一个大写字母");
        }

        // 小写字母检查
        if (passwordConfig.getRequireLowercase() && !LOWERCASE_PATTERN.matcher(password).find()) {
            failureReasons.add("密码必须包含小写字母");
            suggestions.add("请添加至少一个小写字母");
        }

        // 数字检查
        if (passwordConfig.getRequireDigit() && !DIGIT_PATTERN.matcher(password).find()) {
            failureReasons.add("密码必须包含数字");
            suggestions.add("请添加至少一个数字");
        }

        // 特殊字符检查
        if (passwordConfig.getRequireSpecialChar() && !SPECIAL_CHAR_PATTERN.matcher(password).find()) {
            failureReasons.add("密码必须包含特殊字符");
            suggestions.add("请添加至少一个特殊字符（如!@#$%^&*等）");
        }

        // 如果有验证失败的原因，返回失败结果
        if (!failureReasons.isEmpty()) {
            return PasswordValidationResult.failure(failureReasons, suggestions);
        }

        // 计算密码强度
        int strengthLevel = calculatePasswordStrength(password);
        String strengthDescription = getStrengthDescription(strengthLevel);

        log.debug("[密码验证] 密码验证通过，强度等级={}", strengthLevel);
        return PasswordValidationResult.success(strengthLevel, strengthDescription);
    }

    /**
     * 计算密码强度等级
     *
     * @param password 密码
     * @return 强度等级（1-弱，2-中，3-强）
     */
    private int calculatePasswordStrength(String password) {
        int score = 0;

        // 长度评分
        if (password.length() >= 8) score += 1;
        if (password.length() >= 12) score += 1;
        if (password.length() >= 16) score += 1;

        // 字符类型评分
        if (UPPERCASE_PATTERN.matcher(password).find()) score += 1;
        if (LOWERCASE_PATTERN.matcher(password).find()) score += 1;
        if (DIGIT_PATTERN.matcher(password).find()) score += 1;
        if (SPECIAL_CHAR_PATTERN.matcher(password).find()) score += 1;

        // 复杂度评分
        if (hasMultipleCharTypes(password)) score += 1;
        if (!hasCommonPatterns(password)) score += 1;

        // 转换为1-3的等级
        if (score <= 3) return 1; // 弱
        if (score <= 6) return 2; // 中
        return 3; // 强
    }

    /**
     * 检查是否包含多种字符类型
     *
     * @param password 密码
     * @return true-包含多种类型，false-单一类型
     */
    private boolean hasMultipleCharTypes(String password) {
        int typeCount = 0;
        if (UPPERCASE_PATTERN.matcher(password).find()) typeCount++;
        if (LOWERCASE_PATTERN.matcher(password).find()) typeCount++;
        if (DIGIT_PATTERN.matcher(password).find()) typeCount++;
        if (SPECIAL_CHAR_PATTERN.matcher(password).find()) typeCount++;
        return typeCount >= 3;
    }

    /**
     * 检查是否包含常见模式（如连续数字、键盘序列等）
     *
     * @param password 密码
     * @return true-包含常见模式，false-不包含
     */
    private boolean hasCommonPatterns(String password) {
        String lowerPassword = password.toLowerCase();

        // 检查连续数字
        if (lowerPassword.contains("123") || lowerPassword.contains("234") ||
            lowerPassword.contains("345") || lowerPassword.contains("456") ||
            lowerPassword.contains("789") || lowerPassword.contains("890")) {
            return true;
        }

        // 检查键盘序列
        if (lowerPassword.contains("qwe") || lowerPassword.contains("asd") ||
            lowerPassword.contains("zxc") || lowerPassword.contains("qaz") ||
            lowerPassword.contains("wsx") || lowerPassword.contains("edc")) {
            return true;
        }

        // 检查重复字符
        return hasRepeatingChars(password);
    }

    /**
     * 检查是否有重复字符
     *
     * @param password 密码
     * @return true-有重复，false-无重复
     */
    private boolean hasRepeatingChars(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i + 1) &&
                password.charAt(i + 1) == password.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取强度描述
     *
     * @param strengthLevel 强度等级
     * @return 强度描述
     */
    private String getStrengthDescription(int strengthLevel) {
        switch (strengthLevel) {
            case 1:
                return "弱";
            case 2:
                return "中";
            case 3:
                return "强";
            default:
                return "未知";
        }
    }


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
