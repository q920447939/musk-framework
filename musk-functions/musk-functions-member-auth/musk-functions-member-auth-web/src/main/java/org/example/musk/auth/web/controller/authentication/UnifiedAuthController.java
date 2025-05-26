package org.example.musk.auth.web.controller.authentication;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.enums.auth.AuthTypeEnum;
import org.example.musk.auth.service.core.chain.UnifiedAuthenticationChain;
import org.example.musk.auth.service.core.code.VerificationCodeService;
import org.example.musk.auth.web.anno.PassToken;
import org.example.musk.auth.vo.req.auth.UsernamePasswordAuthRequest;
import org.example.musk.auth.vo.req.auth.VerificationCodeAuthRequest;
import org.example.musk.auth.vo.req.code.SendCodeRequest;
import org.example.musk.auth.vo.res.LoginResponseDTO;
import org.example.musk.auth.vo.result.AuthenticationResult;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.common.exception.BusinessPageExceptionEnum;
import org.example.musk.common.pojo.CommonResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 统一认证控制器
 * 提供多种认证方式的统一入口
 *
 * @author musk
 */
@RestController
@RequestMapping("/api/auth")
@Validated
@Slf4j
public class UnifiedAuthController {

    @Resource
    private UnifiedAuthenticationChain unifiedAuthenticationChain;

    @Resource
    private VerificationCodeService verificationCodeService;

    /**
     * 用户名密码登录
     *
     * @param request HTTP请求
     * @param authRequest 认证请求
     * @return 登录结果
     */
    @PostMapping("/login/username")
    @PassToken
    public CommonResult<LoginResponseDTO> loginByUsername(HttpServletRequest request,
                                                         @RequestBody @Valid UsernamePasswordAuthRequest authRequest) {
        log.info("[用户名密码登录] 开始处理登录请求，username={}", authRequest.getUsername());

        // 设置认证类型
        authRequest.setAuthType(AuthTypeEnum.USERNAME_PASSWORD);

        // 设置客户端IP
        authRequest.setClientIp(getClientIp(request));

        // 执行认证
        AuthenticationResult result = unifiedAuthenticationChain.authenticate(authRequest);

        // 处理认证结果
        return handleAuthResult(result);
    }

    /**
     * 邮箱验证码登录
     *
     * @param request HTTP请求
     * @param authRequest 认证请求
     * @return 登录结果
     */
    @PostMapping("/login/email")
    @PassToken
    public CommonResult<LoginResponseDTO> loginByEmail(HttpServletRequest request,
                                                      @RequestBody @Valid VerificationCodeAuthRequest authRequest) {
        log.info("[邮箱验证码登录] 开始处理登录请求，email={}", authRequest.getTarget());

        // 设置认证类型
        authRequest.setAuthType(AuthTypeEnum.EMAIL_CODE);

        // 设置客户端IP
        authRequest.setClientIp(getClientIp(request));

        // 执行认证
        AuthenticationResult result = unifiedAuthenticationChain.authenticate(authRequest);

        // 处理认证结果
        return handleAuthResult(result);
    }

    /**
     * 短信验证码登录
     *
     * @param request HTTP请求
     * @param authRequest 认证请求
     * @return 登录结果
     */
    @PostMapping("/login/sms")
    @PassToken
    public CommonResult<LoginResponseDTO> loginBySms(HttpServletRequest request,
                                                    @RequestBody @Valid VerificationCodeAuthRequest authRequest) {
        log.info("[短信验证码登录] 开始处理登录请求，phone={}", authRequest.getTarget());

        // 设置认证类型
        authRequest.setAuthType(AuthTypeEnum.SMS_CODE);

        // 设置客户端IP
        authRequest.setClientIp(getClientIp(request));

        // 执行认证
        AuthenticationResult result = unifiedAuthenticationChain.authenticate(authRequest);

        // 处理认证结果
        return handleAuthResult(result);
    }

    /**
     * 发送验证码
     *
     * @param request HTTP请求
     * @param sendRequest 发送验证码请求
     * @return 发送结果
     */
    @PostMapping("/code/send")
    @PassToken
    public CommonResult<Boolean> sendVerificationCode(HttpServletRequest request,
                                                     @RequestBody @Valid SendCodeRequest sendRequest) {
        log.info("[发送验证码] 开始处理发送请求，target={}, channel={}, scene={}",
                sendRequest.getTarget(), sendRequest.getChannel().getCode(), sendRequest.getScene().getCode());

        // 设置客户端IP
        sendRequest.setClientIp(getClientIp(request));

        // 发送验证码
        boolean success = verificationCodeService.sendCode(sendRequest);

        if (success) {
            log.info("[发送验证码] 发送成功，target={}", sendRequest.getTarget());
            return CommonResult.success(true);
        } else {
            log.warn("[发送验证码] 发送失败，target={}", sendRequest.getTarget());
            throw new BusinessException(BusinessPageExceptionEnum.VERIFICATION_CODE_SEND_FAILED);
        }
    }

    /**
     * 获取支持的认证类型
     *
     * @return 支持的认证类型列表
     */
    @GetMapping("/types")
    @PassToken
    public CommonResult<List<AuthTypeEnum>> getSupportedAuthTypes() {
        List<AuthTypeEnum> supportedTypes = unifiedAuthenticationChain.getSupportedAuthTypes();
        return CommonResult.success(supportedTypes);
    }

    /**
     * 获取认证策略统计信息
     *
     * @return 策略统计信息
     */
    @GetMapping("/stats")
    @PassToken
    public CommonResult<Map<String, Object>> getStrategyStats() {
        Map<String, Object> stats = unifiedAuthenticationChain.getStrategyStats();
        return CommonResult.success(stats);
    }

    /**
     * 退出登录
     *
     * @return 退出结果
     */
    @PostMapping("/logout")
    public CommonResult<Boolean> logout() {
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        log.info("[退出登录] 会员退出登录，memberId={}", memberId);

        StpUtil.logout();
        return CommonResult.success(true);
    }

    /**
     * 处理认证结果
     *
     * @param result 认证结果
     * @return 登录响应
     */
    private CommonResult<LoginResponseDTO> handleAuthResult(AuthenticationResult result) {
        if (!result.isSuccess()) {
            log.warn("[处理认证结果] 认证失败，errorCode={}, errorMessage={}",
                    result.getErrorCode(), result.getErrorMessage());
            throw new BusinessException(result.getErrorCode(), result.getErrorMessage());
        }

        // 认证成功，生成Token
        StpUtil.login(result.getMember().getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 构建响应
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(tokenInfo.tokenValue);

        log.info("[处理认证结果] 认证成功，memberId={}, token={}",
                result.getMember().getId(), tokenInfo.tokenValue);

        return CommonResult.success(response);
    }

    /**
     * 获取客户端IP地址
     *
     * @param request HTTP请求
     * @return 客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
