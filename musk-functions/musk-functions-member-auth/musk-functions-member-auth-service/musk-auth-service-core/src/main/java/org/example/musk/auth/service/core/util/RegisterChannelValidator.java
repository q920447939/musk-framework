package org.example.musk.auth.service.core.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.enums.code.CodeChannelEnum;
import org.example.musk.auth.enums.member.RegisterChannelEnums;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 注册渠道校验工具
 *
 * @author musk
 */
@Component
@Slf4j
public class RegisterChannelValidator {

    /**
     * 邮箱正则表达式
     */
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    /**
     * 手机号正则表达式（中国大陆）
     */
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

    /**
     * 用户名正则表达式
     */
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{6,30}$";

    /**
     * 验证注册渠道
     *
     * @param channel 注册渠道
     * @param target 目标值（用户名、邮箱、手机号等）
     * @return true-有效，false-无效
     */
    public boolean validateRegisterChannel(RegisterChannelEnums channel, String target) {
        if (channel == null || StrUtil.isBlank(target)) {
            return false;
        }

        switch (channel) {
            case APP:
            case WEB:
            case WECHAT_MINI_PROGRAM:
            case H5:
                // 这些渠道主要用于标识来源，不直接验证target
                return true;
            default:
                log.warn("[注册渠道校验] 未知的注册渠道: {}", channel);
                return false;
        }
    }

    /**
     * 验证验证码渠道对应的目标值
     *
     * @param channel 验证码渠道
     * @param target 目标值
     * @return true-有效，false-无效
     */
    public boolean validateCodeChannelTarget(CodeChannelEnum channel, String target) {
        if (channel == null || StrUtil.isBlank(target)) {
            return false;
        }

        switch (channel) {
            case EMAIL:
                return isValidEmail(target);
            case SMS:
                return isValidPhone(target);
            default:
                log.warn("[验证码渠道校验] 未知的验证码渠道: {}", channel);
                return false;
        }
    }

    /**
     * 验证邮箱格式
     *
     * @param email 邮箱地址
     * @return true-格式正确，false-格式错误
     */
    public boolean isValidEmail(String email) {
        if (StrUtil.isBlank(email)) {
            return false;
        }
        return Pattern.matches(EMAIL_REGEX, email);
    }

    /**
     * 验证手机号格式
     *
     * @param phone 手机号
     * @return true-格式正确，false-格式错误
     */
    public boolean isValidPhone(String phone) {
        if (StrUtil.isBlank(phone)) {
            return false;
        }
        return Pattern.matches(PHONE_REGEX, phone);
    }

    /**
     * 验证用户名格式
     *
     * @param username 用户名
     * @return true-格式正确，false-格式错误
     */
    public boolean isValidUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return false;
        }

        // 基本格式验证
        if (!Pattern.matches(USERNAME_REGEX, username)) {
            return false;
        }

        // 用户名不能以数字开头
        if (Character.isDigit(username.charAt(0))) {
            return false;
        }

        // 用户名不能全是数字
        if (username.matches("\\d+")) {
            return false;
        }

        return true;
    }

    /**
     * 获取邮箱域名
     *
     * @param email 邮箱地址
     * @return 域名，如果邮箱格式不正确返回null
     */
    public String getEmailDomain(String email) {
        if (!isValidEmail(email)) {
            return null;
        }
        int atIndex = email.indexOf("@");
        return email.substring(atIndex + 1);
    }

    /**
     * 获取手机号归属地区号
     *
     * @param phone 手机号
     * @return 区号，如果手机号格式不正确返回null
     */
    public String getPhoneAreaCode(String phone) {
        if (!isValidPhone(phone)) {
            return null;
        }
        // 中国大陆手机号前三位
        return phone.substring(0, 3);
    }

    /**
     * 脱敏邮箱地址
     *
     * @param email 邮箱地址
     * @return 脱敏后的邮箱地址
     */
    public String maskEmail(String email) {
        if (!isValidEmail(email)) {
            return email;
        }

        int atIndex = email.indexOf("@");
        String localPart = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        if (localPart.length() <= 2) {
            return localPart.charAt(0) + "*" + domain;
        } else if (localPart.length() <= 4) {
            return localPart.charAt(0) + "**" + localPart.charAt(localPart.length() - 1) + domain;
        } else {
            return localPart.substring(0, 2) + "***" + localPart.substring(localPart.length() - 2) + domain;
        }
    }

    /**
     * 脱敏手机号
     *
     * @param phone 手机号
     * @return 脱敏后的手机号
     */
    public String maskPhone(String phone) {
        if (!isValidPhone(phone)) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 脱敏用户名
     *
     * @param username 用户名
     * @return 脱敏后的用户名
     */
    public String maskUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return username;
        }

        if (username.length() <= 2) {
            return username.charAt(0) + "*";
        } else if (username.length() <= 4) {
            return username.charAt(0) + "**" + username.charAt(username.length() - 1);
        } else {
            return username.substring(0, 2) + "***" + username.substring(username.length() - 2);
        }
    }
}
