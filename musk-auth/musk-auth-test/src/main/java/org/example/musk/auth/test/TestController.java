package org.example.musk.auth.test;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.example.musk.auth.web.anno.LimitRateLogin;
import org.example.musk.auth.web.authentication.AuthenticationController;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.auth.web.vo.req.LoginRequestDTO;
import org.example.musk.auth.web.vo.req.RegisterRequestDTO;
import org.example.musk.auth.web.vo.res.LoginResponseDTO;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: HomeController
 *
 * @author
 * @Description:
 * @date 2024年11月13日
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Resource
    private AuthenticationController authenticationController;


    @SneakyThrows
    @PostMapping("/login")
    @LimitRateLogin
    public CommonResult<LoginResponseDTO> login(HttpServletRequest request, @RequestBody LoginRequestDTO loginRequestDTO){
        ThreadLocalTenantContext.setMemberThread(123);
        ThreadLocalTenantContext.setTenantId(1);

        return authenticationController.login(request,loginRequestDTO);
    }

    /**
     * 注册
     *
     * @param request
     */
    @PostMapping("/register")
    public CommonResult<Boolean> register(HttpServletRequest request, @RequestBody @Valid @Validated RegisterRequestDTO registerRequestDTO) {
        ThreadLocalTenantContext.setTenantId(1);
        return authenticationController.register(request,registerRequestDTO);
    }
}
