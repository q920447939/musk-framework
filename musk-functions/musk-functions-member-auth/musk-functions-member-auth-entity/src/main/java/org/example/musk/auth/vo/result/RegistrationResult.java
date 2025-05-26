package org.example.musk.auth.vo.result;

import lombok.Data;
import org.example.musk.auth.entity.member.MemberDO;
import org.example.musk.auth.enums.auth.AuthTypeEnum;

import java.time.LocalDateTime;

/**
 * 注册结果
 *
 * @author musk
 */
@Data
public class RegistrationResult {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 会员信息
     */
    private MemberDO member;

    /**
     * 注册类型
     */
    private AuthTypeEnum registerType;

    /**
     * 注册时间
     */
    private LocalDateTime registerTime;

    /**
     * 是否自动登录
     */
    private boolean autoLogin;

    /**
     * 登录令牌（如果自动登录）
     */
    private String token;

    /**
     * 创建成功结果
     *
     * @param member 会员信息
     * @param registerType 注册类型
     * @return 注册结果
     */
    public static RegistrationResult success(MemberDO member, AuthTypeEnum registerType) {
        RegistrationResult result = new RegistrationResult();
        result.setSuccess(true);
        result.setMember(member);
        result.setRegisterType(registerType);
        result.setRegisterTime(LocalDateTime.now());
        result.setAutoLogin(false);
        return result;
    }

    /**
     * 创建成功结果（带自动登录）
     *
     * @param member 会员信息
     * @param registerType 注册类型
     * @param token 登录令牌
     * @return 注册结果
     */
    public static RegistrationResult successWithAutoLogin(MemberDO member, AuthTypeEnum registerType, String token) {
        RegistrationResult result = success(member, registerType);
        result.setAutoLogin(true);
        result.setToken(token);
        return result;
    }

    /**
     * 创建失败结果
     *
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     * @return 注册结果
     */
    public static RegistrationResult failure(String errorCode, String errorMessage) {
        RegistrationResult result = new RegistrationResult();
        result.setSuccess(false);
        result.setErrorCode(errorCode);
        result.setErrorMessage(errorMessage);
        return result;
    }

    /**
     * 创建失败结果
     *
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     * @param registerType 注册类型
     * @return 注册结果
     */
    public static RegistrationResult failure(String errorCode, String errorMessage, AuthTypeEnum registerType) {
        RegistrationResult result = failure(errorCode, errorMessage);
        result.setRegisterType(registerType);
        return result;
    }
}
