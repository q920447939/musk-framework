package org.example.musk.auth.service.core.password;

import org.example.musk.auth.vo.req.password.ChangePasswordRequest;
import org.example.musk.auth.vo.req.password.ForgotPasswordRequest;
import org.example.musk.auth.vo.req.password.ResetPasswordRequest;
import org.example.musk.auth.vo.result.PasswordValidationResult;

/**
 * 会员密码管理服务接口
 *
 * @author musk
 */
public interface MemberPasswordService {

    /**
     * 修改密码
     *
     * @param memberId 会员ID
     * @param request 修改密码请求
     * @return true-修改成功，false-修改失败
     */
    boolean changePassword(Integer memberId, ChangePasswordRequest request);

    /**
     * 重置密码
     *
     * @param request 重置密码请求
     * @return true-重置成功，false-重置失败
     */
    boolean resetPassword(ResetPasswordRequest request);

    /**
     * 忘记密码（发送重置验证码）
     *
     * @param request 忘记密码请求
     * @return true-发送成功，false-发送失败
     */
    boolean forgotPassword(ForgotPasswordRequest request);

    /**
     * 验证密码强度
     *
     * @param password 密码
     * @return 验证结果
     */
    PasswordValidationResult validatePasswordStrength(String password);

    /**
     * 检查密码是否在历史记录中
     *
     * @param memberId 会员ID
     * @param password 密码
     * @return true-在历史记录中，false-不在历史记录中
     */
    boolean isPasswordInHistory(Integer memberId, String password);

    /**
     * 保存密码到历史记录
     *
     * @param memberId 会员ID
     * @param passwordHash 密码哈希值
     */
    void savePasswordHistory(Integer memberId, String passwordHash);

    /**
     * 清理过期的密码历史记录
     *
     * @param memberId 会员ID
     */
    void cleanExpiredPasswordHistory(Integer memberId);

    /**
     * 检查密码重置频率限制
     *
     * @param identifier 标识符（邮箱或手机号）
     * @return true-允许重置，false-超出频率限制
     */
    boolean checkResetFrequencyLimit(String identifier);

    /**
     * 记录密码重置尝试
     *
     * @param identifier 标识符（邮箱或手机号）
     */
    void recordResetAttempt(String identifier);
}
