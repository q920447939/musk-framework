package org.example.musk.functions.invitation.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.util.commonResult.CommonResultUtils;
import org.example.musk.functions.invitation.dao.entity.MemberInvitationCodeDO;
import org.example.musk.functions.invitation.dao.enums.InvitationCodeTypeEnum;
import org.example.musk.functions.invitation.service.InvitationCodeService;
import org.example.musk.functions.invitation.web.vo.request.InvitationCodeCreateReqVO;
import org.example.musk.functions.invitation.web.vo.response.InvitationCodeRespVO;
import org.example.musk.functions.invitation.web.vo.response.InvitationCodeSimpleRespVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 邀请码管理 Controller
 *
 * @author musk-functions-member-invitation
 */
@RestController
@RequestMapping("/api/invitation/code")
@Validated
@Slf4j
public class InvitationCodeController {

    @Resource
    private InvitationCodeService invitationCodeService;

    /**
     * 生成邀请码
     *
     * @param request 生成请求
     * @return 邀请码信息
     */
    @PostMapping("/generate")
    public CommonResult<InvitationCodeSimpleRespVO> generateInvitationCode(@RequestBody @Valid InvitationCodeCreateReqVO request) {
        Integer memberId = ThreadLocalTenantContext.getMemberId();

        InvitationCodeTypeEnum codeType = InvitationCodeTypeEnum.fromCode(request.getCodeType());
        if (codeType == null) {
            return CommonResult.error("邀请码类型无效");
        }

        MemberInvitationCodeDO codeEntity = invitationCodeService.generateInvitationCode(
                memberId, codeType, request.getMaxUseCount(), request.getExpireHours());

        InvitationCodeSimpleRespVO respVO = convertToSimpleRespVO(codeEntity);
        return CommonResult.success(respVO);
    }

    /**
     * 获取我的邀请码列表
     *
     * @return 邀请码列表
     */
    @GetMapping("/my")
    public CommonResult<List<InvitationCodeRespVO>> getMyInvitationCodes() {
        Integer memberId = ThreadLocalTenantContext.getMemberId();

        List<MemberInvitationCodeDO> codeList = invitationCodeService.getMemberInvitationCodes(memberId);
        List<InvitationCodeRespVO> respList = codeList.stream()
                .map(this::convertToRespVO)
                .collect(Collectors.toList());

        return CommonResult.success(respList);
    }

    /**
     * 验证邀请码
     *
     * @param invitationCode 邀请码
     * @return 验证结果
     */
    @GetMapping("/validate/{invitationCode}")
    public CommonResult<InvitationCodeRespVO> validateInvitationCode(@PathVariable String invitationCode) {
        MemberInvitationCodeDO codeEntity = invitationCodeService.validateInvitationCode(invitationCode);

        return CommonResultUtils.wrapEmptyObjResult(codeEntity, () -> convertToRespVO(codeEntity));
    }

    /**
     * 禁用邀请码
     *
     * @param id 邀请码ID
     * @return 操作结果
     */
    @PutMapping("/disable/{id}")
    public CommonResult<Boolean> disableInvitationCode(@PathVariable Long id) {
        boolean result = invitationCodeService.disableInvitationCode(id);
        return CommonResult.success(result);
    }

    /**
     * 启用邀请码
     *
     * @param id 邀请码ID
     * @return 操作结果
     */
    @PutMapping("/enable/{id}")
    public CommonResult<Boolean> enableInvitationCode(@PathVariable Long id) {
        boolean result = invitationCodeService.enableInvitationCode(id);
        return CommonResult.success(result);
    }

    /**
     * 根据ID获取邀请码
     *
     * @param id 邀请码ID
     * @return 邀请码信息
     */
    @GetMapping("/{id}")
    public CommonResult<InvitationCodeRespVO> getInvitationCodeById(@PathVariable Long id) {
        MemberInvitationCodeDO codeEntity = invitationCodeService.getInvitationCodeById(id);

        return CommonResultUtils.wrapEmptyObjResult(codeEntity, () -> convertToRespVO(codeEntity));
    }

    /**
     * 转换为响应VO
     *
     * @param entity 实体
     * @return 响应VO
     */
    private InvitationCodeRespVO convertToRespVO(MemberInvitationCodeDO entity) {
        if (entity == null) {
            return null;
        }

        return InvitationCodeRespVO.builder()
                .id(entity.getId())
                .invitationCode(entity.getInvitationCode())
                .inviterMemberId(entity.getInviterMemberId())
                .codeType(entity.getCodeType())
                .codeTypeName(InvitationCodeTypeEnum.fromCode(entity.getCodeType()).getDescription())
                .status(entity.getStatus())
                .maxUseCount(entity.getMaxUseCount())
                .usedCount(entity.getUsedCount())
                .expireTime(entity.getExpireTime())
                .createTime(entity.getCreateTime())
                .build();
    }
    private InvitationCodeSimpleRespVO convertToSimpleRespVO(MemberInvitationCodeDO entity) {
        if (entity == null) {
            return null;
        }

        return InvitationCodeSimpleRespVO.builder()
                .invitationCode(entity.getInvitationCode())
                .inviterMemberId(entity.getInviterMemberId())
                .codeType(entity.getCodeType())
                .codeTypeName(InvitationCodeTypeEnum.fromCode(entity.getCodeType()).getDescription())
                .status(entity.getStatus())
                .expireTime(entity.getExpireTime())
                .createTime(entity.getCreateTime())
                .build();
    }

}
