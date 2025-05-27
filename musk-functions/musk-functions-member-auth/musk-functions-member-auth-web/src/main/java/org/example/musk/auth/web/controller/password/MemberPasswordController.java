package org.example.musk.auth.web.controller.password;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.enums.SecurityOperationEnum;
import org.example.musk.auth.service.core.password.MemberPasswordService;
import org.example.musk.auth.service.core.security.MemberSecurityService;
import org.example.musk.auth.vo.req.password.ChangePasswordRequest;
import org.example.musk.auth.vo.req.password.ForgotPasswordRequest;
import org.example.musk.auth.vo.req.password.ResetPasswordRequest;
import org.example.musk.auth.vo.result.PasswordValidationResult;
import org.example.musk.auth.web.anno.PassToken;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 会员密码管理控制器
 *
 * @author musk
 */
@RestController
@RequestMapping("/api/member/password")
@Validated
@Slf4j
public class MemberPasswordController {

    @Resource
    private MemberPasswordService memberPasswordService;

    @Resource
    private MemberSecurityService memberSecurityService;

    /**
     * 修改密码
     *
     * @param request HTTP请求
     * @param changePasswordRequest 修改密码请求
     * @return 修改结果
     */
    @PostMapping("/change")
    public CommonResult<Boolean> changePassword(HttpServletRequest request,
                                               @RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        log.info("[修改密码] 开始处理修改密码请求，memberId={}", memberId);

        try {
            // 记录操作日志
            String clientIp = getClientIp(request);
            String userAgent = request.getHeader("User-Agent");

            boolean result = memberPasswordService.changePassword(memberId, changePasswordRequest);

            // 记录安全日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.CHANGE_PASSWORD,
                    result ? "SUCCESS" : "FAILED",
                    clientIp,
                    userAgent,
                    null,
                    result ? null : "密码修改失败"
            );

            if (result) {
                log.info("[修改密码] 密码修改成功，memberId={}", memberId);
                return CommonResult.success(true);
            } else {
                log.warn("[修改密码] 密码修改失败，memberId={}", memberId);
                return CommonResult.error("密码修改失败");
            }

        } catch (Exception e) {
            log.error("[修改密码] 密码修改异常，memberId={}", memberId, e);
            
            // 记录失败日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.CHANGE_PASSWORD,
                    "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    e.getMessage()
            );
            
            return CommonResult.error("密码修改失败：" + e.getMessage());
        }
    }

    /**
     * 重置密码
     *
     * @param request HTTP请求
     * @param resetPasswordRequest 重置密码请求
     * @return 重置结果
     */
    @PostMapping("/reset")
    @PassToken
    public CommonResult<Boolean> resetPassword(HttpServletRequest request,
                                              @RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        log.info("[重置密码] 开始处理重置密码请求，identifier={}", resetPasswordRequest.getIdentifier());

        try {
            boolean result = memberPasswordService.resetPassword(resetPasswordRequest);

            if (result) {
                log.info("[重置密码] 密码重置成功，identifier={}", resetPasswordRequest.getIdentifier());
                return CommonResult.success(true);
            } else {
                log.warn("[重置密码] 密码重置失败，identifier={}", resetPasswordRequest.getIdentifier());
                return CommonResult.error("密码重置失败");
            }

        } catch (Exception e) {
            log.error("[重置密码] 密码重置异常，identifier={}", resetPasswordRequest.getIdentifier(), e);
            return CommonResult.error("密码重置失败：" + e.getMessage());
        }
    }

    /**
     * 忘记密码（发送重置验证码）
     *
     * @param request HTTP请求
     * @param forgotPasswordRequest 忘记密码请求
     * @return 发送结果
     */
    @PostMapping("/forgot")
    @PassToken
    public CommonResult<Boolean> forgotPassword(HttpServletRequest request,
                                               @RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) {
        log.info("[忘记密码] 开始处理忘记密码请求，identifier={}", forgotPasswordRequest.getIdentifier());

        try {
            boolean result = memberPasswordService.forgotPassword(forgotPasswordRequest);

            if (result) {
                log.info("[忘记密码] 验证码发送成功，identifier={}", forgotPasswordRequest.getIdentifier());
                return CommonResult.success(true);
            } else {
                log.warn("[忘记密码] 验证码发送失败，identifier={}", forgotPasswordRequest.getIdentifier());
                return CommonResult.error("验证码发送失败");
            }

        } catch (Exception e) {
            log.error("[忘记密码] 处理异常，identifier={}", forgotPasswordRequest.getIdentifier(), e);
            return CommonResult.error("处理失败：" + e.getMessage());
        }
    }

    /**
     * 验证密码强度
     *
     * @param password 密码
     * @return 验证结果
     */
    @PostMapping("/validate")
    public CommonResult<PasswordValidationResult> validatePassword(@RequestParam("password") String password) {
        log.debug("[密码验证] 开始验证密码强度");

        try {
            PasswordValidationResult result = memberPasswordService.validatePasswordStrength(password);
            return CommonResult.success(result);

        } catch (Exception e) {
            log.error("[密码验证] 验证异常", e);
            return CommonResult.error("密码验证失败：" + e.getMessage());
        }
    }

    /**
     * 检查密码是否在历史记录中
     *
     * @param password 密码
     * @return 检查结果
     */
    @PostMapping("/check-history")
    public CommonResult<Boolean> checkPasswordHistory(@RequestParam("password") String password) {
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        log.debug("[密码历史检查] 开始检查密码历史，memberId={}", memberId);

        try {
            boolean inHistory = memberPasswordService.isPasswordInHistory(memberId, password);
            return CommonResult.success(inHistory);

        } catch (Exception e) {
            log.error("[密码历史检查] 检查异常，memberId={}", memberId, e);
            return CommonResult.error("检查失败：" + e.getMessage());
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
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
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
