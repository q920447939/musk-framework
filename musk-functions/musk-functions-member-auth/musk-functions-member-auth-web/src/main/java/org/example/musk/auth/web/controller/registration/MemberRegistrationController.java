package org.example.musk.auth.web.controller.registration;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.enums.auth.AuthTypeEnum;
import org.example.musk.auth.enums.code.CodeChannelEnum;
import org.example.musk.auth.enums.code.CodeSceneEnum;
import org.example.musk.auth.service.core.chain.UnifiedRegistrationChain;
import org.example.musk.auth.service.core.code.CaptchaService;
import org.example.musk.auth.service.core.code.VerificationCodeService;
import org.example.musk.auth.vo.req.code.SendCodeRequest;
import org.example.musk.auth.vo.req.register.UsernamePasswordRegisterRequest;
import org.example.musk.auth.vo.req.register.VerificationCodeRegisterRequest;
import org.example.musk.auth.vo.res.RegisterResponseDTO;
import org.example.musk.auth.vo.result.RegistrationResult;
import org.example.musk.auth.web.anno.PassToken;
import org.example.musk.common.exception.BusinessPageExceptionEnum;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.util.servlet.ServletUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员注册控制器
 *
 * @author musk
 */
@RestController
@RequestMapping("/api/auth/register")
@Slf4j
public class MemberRegistrationController {

    @Resource
    private UnifiedRegistrationChain unifiedRegistrationChain;

    @Resource
    private VerificationCodeService verificationCodeService;

    @Resource
    private CaptchaService captchaService;

    /**
     * 用户名密码注册
     *
     * @param request         HTTP请求
     * @param registerRequest 注册请求
     * @return 注册结果
     */
    @PostMapping("/username")
    @PassToken
    public CommonResult<RegisterResponseDTO> registerByUsername(HttpServletRequest request,
                                                                @RequestBody @Valid UsernamePasswordRegisterRequest registerRequest) {
        log.info("[用户名密码注册] 开始处理注册请求，username={}", registerRequest.getUsername());

        // 设置注册类型
        registerRequest.setAuthType(AuthTypeEnum.USERNAME_PASSWORD_REGISTER);

        // 设置客户端IP
        registerRequest.setClientIp(getClientIp(request));

        // 执行注册
        RegistrationResult result = unifiedRegistrationChain.register(registerRequest);

        // 处理注册结果
        return handleRegisterResult(result);
    }

    /**
     * 邮箱验证码注册
     *
     * @param request         HTTP请求
     * @param registerRequest 注册请求
     * @return 注册结果
     */
    @PostMapping("/email")
    @PassToken
    public CommonResult<RegisterResponseDTO> registerByEmail(HttpServletRequest request,
                                                             @RequestBody @Valid VerificationCodeRegisterRequest registerRequest) {
        log.info("[邮箱验证码注册] 开始处理注册请求，email={}", registerRequest.getTarget());

        // 设置注册类型
        registerRequest.setAuthType(AuthTypeEnum.EMAIL_CODE_REGISTER);
        registerRequest.setChannel(CodeChannelEnum.EMAIL);

        // 设置客户端IP
        registerRequest.setClientIp(getClientIp(request));

        // 执行注册
        RegistrationResult result = unifiedRegistrationChain.register(registerRequest);

        // 处理注册结果
        return handleRegisterResult(result);
    }

    /**
     * 短信验证码注册
     *
     * @param request         HTTP请求
     * @param registerRequest 注册请求
     * @return 注册结果
     */
    @PostMapping("/sms")
    @PassToken
    public CommonResult<RegisterResponseDTO> registerBySms(HttpServletRequest request,
                                                           @RequestBody @Valid VerificationCodeRegisterRequest registerRequest) {
        log.info("[短信验证码注册] 开始处理注册请求，phone={}", registerRequest.getTarget());

        // 设置注册类型
        registerRequest.setAuthType(AuthTypeEnum.SMS_CODE_REGISTER);
        registerRequest.setChannel(CodeChannelEnum.SMS);

        // 设置客户端IP
        registerRequest.setClientIp(getClientIp(request));

        // 执行注册
        RegistrationResult result = unifiedRegistrationChain.register(registerRequest);

        // 处理注册结果
        return handleRegisterResult(result);
    }

    /**
     * 生成图形验证码
     *
     * @return 验证码信息
     */
    @GetMapping("/captcha/generate")
    @PassToken
    public CommonResult<Map<String, Object>> generateCaptcha() {
        log.info("[图形验证码] 开始生成验证码");

        try {
            CaptchaService.CaptchaInfo captchaInfo = captchaService.generateCaptcha();

            Map<String, Object> result = new HashMap<>();
            result.put("sessionId", captchaInfo.getSessionId());
            result.put("imageBase64", captchaInfo.getImageBase64());
            result.put("expireSeconds", captchaInfo.getExpireSeconds());

            log.info("[图形验证码] 生成成功，sessionId={}", captchaInfo.getSessionId());
            return CommonResult.success(result);

        } catch (Exception e) {
            log.error("[图形验证码] 生成失败", e);
            return CommonResult.error(BusinessPageExceptionEnum.GENERATOR_CAPTCHA_FAIL);
        }
    }

    /**
     * 发送注册验证码
     *
     * @param request     HTTP请求
     * @param sendRequest 发送请求
     * @return 发送结果
     */
    @PostMapping("/code/send")
    @PassToken
    public CommonResult<Boolean> sendRegisterCode(HttpServletRequest request,
                                                  @RequestBody @Valid SendCodeRequest sendRequest) {
        log.info("[发送注册验证码] 开始发送验证码，target={}, channel={}",
                sendRequest.getTarget(), sendRequest.getChannel().getCode());

        // 设置注册场景
        sendRequest.setScene(CodeSceneEnum.REGISTER);

        // 设置客户端IP
        sendRequest.setClientIp(getClientIp(request));

        // 发送验证码
        boolean success = verificationCodeService.sendCode(sendRequest);

        if (success) {
            log.info("[发送注册验证码] 发送成功，target={}", sendRequest.getTarget());
            return CommonResult.success(true);
        } else {
            log.warn("[发送注册验证码] 发送失败，target={}", sendRequest.getTarget());
            //return CommonResult.error("验证码发送失败");
            return CommonResult.error(BusinessPageExceptionEnum.GENERATOR_CAPTCHA_FAIL);

        }
    }

    /**
     * 获取支持的注册类型
     *
     * @return 支持的注册类型列表
     */
    @GetMapping("/types")
    @PassToken
    public CommonResult<List<AuthTypeEnum>> getSupportedRegisterTypes() {
        List<AuthTypeEnum> supportedTypes = unifiedRegistrationChain.getSupportedRegisterTypes();
        return CommonResult.success(supportedTypes);
    }

    /**
     * 处理注册结果
     *
     * @param result 注册结果
     * @return 响应结果
     */
    private CommonResult<RegisterResponseDTO> handleRegisterResult(RegistrationResult result) {
        if (!result.isSuccess()) {
            log.warn("[注册处理] 注册失败，errorCode={}, errorMessage={}",
                    result.getErrorCode(), result.getErrorMessage());
            return CommonResult.error(Integer.parseInt(result.getErrorCode()), result.getErrorMessage());
        }

        try {
            // 构建响应DTO
            RegisterResponseDTO responseDTO = new RegisterResponseDTO();
            responseDTO.setMemberCode(result.getMember().getMemberCode());
            responseDTO.setMemberNickName(result.getMember().getMemberNickName());
            responseDTO.setAvatar(result.getMember().getAvatar());
            responseDTO.setRegisterTime(LocalDateTime.now());

            // 处理自动登录
            if (result.isAutoLogin() && result.getToken() != null) {
                responseDTO.setAutoLogin(true);
                responseDTO.setToken(result.getToken());
                responseDTO.setTokenType("Bearer");
                // 这里可以设置token过期时间
                responseDTO.setExpiresIn(7200L); // 2小时
            } else {
                responseDTO.setAutoLogin(false);
            }

            log.info("[注册处理] 注册成功，memberId={}, memberCode={}",
                    result.getMember().getId(), result.getMember().getMemberCode());

            return CommonResult.success(responseDTO);

        } catch (Exception e) {
            log.error("[注册处理] 处理注册结果异常", e);
            return CommonResult.error(BusinessPageExceptionEnum.COMMON_RESULT_BOOL_IS_FALSE);
        }
    }

    /**
     * 获取客户端IP
     *
     * @param request HTTP请求
     * @return 客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        return ServletUtils.getClientIP(request);
    }
}
