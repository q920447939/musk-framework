package org.example.musk.auth.web.controller.profile;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.enums.SecurityOperationEnum;
import org.example.musk.auth.service.core.profile.MemberProfileService;
import org.example.musk.auth.service.core.security.MemberSecurityService;
import org.example.musk.auth.vo.req.profile.EmailManagementRequest;
import org.example.musk.auth.vo.req.profile.PhoneManagementRequest;
import org.example.musk.auth.vo.req.profile.UpdateBasicInfoRequest;
import org.example.musk.auth.vo.res.ContactManagementResponse;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 会员信息管理控制器
 *
 * @author musk
 */
@RestController
@RequestMapping("/api/member/profile")
@Validated
@Slf4j
public class MemberProfileController {

    @Resource
    private MemberProfileService memberProfileService;

    @Resource
    private MemberSecurityService memberSecurityService;

    /**
     * 修改基础信息
     *
     * @param request HTTP请求
     * @param updateRequest 修改请求
     * @return 修改结果
     */
    @PutMapping("/basic")
    public CommonResult<Boolean> updateBasicInfo(HttpServletRequest request,
                                                 @RequestBody @Valid UpdateBasicInfoRequest updateRequest) {
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        log.info("[修改基础信息] 开始处理修改基础信息请求，memberId={}", memberId);

        try {
            boolean result = memberProfileService.updateBasicInfo(memberId, updateRequest);

            // 记录安全日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.UPDATE_BASIC_INFO,
                    result ? "SUCCESS" : "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    result ? null : "基础信息修改失败"
            );

            if (result) {
                log.info("[修改基础信息] 修改成功，memberId={}", memberId);
                return CommonResult.success(true);
            } else {
                log.warn("[修改基础信息] 修改失败，memberId={}", memberId);
                return CommonResult.error("基础信息修改失败");
            }

        } catch (Exception e) {
            log.error("[修改基础信息] 修改异常，memberId={}", memberId, e);
            
            // 记录失败日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.UPDATE_BASIC_INFO,
                    "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    e.getMessage()
            );
            
            return CommonResult.error("基础信息修改失败：" + e.getMessage());
        }
    }

    /**
     * 绑定邮箱
     *
     * @param request HTTP请求
     * @param emailRequest 邮箱管理请求
     * @return 绑定结果
     */
    @PostMapping("/email/bind")
    public CommonResult<ContactManagementResponse> bindEmail(HttpServletRequest request,
                                                            @RequestBody @Valid EmailManagementRequest emailRequest) {
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        log.info("[绑定邮箱] 开始处理绑定邮箱请求，memberId={}, email={}", memberId, emailRequest.getNewEmail());

        try {
            // 设置操作类型为绑定
            emailRequest.setOperation(org.example.musk.auth.enums.OperationTypeEnum.BIND);
            
            ContactManagementResponse result = memberProfileService.manageEmail(memberId, emailRequest);

            // 记录安全日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.BIND_EMAIL,
                    result.getSuccess() ? "SUCCESS" : "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    result.getSuccess() ? null : result.getVerificationTip()
            );

            return CommonResult.success(result);

        } catch (Exception e) {
            log.error("[绑定邮箱] 绑定异常，memberId={}", memberId, e);
            
            // 记录失败日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.BIND_EMAIL,
                    "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    e.getMessage()
            );
            
            return CommonResult.error("邮箱绑定失败：" + e.getMessage());
        }
    }

    /**
     * 解绑邮箱
     *
     * @param request HTTP请求
     * @param emailRequest 邮箱管理请求
     * @return 解绑结果
     */
    @PostMapping("/email/unbind")
    public CommonResult<ContactManagementResponse> unbindEmail(HttpServletRequest request,
                                                              @RequestBody @Valid EmailManagementRequest emailRequest) {
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        log.info("[解绑邮箱] 开始处理解绑邮箱请求，memberId={}, email={}", memberId, emailRequest.getCurrentEmail());

        try {
            // 设置操作类型为解绑
            emailRequest.setOperation(org.example.musk.auth.enums.OperationTypeEnum.UNBIND);
            
            ContactManagementResponse result = memberProfileService.manageEmail(memberId, emailRequest);

            // 记录安全日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.UNBIND_EMAIL,
                    result.getSuccess() ? "SUCCESS" : "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    result.getSuccess() ? null : result.getVerificationTip()
            );

            return CommonResult.success(result);

        } catch (Exception e) {
            log.error("[解绑邮箱] 解绑异常，memberId={}", memberId, e);
            
            // 记录失败日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.UNBIND_EMAIL,
                    "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    e.getMessage()
            );
            
            return CommonResult.error("邮箱解绑失败：" + e.getMessage());
        }
    }

    /**
     * 更换邮箱
     *
     * @param request HTTP请求
     * @param emailRequest 邮箱管理请求
     * @return 更换结果
     */
    @PostMapping("/email/change")
    public CommonResult<ContactManagementResponse> changeEmail(HttpServletRequest request,
                                                              @RequestBody @Valid EmailManagementRequest emailRequest) {
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        log.info("[更换邮箱] 开始处理更换邮箱请求，memberId={}, currentEmail={}, newEmail={}", 
                memberId, emailRequest.getCurrentEmail(), emailRequest.getNewEmail());

        try {
            // 设置操作类型为更换
            emailRequest.setOperation(org.example.musk.auth.enums.OperationTypeEnum.CHANGE);
            
            ContactManagementResponse result = memberProfileService.manageEmail(memberId, emailRequest);

            // 记录安全日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.CHANGE_EMAIL,
                    result.getSuccess() ? "SUCCESS" : "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    result.getSuccess() ? null : result.getVerificationTip()
            );

            return CommonResult.success(result);

        } catch (Exception e) {
            log.error("[更换邮箱] 更换异常，memberId={}", memberId, e);
            
            // 记录失败日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.CHANGE_EMAIL,
                    "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    e.getMessage()
            );
            
            return CommonResult.error("邮箱更换失败：" + e.getMessage());
        }
    }

    /**
     * 绑定手机号
     *
     * @param request HTTP请求
     * @param phoneRequest 手机号管理请求
     * @return 绑定结果
     */
    @PostMapping("/phone/bind")
    public CommonResult<ContactManagementResponse> bindPhone(HttpServletRequest request,
                                                            @RequestBody @Valid PhoneManagementRequest phoneRequest) {
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        log.info("[绑定手机号] 开始处理绑定手机号请求，memberId={}, phone={}", memberId, phoneRequest.getNewPhone());

        try {
            // 设置操作类型为绑定
            phoneRequest.setOperation(org.example.musk.auth.enums.OperationTypeEnum.BIND);
            
            ContactManagementResponse result = memberProfileService.managePhone(memberId, phoneRequest);

            // 记录安全日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.BIND_PHONE,
                    result.getSuccess() ? "SUCCESS" : "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    result.getSuccess() ? null : result.getVerificationTip()
            );

            return CommonResult.success(result);

        } catch (Exception e) {
            log.error("[绑定手机号] 绑定异常，memberId={}", memberId, e);
            
            // 记录失败日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.BIND_PHONE,
                    "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    e.getMessage()
            );
            
            return CommonResult.error("手机号绑定失败：" + e.getMessage());
        }
    }

    /**
     * 解绑手机号
     *
     * @param request HTTP请求
     * @param phoneRequest 手机号管理请求
     * @return 解绑结果
     */
    @PostMapping("/phone/unbind")
    public CommonResult<ContactManagementResponse> unbindPhone(HttpServletRequest request,
                                                              @RequestBody @Valid PhoneManagementRequest phoneRequest) {
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        log.info("[解绑手机号] 开始处理解绑手机号请求，memberId={}, phone={}", memberId, phoneRequest.getCurrentPhone());

        try {
            // 设置操作类型为解绑
            phoneRequest.setOperation(org.example.musk.auth.enums.OperationTypeEnum.UNBIND);
            
            ContactManagementResponse result = memberProfileService.managePhone(memberId, phoneRequest);

            // 记录安全日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.UNBIND_PHONE,
                    result.getSuccess() ? "SUCCESS" : "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    result.getSuccess() ? null : result.getVerificationTip()
            );

            return CommonResult.success(result);

        } catch (Exception e) {
            log.error("[解绑手机号] 解绑异常，memberId={}", memberId, e);
            
            // 记录失败日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.UNBIND_PHONE,
                    "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    e.getMessage()
            );
            
            return CommonResult.error("手机号解绑失败：" + e.getMessage());
        }
    }

    /**
     * 更换手机号
     *
     * @param request HTTP请求
     * @param phoneRequest 手机号管理请求
     * @return 更换结果
     */
    @PostMapping("/phone/change")
    public CommonResult<ContactManagementResponse> changePhone(HttpServletRequest request,
                                                              @RequestBody @Valid PhoneManagementRequest phoneRequest) {
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        log.info("[更换手机号] 开始处理更换手机号请求，memberId={}, currentPhone={}, newPhone={}", 
                memberId, phoneRequest.getCurrentPhone(), phoneRequest.getNewPhone());

        try {
            // 设置操作类型为更换
            phoneRequest.setOperation(org.example.musk.auth.enums.OperationTypeEnum.CHANGE);
            
            ContactManagementResponse result = memberProfileService.managePhone(memberId, phoneRequest);

            // 记录安全日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.CHANGE_PHONE,
                    result.getSuccess() ? "SUCCESS" : "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    result.getSuccess() ? null : result.getVerificationTip()
            );

            return CommonResult.success(result);

        } catch (Exception e) {
            log.error("[更换手机号] 更换异常，memberId={}", memberId, e);
            
            // 记录失败日志
            memberSecurityService.recordSecurityLog(
                    memberId,
                    SecurityOperationEnum.CHANGE_PHONE,
                    "FAILED",
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    null,
                    e.getMessage()
            );
            
            return CommonResult.error("手机号更换失败：" + e.getMessage());
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
