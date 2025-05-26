package org.example.musk.auth.service.core.strategy.impl;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.entity.channel.MemberAuthChannelDO;
import org.example.musk.auth.entity.member.MemberDO;
import org.example.musk.auth.entity.member.vo.MemberSaveReqVO;
import org.example.musk.auth.enums.MemberStatusEnums;
import org.example.musk.auth.enums.auth.AuthChannelTypeEnum;
import org.example.musk.auth.enums.auth.AuthTypeEnum;
import org.example.musk.auth.enums.code.CodeChannelEnum;
import org.example.musk.auth.enums.code.CodeSceneEnum;
import org.example.musk.auth.service.core.channel.MemberAuthChannelService;
import org.example.musk.auth.service.core.code.VerificationCodeService;
import org.example.musk.auth.service.core.helper.MemberHelper;
import org.example.musk.auth.service.core.helper.MemberSimpleIdHelper;
import org.example.musk.auth.service.core.helper.member.MemberNickNameHelper;
import org.example.musk.auth.service.core.member.MemberService;
import org.example.musk.auth.service.core.strategy.AuthenticationStrategy;
import org.example.musk.auth.vo.req.auth.VerificationCodeAuthRequest;
import org.example.musk.auth.vo.req.code.VerifyCodeRequest;
import org.example.musk.auth.vo.result.AuthenticationResult;
import org.example.musk.common.exception.BusinessPageExceptionEnum;
import org.springframework.stereotype.Component;

/**
 * 邮箱验证码认证策略
 *
 * @author musk
 */
@Component
@Slf4j
public class EmailCodeAuthStrategy implements AuthenticationStrategy<VerificationCodeAuthRequest> {

    @Resource
    private VerificationCodeService verificationCodeService;

    @Resource
    private MemberAuthChannelService memberAuthChannelService;

    @Resource
    private MemberService memberService;

    @Resource
    private MemberHelper memberHelper;

    @Resource
    private MemberSimpleIdHelper memberSimpleIdHelper;

    @Override
    public boolean supports(AuthTypeEnum authType) {
        return AuthTypeEnum.EMAIL_CODE.equals(authType);
    }

    @Override
    public AuthenticationResult authenticate(VerificationCodeAuthRequest request) {
        log.info("[邮箱验证码认证] 开始认证，email={}", request.getTarget());

        try {
            // 前置处理
            if (!preProcess(request)) {
                return AuthenticationResult.failure(
                        BusinessPageExceptionEnum.AUTH_PRE_PROCESS_FAILED.getExCode(),
                        "认证前置处理失败"
                );
            }

            // 验证验证码
            VerifyCodeRequest verifyRequest = new VerifyCodeRequest();
            verifyRequest.setTarget(request.getTarget());
            verifyRequest.setCode(request.getVerificationCode());
            verifyRequest.setChannel(request.getChannel());
            verifyRequest.setScene(CodeSceneEnum.LOGIN);
            verifyRequest.setClientIp(request.getClientIp());

            if (!verificationCodeService.verifyCode(verifyRequest)) {
                log.warn("[邮箱验证码认证] 验证码验证失败，email={}, code={}",
                        request.getTarget(), request.getVerificationCode());
                return AuthenticationResult.failure(
                        BusinessPageExceptionEnum.VERIFICATION_CODE_ERROR.getExCode(),
                        "验证码错误或已过期"
                );
            }

            // 查找或创建认证渠道
            MemberAuthChannelDO authChannel = memberAuthChannelService.findAuthChannel(
                    AuthChannelTypeEnum.EMAIL, request.getTarget());

            // 获取或创建会员
            MemberDO member = getOrCreateMember(request, authChannel);
            if (member == null) {
                log.error("[邮箱验证码认证] 获取或创建会员失败，email={}", request.getTarget());
                return AuthenticationResult.failure(
                        BusinessPageExceptionEnum.COMMON_RESULT_BOOL_IS_FALSE.getExCode(),
                        "系统异常，请稍后重试"
                );
            }

            // 检查会员状态
            if (!isMemberActive(member)) {
                log.warn("[邮箱验证码认证] 会员状态异常，email={}, status={}",
                        request.getTarget(), member.getStatus());
                return AuthenticationResult.failure(
                        BusinessPageExceptionEnum.MEMBER_STATUS_ABNORMAL.getExCode(),
                        "账户状态异常，请联系客服"
                );
            }

            // 认证成功
            AuthenticationResult result = AuthenticationResult.success(member);

            // 后置处理
            postProcess(request, result);

            log.info("[邮箱验证码认证] 认证成功，email={}, memberId={}",
                    request.getTarget(), member.getId());
            return result;

        } catch (Exception e) {
            log.error("[邮箱验证码认证] 认证异常，email={}", request.getTarget(), e);
            return AuthenticationResult.failure(
                    BusinessPageExceptionEnum.COMMON_RESULT_BOOL_IS_FALSE.getExCode(),
                    "系统异常，请稍后重试"
            );
        }
    }

    @Override
    public MemberDO getOrCreateMember(VerificationCodeAuthRequest request, MemberAuthChannelDO authChannel) {
        // 如果认证渠道存在，直接返回对应的会员
        if (authChannel != null) {
            MemberDO existingMember = memberAuthChannelService.findMemberByChannel(
                    AuthChannelTypeEnum.EMAIL, request.getTarget());
            if (existingMember != null) {
                // 更新邮箱验证状态
                memberAuthChannelService.updateVerifiedStatus(
                        AuthChannelTypeEnum.EMAIL, request.getTarget(), true);
                return existingMember;
            }
        }

        // 创建新会员
        return createNewMember(request);
    }

    @Override
    public boolean preProcess(VerificationCodeAuthRequest request) {
        // 参数校验
        if (!request.isValidTarget() || StrUtil.isBlank(request.getVerificationCode())) {
            log.warn("[邮箱验证码认证] 参数校验失败，email={}", request.getTarget());
            return false;
        }

        // 检查渠道类型
        if (!CodeChannelEnum.EMAIL.equals(request.getChannel())) {
            log.warn("[邮箱验证码认证] 渠道类型错误，expected=EMAIL, actual={}",
                    request.getChannel().getCode());
            return false;
        }

        return true;
    }

    @Override
    public void postProcess(VerificationCodeAuthRequest request, AuthenticationResult result) {
        if (result.isSuccess()) {
            // 更新最后使用时间
            memberAuthChannelService.updateLastUsedTime(
                    AuthChannelTypeEnum.EMAIL, request.getTarget());

            // TODO: 可以在这里添加其他后置处理逻辑
            // 1. 记录登录日志
            // 2. 更新会员最后登录时间和IP
            // 3. 发送登录通知
            // 4. 统计登录次数
        }
    }

    /**
     * 创建新会员
     *
     * @param request 认证请求
     * @return 新创建的会员
     */
    private MemberDO createNewMember(VerificationCodeAuthRequest request) {
        log.info("[邮箱验证码认证] 开始创建新会员，email={}", request.getTarget());

        try {
            // 构建会员保存请求
            MemberSaveReqVO memberSaveReqVO = new MemberSaveReqVO();
            memberSaveReqVO.setMemberCode(generateMemberCode(request.getTarget()));
            memberSaveReqVO.setMemberNickName(MemberNickNameHelper.generatorName());
            //TODO
            /*memberSaveReqVO.setMemberSimpleId(memberSimpleIdHelper.generateSimpleId(
                    String.valueOf(request.getRegisterChannel())));*/
            memberSaveReqVO.setAvatar(memberHelper.getDefaultAvatar());
            memberSaveReqVO.setStatus(MemberStatusEnums.EFFECTIVE.getStatus());
            /* memberSaveReqVO.setRegisterChannel(request.getRegisterChannel());*/

            // 创建会员
            MemberDO newMember = memberService.register(memberSaveReqVO);
            if (newMember == null) {
                log.error("[邮箱验证码认证] 创建会员失败，email={}", request.getTarget());
                return null;
            }

            // 添加邮箱认证渠道
            memberAuthChannelService.addAuthChannel(
                    newMember.getId(),
                    AuthChannelTypeEnum.EMAIL,
                    request.getTarget(),
                    request.getTarget(), // 邮箱渠道的值就是邮箱地址本身
                    true // 通过验证码验证，标记为已验证
            );

            log.info("[邮箱验证码认证] 创建新会员成功，email={}, memberId={}",
                    request.getTarget(), newMember.getId());
            return newMember;

        } catch (Exception e) {
            log.error("[邮箱验证码认证] 创建新会员异常，email={}", request.getTarget(), e);
            return null;
        }
    }

    /**
     * 生成会员编码
     *
     * @param email 邮箱地址
     * @return 会员编码
     */
    private String generateMemberCode(String email) {
        // 使用邮箱前缀作为会员编码的基础
        String prefix = email.substring(0, email.indexOf("@"));
        // 添加时间戳确保唯一性
        return prefix + "_" + System.currentTimeMillis();
    }

    /**
     * 检查会员是否处于活跃状态
     *
     * @param member 会员信息
     * @return true-活跃，false-非活跃
     */
    private boolean isMemberActive(MemberDO member) {
        // 这里需要根据实际的会员状态枚举来判断
        // 假设状态值：1-正常，2-冻结，3-注销
        return member.getStatus() != null && member.getStatus() == 1;
    }
}
