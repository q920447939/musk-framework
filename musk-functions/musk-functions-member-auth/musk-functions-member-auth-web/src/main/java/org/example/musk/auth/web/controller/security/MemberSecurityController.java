package org.example.musk.auth.web.controller.security;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.enums.SecurityOperationEnum;
import org.example.musk.auth.service.core.security.MemberSecurityService;
import org.example.musk.auth.vo.req.security.SecurityLogQueryRequest;
import org.example.musk.auth.vo.req.security.SecurityVerificationRequest;
import org.example.musk.auth.vo.res.SecurityLogResponse;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.pojo.db.PageResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 会员安全管理控制器
 *
 * @author musk
 */
@RestController
@RequestMapping("/api/member/security")
@Validated
@Slf4j
public class MemberSecurityController {

    @Resource
    private MemberSecurityService memberSecurityService;

    /**
     * 查询安全日志
     *
     * @param request HTTP请求
     * @param queryRequest 查询请求
     * @return 安全日志分页结果
     */
    @GetMapping("/logs")
    public CommonResult<PageResult<SecurityLogResponse>> querySecurityLogs(
            HttpServletRequest request,
            @Valid SecurityLogQueryRequest queryRequest) {
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        log.info("[查询安全日志] 开始查询安全日志，memberId={}", memberId);

        try {
            PageResult<SecurityLogResponse> result = memberSecurityService.querySecurityLogs(memberId, queryRequest);

            // 记录查看日志的操作
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.VIEW_SECURITY_LOG,
                    "SUCCESS",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    null
            );

            log.info("[查询安全日志] 查询成功，memberId={}, total={}", memberId, result.getTotal());
            return CommonResult.success(result);

        } catch (Exception e) {
            log.error("[查询安全日志] 查询异常，memberId={}", memberId, e);

            // 记录失败日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.VIEW_SECURITY_LOG,
                    "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    e.getMessage()
            );

            return CommonResult.error("查询安全日志失败：" + e.getMessage());
        }
    }

    /**
     * 安全验证
     *
     * @param request HTTP请求
     * @param verificationRequest 验证请求
     * @return 验证结果
     */
    @PostMapping("/verify")
    public CommonResult<Boolean> securityVerification(HttpServletRequest request,
                                                     @RequestBody @Valid SecurityVerificationRequest verificationRequest) {
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        log.info("[安全验证] 开始安全验证，memberId={}, operationType={}",
                memberId, verificationRequest.getOperationType().getCode());

        try {
            boolean result = memberSecurityService.securityVerification(memberId, verificationRequest);

            // 记录验证操作
            memberSecurityService.recordSecurityLog(
                    memberId,
                    verificationRequest.getOperationType(),
                    result ? "SUCCESS" : "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    verificationRequest.getExtraInfo(),
                    result ? null : "安全验证失败"
            );

            if (result) {
                log.info("[安全验证] 验证成功，memberId={}, operationType={}",
                        memberId, verificationRequest.getOperationType().getCode());
                return CommonResult.success(true);
            } else {
                log.warn("[安全验证] 验证失败，memberId={}, operationType={}",
                        memberId, verificationRequest.getOperationType().getCode());
                return CommonResult.error("安全验证失败");
            }

        } catch (Exception e) {
            log.error("[安全验证] 验证异常，memberId={}, operationType={}",
                    memberId, verificationRequest.getOperationType().getCode(), e);

            // 记录失败日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    verificationRequest.getOperationType(),
                    "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    verificationRequest.getExtraInfo(),
                    e.getMessage()
            );

            return CommonResult.error("安全验证失败：" + e.getMessage());
        }
    }

    /**
     * 检查操作频率限制
     *
     * @param operationType 操作类型
     * @return 检查结果
     */
    @GetMapping("/check-frequency")
    public CommonResult<Boolean> checkOperationFrequency(@RequestParam("operationType") String operationType) {
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        log.debug("[频率检查] 开始检查操作频率，memberId={}, operationType={}", memberId, operationType);

        try {
            SecurityOperationEnum operation = SecurityOperationEnum.fromCode(operationType);
            if (operation == null) {
                return CommonResult.error("不支持的操作类型");
            }

            boolean allowed = memberSecurityService.checkOperationFrequencyLimit(memberId, operation);
            return CommonResult.success(allowed);

        } catch (Exception e) {
            log.error("[频率检查] 检查异常，memberId={}, operationType={}", memberId, operationType, e);
            return CommonResult.error("频率检查失败：" + e.getMessage());
        }
    }

    /**
     * 验证图形验证码
     *
     * @param sessionId 会话ID
     * @param code 验证码
     * @return 验证结果
     */
    @PostMapping("/verify-captcha")
    public CommonResult<Boolean> verifyCaptcha(@RequestParam("sessionId") String sessionId,
                                              @RequestParam("code") String code) {
        log.debug("[图形验证码验证] 开始验证图形验证码，sessionId={}", sessionId);

        try {
            boolean result = memberSecurityService.verifyCaptcha(sessionId, code);
            return CommonResult.success(result);

        } catch (Exception e) {
            log.error("[图形验证码验证] 验证异常，sessionId={}", sessionId, e);
            return CommonResult.error("图形验证码验证失败：" + e.getMessage());
        }
    }

    /**
     * 验证邮箱验证码
     *
     * @param email 邮箱
     * @param code 验证码
     * @param scene 场景
     * @return 验证结果
     */
    @PostMapping("/verify-email-code")
    public CommonResult<Boolean> verifyEmailCode(@RequestParam("email") String email,
                                                 @RequestParam("code") String code,
                                                 @RequestParam("scene") String scene) {
        log.debug("[邮箱验证码验证] 开始验证邮箱验证码，email={}, scene={}", email, scene);

        try {
            boolean result = memberSecurityService.verifyEmailCode(email, code, scene);
            return CommonResult.success(result);

        } catch (Exception e) {
            log.error("[邮箱验证码验证] 验证异常，email={}, scene={}", email, scene, e);
            return CommonResult.error("邮箱验证码验证失败：" + e.getMessage());
        }
    }

    /**
     * 验证短信验证码
     *
     * @param phone 手机号
     * @param code 验证码
     * @param scene 场景
     * @return 验证结果
     */
    @PostMapping("/verify-sms-code")
    public CommonResult<Boolean> verifySmsCode(@RequestParam("phone") String phone,
                                              @RequestParam("code") String code,
                                              @RequestParam("scene") String scene) {
        log.debug("[短信验证码验证] 开始验证短信验证码，phone={}, scene={}", phone, scene);

        try {
            boolean result = memberSecurityService.verifySmsCode(phone, code, scene);
            return CommonResult.success(result);

        } catch (Exception e) {
            log.error("[短信验证码验证] 验证异常，phone={}, scene={}", phone, scene, e);
            return CommonResult.error("短信验证码验证失败：" + e.getMessage());
        }
    }

    /**
     * 获取客户端IP地址
     *
     * @param request HTTP请求
     * @return IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String xip = request.getHeader("X-Real-IP");
        String xfor = request.getHeader("X-Forwarded-For");
        if (xfor != null && !xfor.isEmpty() && !"unKnown".equalsIgnoreCase(xfor)) {
            int index = xfor.indexOf(",");
            if (index != -1) {
                return xfor.substring(0, index);
            } else {
                return xfor;
            }
        }
        xfor = xip;
        if (xfor != null && !xfor.isEmpty() && !"unKnown".equalsIgnoreCase(xfor)) {
            return xfor;
        }
        if (xfor == null || xfor.isEmpty() || "unknown".equalsIgnoreCase(xfor)) {
            xfor = request.getHeader("Proxy-Client-IP");
        }
        if (xfor == null || xfor.isEmpty() || "unknown".equalsIgnoreCase(xfor)) {
            xfor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (xfor == null || xfor.isEmpty() || "unknown".equalsIgnoreCase(xfor)) {
            xfor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (xfor == null || xfor.isEmpty() || "unknown".equalsIgnoreCase(xfor)) {
            xfor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (xfor == null || xfor.isEmpty() || "unknown".equalsIgnoreCase(xfor)) {
            xfor = request.getRemoteAddr();
        }
        return xfor;
    }
}
