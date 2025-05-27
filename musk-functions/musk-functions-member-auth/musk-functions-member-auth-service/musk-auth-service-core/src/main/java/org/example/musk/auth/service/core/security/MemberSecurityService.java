package org.example.musk.auth.service.core.security;

import org.example.musk.auth.entity.security.MemberSecurityLogDO;
import org.example.musk.auth.enums.SecurityOperationEnum;
import org.example.musk.auth.vo.req.security.SecurityLogQueryRequest;
import org.example.musk.auth.vo.req.security.SecurityVerificationRequest;
import org.example.musk.auth.vo.res.SecurityLogResponse;
import org.example.musk.common.pojo.db.PageResult;

/**
 * 会员安全服务接口
 *
 * @author musk
 */
public interface MemberSecurityService {

    /**
     * 安全验证
     *
     * @param memberId 会员ID
     * @param request 验证请求
     * @return true-验证通过，false-验证失败
     */
    boolean securityVerification(Integer memberId, SecurityVerificationRequest request);

    /**
     * 记录安全操作日志
     *
     * @param memberId 会员ID
     * @param operationType 操作类型
     * @param operationResult 操作结果
     * @param clientIp 客户端IP
     * @param userAgent 用户代理
     * @param extraInfo 扩展信息
     * @param failureReason 失败原因
     * @return 日志ID
     */
    Long recordSecurityLog(Integer memberId, SecurityOperationEnum operationType,
                          String operationResult, String clientIp, String userAgent,
                          String extraInfo, String failureReason);

    /**
     * 查询安全日志
     *
     * @param memberId 会员ID
     * @param request 查询请求
     * @return 安全日志分页结果
     */
    PageResult<SecurityLogResponse> querySecurityLogs(Integer memberId, SecurityLogQueryRequest request);

    /**
     * 检查操作频率限制
     *
     * @param memberId 会员ID
     * @param operationType 操作类型
     * @return true-允许操作，false-超出频率限制
     */
    boolean checkOperationFrequencyLimit(Integer memberId, SecurityOperationEnum operationType);

    /**
     * 记录操作尝试
     *
     * @param memberId 会员ID
     * @param operationType 操作类型
     */
    void recordOperationAttempt(Integer memberId, SecurityOperationEnum operationType);

    /**
     * 验证密码
     *
     * @param memberId 会员ID
     * @param password 密码
     * @return true-验证通过，false-验证失败
     */
    boolean verifyPassword(Integer memberId, String password);

    /**
     * 验证图形验证码
     *
     * @param sessionId 会话ID
     * @param code 验证码
     * @return true-验证通过，false-验证失败
     */
    boolean verifyCaptcha(String sessionId, String code);

    /**
     * 验证邮箱验证码
     *
     * @param email 邮箱
     * @param code 验证码
     * @param scene 场景
     * @return true-验证通过，false-验证失败
     */
    boolean verifyEmailCode(String email, String code, String scene);

    /**
     * 验证短信验证码
     *
     * @param phone 手机号
     * @param code 验证码
     * @param scene 场景
     * @return true-验证通过，false-验证失败
     */
    boolean verifySmsCode(String phone, String code, String scene);

    /**
     * 清理过期的安全日志
     *
     * @param retentionDays 保留天数
     * @return 清理的记录数量
     */
    int cleanExpiredSecurityLogs(Integer retentionDays);
}
