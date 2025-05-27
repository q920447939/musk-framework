package org.example.musk.auth.service.core.profile.impl;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.enums.OperationTypeEnum;
import org.example.musk.auth.enums.auth.AuthChannelTypeEnum;
import org.example.musk.auth.enums.code.CodeChannelEnum;
import org.example.musk.auth.enums.code.CodeSceneEnum;
import org.example.musk.auth.service.core.channel.MemberAuthChannelService;
import org.example.musk.auth.service.core.code.VerificationCodeService;
import org.example.musk.auth.service.core.member.MemberService;
import org.example.musk.auth.service.core.profile.MemberProfileService;
import org.example.musk.auth.service.core.security.MemberSecurityService;
import org.example.musk.auth.vo.req.code.VerifyCodeRequest;
import org.example.musk.auth.vo.req.profile.EmailManagementRequest;
import org.example.musk.auth.vo.req.profile.PhoneManagementRequest;
import org.example.musk.auth.vo.req.profile.UpdateBasicInfoRequest;
import org.example.musk.auth.vo.res.ContactManagementResponse;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.common.exception.BusinessPageExceptionEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员信息管理服务实现
 *
 * @author musk
 */
@Service
@Slf4j
public class MemberProfileServiceImpl implements MemberProfileService {

    @Resource
    private MemberService memberService;

    @Resource
    private MemberAuthChannelService memberAuthChannelService;

    @Resource
    private MemberSecurityService memberSecurityService;

    @Resource
    private VerificationCodeService verificationCodeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBasicInfo(Integer memberId, UpdateBasicInfoRequest request) {
        log.info("[修改基础信息] 开始修改基础信息，memberId={}", memberId);

        try {
            // 1. 验证会员是否存在
            var member = memberService.getMemberInfoByMemberId(memberId);
            if (member == null) {
                log.warn("[修改基础信息] 会员不存在，memberId={}", memberId);
                throw new BusinessException(BusinessPageExceptionEnum.MEMBER_NOT_EXISTS);
            }

            // 2. 更新昵称
            if (StrUtil.isNotBlank(request.getNickname())) {
                boolean updateResult = memberService.updateNickName(memberId, request.getNickname());
                if (!updateResult) {
                    log.warn("[修改基础信息] 更新昵称失败，memberId={}", memberId);
                    return false;
                }
            }

            // 3. 更新头像
            if (StrUtil.isNotBlank(request.getAvatar())) {
                boolean updateResult = memberService.updateAvatar(memberId, request.getAvatar());
                if (!updateResult) {
                    log.warn("[修改基础信息] 更新头像失败，memberId={}", memberId);
                    return false;
                }
            }

            // 4. 其他字段的更新需要扩展MemberService
            // 这里暂时只实现昵称和头像的更新

            log.info("[修改基础信息] 修改成功，memberId={}", memberId);
            return true;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("[修改基础信息] 修改异常，memberId={}", memberId, e);
            throw new BusinessException(BusinessPageExceptionEnum.COMMON_RESULT_BOOL_IS_FALSE);
        }
    }

    @Override
    public ContactManagementResponse manageEmail(Integer memberId, EmailManagementRequest request) {
        log.info("[邮箱管理] 开始处理邮箱管理请求，memberId={}, operation={}",
                memberId, request.getOperation().getCode());

        try {
            switch (request.getOperation()) {
                case BIND:
                    return handleBindEmail(memberId, request);
                case UNBIND:
                    return handleUnbindEmail(memberId, request);
                case CHANGE:
                    return handleChangeEmail(memberId, request);
                default:
                    throw new BusinessException(BusinessPageExceptionEnum.PARAMS_ERROR);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("[邮箱管理] 处理异常，memberId={}", memberId, e);
            throw new BusinessException(BusinessPageExceptionEnum.COMMON_RESULT_BOOL_IS_FALSE);
        }
    }

    @Override
    public ContactManagementResponse managePhone(Integer memberId, PhoneManagementRequest request) {
        log.info("[手机号管理] 开始处理手机号管理请求，memberId={}, operation={}",
                memberId, request.getOperation().getCode());

        try {
            switch (request.getOperation()) {
                case BIND:
                    return handleBindPhone(memberId, request);
                case UNBIND:
                    return handleUnbindPhone(memberId, request);
                case CHANGE:
                    return handleChangePhone(memberId, request);
                default:
                    throw new BusinessException(BusinessPageExceptionEnum.PARAMS_ERROR);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("[手机号管理] 处理异常，memberId={}", memberId, e);
            throw new BusinessException(BusinessPageExceptionEnum.COMMON_RESULT_BOOL_IS_FALSE);
        }
    }

    @Override
    public boolean bindEmail(Integer memberId, String email, String verificationCode) {
        log.info("[绑定邮箱] 开始绑定邮箱，memberId={}, email={}", memberId, email);

        try {
            // 1. 验证验证码
            VerifyCodeRequest verifyCodeRequest = new VerifyCodeRequest();
            verifyCodeRequest.setTarget(email);
            verifyCodeRequest.setCode(verificationCode);
            verifyCodeRequest.setScene(CodeSceneEnum.BIND_EMAIL);
            verifyCodeRequest.setChannel(CodeChannelEnum.EMAIL);

            if (!verificationCodeService.verifyCode(verifyCodeRequest)) {
                log.warn("[绑定邮箱] 验证码验证失败，memberId={}, email={}", memberId, email);
                throw new BusinessException(BusinessPageExceptionEnum.VERIFICATION_CODE_ERROR);
            }

            // 2. 检查邮箱是否已被使用
            if (memberAuthChannelService.findMemberByChannel(AuthChannelTypeEnum.EMAIL, email) != null) {
                log.warn("[绑定邮箱] 邮箱已被使用，email={}", email);
                throw new BusinessException(BusinessPageExceptionEnum.AUTH_CHANNEL_ALREADY_EXISTS);
            }

            // 3. 添加邮箱认证渠道
            Long channelId = memberAuthChannelService.addAuthChannel(
                    memberId, AuthChannelTypeEnum.EMAIL, email, "", true);

            if (channelId == null) {
                log.error("[绑定邮箱] 添加认证渠道失败，memberId={}, email={}", memberId, email);
                return false;
            }

            log.info("[绑定邮箱] 绑定成功，memberId={}, email={}, channelId={}", memberId, email, channelId);
            return true;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("[绑定邮箱] 绑定异常，memberId={}, email={}", memberId, email, e);
            return false;
        }
    }

    @Override
    public boolean unbindEmail(Integer memberId, String email, String password) {
        log.info("[解绑邮箱] 开始解绑邮箱，memberId={}, email={}", memberId, email);

        try {
            // 1. 验证密码
            if (!memberSecurityService.verifyPassword(memberId, password)) {
                log.warn("[解绑邮箱] 密码验证失败，memberId={}", memberId);
                throw new BusinessException(BusinessPageExceptionEnum.OLD_PASSWORD_ERROR);
            }

            // 2. 检查是否为最后一个认证渠道
            var authChannels = memberAuthChannelService.getMemberAuthChannels(memberId);
            if (authChannels.size() <= 1) {
                log.warn("[解绑邮箱] 不能解绑最后一个认证渠道，memberId={}", memberId);
                throw new BusinessException(BusinessPageExceptionEnum.CANNOT_UNBIND_LAST_AUTH_CHANNEL);
            }

            // 3. 解绑邮箱认证渠道
            boolean result = memberAuthChannelService.unbindAuthChannel(
                    memberId, AuthChannelTypeEnum.EMAIL, email);

            log.info("[解绑邮箱] 解绑结果，memberId={}, email={}, result={}", memberId, email, result);
            return result;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("[解绑邮箱] 解绑异常，memberId={}, email={}", memberId, email, e);
            return false;
        }
    }

    @Override
    public boolean changeEmail(Integer memberId, String currentEmail, String newEmail,
                              String verificationCode, String password) {
        log.info("[更换邮箱] 开始更换邮箱，memberId={}, currentEmail={}, newEmail={}",
                memberId, currentEmail, newEmail);

        try {
            // 1. 验证密码
            if (!memberSecurityService.verifyPassword(memberId, password)) {
                log.warn("[更换邮箱] 密码验证失败，memberId={}", memberId);
                throw new BusinessException(BusinessPageExceptionEnum.OLD_PASSWORD_ERROR);
            }

            // 2. 验证新邮箱验证码
            VerifyCodeRequest verifyCodeRequest = new VerifyCodeRequest();
            verifyCodeRequest.setTarget(newEmail);
            verifyCodeRequest.setCode(verificationCode);
            verifyCodeRequest.setScene(CodeSceneEnum.CHANGE_EMAIL);
            verifyCodeRequest.setChannel(CodeChannelEnum.EMAIL);

            if (!verificationCodeService.verifyCode(verifyCodeRequest)) {
                log.warn("[更换邮箱] 验证码验证失败，memberId={}, newEmail={}", memberId, newEmail);
                throw new BusinessException(BusinessPageExceptionEnum.VERIFICATION_CODE_ERROR);
            }

            // 3. 检查新邮箱是否已被使用
            if (memberAuthChannelService.findMemberByChannel(AuthChannelTypeEnum.EMAIL, newEmail) != null) {
                log.warn("[更换邮箱] 新邮箱已被使用，newEmail={}", newEmail);
                throw new BusinessException(BusinessPageExceptionEnum.AUTH_CHANNEL_ALREADY_EXISTS);
            }

            // 4. 更新邮箱认证渠道
            boolean result = memberAuthChannelService.updateChannelValue(
                    AuthChannelTypeEnum.EMAIL, currentEmail, newEmail);

            log.info("[更换邮箱] 更换结果，memberId={}, result={}", memberId, result);
            return result;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("[更换邮箱] 更换异常，memberId={}", memberId, e);
            return false;
        }
    }

    @Override
    public boolean bindPhone(Integer memberId, String phone, String verificationCode) {
        // 实现逻辑类似bindEmail，使用SMS渠道
        return false; // 暂时返回false，需要后续完善
    }

    @Override
    public boolean unbindPhone(Integer memberId, String phone, String password) {
        // 实现逻辑类似unbindEmail
        return false; // 暂时返回false，需要后续完善
    }

    @Override
    public boolean changePhone(Integer memberId, String currentPhone, String newPhone,
                              String verificationCode, String password) {
        // 实现逻辑类似changeEmail，使用SMS渠道
        return false; // 暂时返回false，需要后续完善
    }

    /**
     * 处理绑定邮箱请求
     */
    private ContactManagementResponse handleBindEmail(Integer memberId, EmailManagementRequest request) {
        boolean result = bindEmail(memberId, request.getNewEmail(), request.getVerificationCode());
        if (result) {
            return ContactManagementResponse.success("邮箱绑定成功", request.getNewEmail());
        } else {
            return ContactManagementResponse.needVerification("邮箱绑定失败", "请检查验证码是否正确");
        }
    }

    /**
     * 处理解绑邮箱请求
     */
    private ContactManagementResponse handleUnbindEmail(Integer memberId, EmailManagementRequest request) {
        boolean result = unbindEmail(memberId, request.getCurrentEmail(), request.getPassword());
        if (result) {
            return ContactManagementResponse.success("邮箱解绑成功", "");
        } else {
            return ContactManagementResponse.needVerification("邮箱解绑失败", "请检查密码是否正确");
        }
    }

    /**
     * 处理更换邮箱请求
     */
    private ContactManagementResponse handleChangeEmail(Integer memberId, EmailManagementRequest request) {
        boolean result = changeEmail(memberId, request.getCurrentEmail(), request.getNewEmail(),
                request.getVerificationCode(), request.getPassword());
        if (result) {
            return ContactManagementResponse.success("邮箱更换成功", request.getNewEmail());
        } else {
            return ContactManagementResponse.needVerification("邮箱更换失败", "请检查验证码和密码是否正确");
        }
    }

    /**
     * 处理绑定手机号请求
     */
    private ContactManagementResponse handleBindPhone(Integer memberId, PhoneManagementRequest request) {
        // 暂时返回需要验证的响应
        return ContactManagementResponse.needVerification("手机号绑定功能开发中", "请稍后再试");
    }

    /**
     * 处理解绑手机号请求
     */
    private ContactManagementResponse handleUnbindPhone(Integer memberId, PhoneManagementRequest request) {
        // 暂时返回需要验证的响应
        return ContactManagementResponse.needVerification("手机号解绑功能开发中", "请稍后再试");
    }

    /**
     * 处理更换手机号请求
     */
    private ContactManagementResponse handleChangePhone(Integer memberId, PhoneManagementRequest request) {
        // 暂时返回需要验证的响应
        return ContactManagementResponse.needVerification("手机号更换功能开发中", "请稍后再试");
    }
}
