package org.example.musk.auth.web.controller.authentication;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.enums.MemberStatusEnums;
import org.example.musk.auth.service.core.member.MemberService;
import org.example.musk.auth.web.anno.LimitRateLogin;
import org.example.musk.auth.web.anno.PassToken;
import org.example.musk.auth.web.helper.MemberHelper;
import org.example.musk.auth.web.helper.MemberSimpleIdHelper;
import org.example.musk.auth.web.helper.member.MemberNickNameHelper;
import org.example.musk.auth.web.loginChain.LoginChainDecorator;
import org.example.musk.auth.web.loginChain.entity.LoginChainRequest;
import org.example.musk.auth.web.loginChain.entity.LoginChainResult;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.auth.web.utils.RequestUtils;
import org.example.musk.auth.web.utils.VertifyCodeUtils;
import org.example.musk.auth.web.vo.req.LoginRequestDTO;
import org.example.musk.auth.web.vo.req.RegisterRequestDTO;
import org.example.musk.auth.web.vo.res.LoginResponseDTO;
import org.example.musk.auth.entity.member.MemberDO;
import org.example.musk.auth.entity.member.vo.MemberSaveReqVO;
import org.example.musk.auth.event.login.fail.entity.LoginFailEventInfo;
import org.example.musk.auth.event.login.succ.entity.LoginSuccEventInfo;
import org.example.musk.auth.event.register.entity.RegisterSuccInfo;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.middleware.redis.RedisUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import static org.example.musk.auth.web.constants.cache.LoginConstant.VALIDATE_CODE_GENERATE_KEY;
import  static  org.example.musk.common.exception.BusinessPageExceptionEnum.*;


@CrossOrigin(allowCredentials = "true", originPatterns = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/authentication")
@Slf4j
public class AuthenticationController {


    @Resource
    private LoginChainDecorator loginChainDecorator;


    @Resource
    private MemberService memberService;

    @Resource
    private MemberHelper memberHelper;
    @Resource
    private MemberSimpleIdHelper memberSimpleIdHelper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ApplicationContext applicationContext;


    /**
     * 常规登录接口
     *
     * @param request
     */
    @PostMapping("/login")
    @LimitRateLogin
    @PassToken
    public CommonResult<LoginResponseDTO> login(HttpServletRequest request, @RequestBody LoginRequestDTO loginRequestDTO) throws Exception {
        Assert.notNull(loginRequestDTO);
        /*if (!AppParamConfigUtil.queryAppParamsConfigByTypeValue1ToBoolean(org.example.auth.context.ThreadLocalTenantContext.getTenantId(), AppParamsConfigTypeEnums.LOGIN_VALIDATE_CODE_SKIP)) {
            this.checkVerificationCode(request, loginRequestDTO.getVerificationCode());
        }*/
        //为空 ，默认传过来的密码是md5后的值
        return this.mergeLogin(request, loginRequestDTO);
    }

    private boolean isSkipVerificationCode() {
        return true;
    }

    /**
     * 登录接口
     */
    public CommonResult<LoginResponseDTO> mergeLogin(HttpServletRequest request, LoginRequestDTO loginRequestDTO) {
        LoginChainRequest chainRequest = new LoginChainRequest();
        chainRequest.setRequest(request);
        chainRequest.setUsername(loginRequestDTO.getUserName());
        chainRequest.setPassword(loginRequestDTO.getPassword());
        LoginChainResult result = loginChainDecorator.auth(chainRequest);
        if (!result.isSuccess()) {
            applicationContext.publishEvent(LoginFailEventInfo.builder().request(request).loginRequestDTO(BeanUtil.toBean(loginRequestDTO, org.example.musk.auth.event.login.succ.entity.LoginRequestDTO.class)).failResult(JSONObject.from(result.getResult())).build());
            CommonResult commonResult = result.getResult();
            throw new BusinessException(String.valueOf(commonResult.getCode()), commonResult.getMsg());
        }
        CommonResult commonResult = result.getResult();
        MemberDO memberDO = (MemberDO) commonResult.getData();

        applicationContext.publishEvent(LoginSuccEventInfo.builder().memberDO(memberDO).request(request).loginRequestDTO(BeanUtil.toBean(loginRequestDTO, org.example.musk.auth.event.login.succ.entity.LoginRequestDTO.class)).build());

        StpUtil.login(memberDO.getId());
        // 第2步，获取 Token  相关参数
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 第3步，返回给前端
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken(tokenInfo.tokenValue);
        return CommonResult.success(loginResponseDTO);
    }


    /**
     * 注册
     *
     * @param request
     */
    @PostMapping("/register")
    @PassToken
    public CommonResult<Boolean> register(HttpServletRequest request, @RequestBody @Valid @Validated RegisterRequestDTO registerRequestDTO) {
        Assert.notNull(registerRequestDTO);
        /**
         * 判断账号密码是否未空，账号是否是ascial码 ，并且账号密码是长度是 6-30之间
         */
        if (StrUtil.isBlank(registerRequestDTO.getUserName()) || StrUtil.isBlank(registerRequestDTO.getPassword())
                || registerRequestDTO.getUserName().length() < 6 || registerRequestDTO.getUserName().length() > 30
                || registerRequestDTO.getPassword().length() < 6 || registerRequestDTO.getPassword().length() > 30) {
            throw new BusinessException(REGISTER_PARAMS_ERROR);
        }
        MemberSaveReqVO memberSaveReqVO = BeanUtil.toBean(registerRequestDTO, MemberSaveReqVO .class);

        memberSaveReqVO.setMemberCode(registerRequestDTO.getUserName());
        memberSaveReqVO.setMemberNickName(StrUtil.isNotBlank(registerRequestDTO.getNickName()) ?  registerRequestDTO.getNickName() : MemberNickNameHelper.generatorName());
        memberSaveReqVO.setMemberSimpleId(memberSimpleIdHelper.generateSimpleId(String.valueOf(registerRequestDTO.getRegisterChannel())));
        memberSaveReqVO.setAvatar(memberHelper.getDefaultAvatar());
        memberSaveReqVO.setStatus(MemberStatusEnums.EFFECTIVE.getStatus());
        MemberDO memberDO = memberService.register(memberSaveReqVO);
        if (null == memberDO) {
            throw new BusinessException(REGISTER_FAIL);
        }
        RegisterSuccInfo registerSuccInfo = new RegisterSuccInfo();
        registerSuccInfo.setMemberDO(memberDO);
        registerSuccInfo.setInviteMemberSimpleId(registerRequestDTO.getInviteMemberSimpleId());

        applicationContext.publishEvent(registerSuccInfo);
        return CommonResult.success(true);
    }

    /**
     * 验证码检查
     *
     * @param verificationCode
     * @return
     */
    private void checkVerificationCode(HttpServletRequest request, String verificationCode) {

        String clientIp = RequestUtils.getIpAddr(request);
        String key = String.format(VALIDATE_CODE_GENERATE_KEY, ThreadLocalTenantContext.getTenantId(), clientIp);
        Object o = redisUtil.get(key);
        if (null == o || !String.class.isAssignableFrom(o.getClass())) {
            throw new BusinessException(VERIFI_CATIONCODE_ERROR);
        }
        String cacheVerificationCode = (String) o;
        if (!StrUtil.equals(verificationCode, cacheVerificationCode)) {
            throw new BusinessException(VERIFI_CATIONCODE_ERROR);
        }
        redisUtil.del(key);
    }


    // 生成验证码
    @SneakyThrows
    @GetMapping("/generatorVerificationCode")
    @PassToken
    public CommonResult<String> generatorVerificationCode(HttpServletRequest request) {
        // 1. 使用工具类生成验证码
        String code = VertifyCodeUtils.generateVerifyCode(4);
        // 2. 将验证码放入session
        //在生成时，先将上一次生成的验证码移除，同一个ip ，只能生成一个验证码
        String clientIp = RequestUtils.getIpAddr(request);
        if (StrUtil.isBlank(clientIp)) {
            throw new BusinessException(GET_VERIFI_CATIONCODE_MISS);
        }
        String key = String.format(VALIDATE_CODE_GENERATE_KEY, ThreadLocalTenantContext.getTenantId(), clientIp);
        redisUtil.set(key, code, 60);
        // 3. 将图片转换成base64格式
        // 字节数组输出流在内存中创建一个字节数组缓冲区，所有发送到输出流的数据保存在该字节数组缓冲区中
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 将得到的验证码，使用工具类生成验证码图片，并放入到字节数组缓存区
        VertifyCodeUtils.outputImage(220, 60, byteArrayOutputStream, code);
        // 使用spring提供的工具类，将字节缓存数组中的验证码图片流转换成Base64的形式
        // 并返回给浏览器
        return CommonResult.success("data:image/png;base64," + Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
    }


    @GetMapping("/updateToken")
    public CommonResult<String> updateToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        if (StrUtil.isBlank(token)) {
            return CommonResult.successNoData();
        }
        StpUtil.renewTimeout(SpringUtil.getProperty("sa-token.timeout", Long.class,-1L));
        //生成新token
        return CommonResult.success(token);
    }
}
