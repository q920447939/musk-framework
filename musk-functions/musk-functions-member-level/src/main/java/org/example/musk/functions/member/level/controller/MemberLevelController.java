package org.example.musk.functions.member.level.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.pojo.db.PageParam;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.member.level.model.entity.MemberLevelDefinitionDO;
import org.example.musk.functions.member.level.model.vo.MemberLevelChangeRecordVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelDefinitionCreateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelDefinitionUpdateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelInfoVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelProgressVO;
import org.example.musk.functions.member.level.service.MemberLevelService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 会员等级 Controller
 *
 * @author musk-functions-member-level
 */
@RestController
@RequestMapping("/member/level")
public class MemberLevelController {

    @Resource
    private MemberLevelService memberLevelService;

    @PostMapping("/create")
    public CommonResult<Integer> createLevelDefinition(@Valid @RequestBody MemberLevelDefinitionCreateReqVO createReqVO) {
        return CommonResult.success(memberLevelService.createLevelDefinition(ThreadLocalTenantContext.getTenantId(),ThreadLocalTenantContext.getDomainId(),createReqVO));
    }

    @PutMapping("/update")
    public CommonResult<Boolean> updateLevelDefinition(@Valid @RequestBody MemberLevelDefinitionUpdateReqVO updateReqVO) {
        memberLevelService.updateLevelDefinition(ThreadLocalTenantContext.getTenantId(),ThreadLocalTenantContext.getDomainId(),updateReqVO);
        return CommonResult.success(true);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResult<Boolean> deleteLevelDefinition(@PathVariable("id") Integer id) {
        memberLevelService.deleteLevelDefinition(ThreadLocalTenantContext.getTenantId(),ThreadLocalTenantContext.getDomainId(),id);
        return CommonResult.success(true);
    }

    @GetMapping("/get/{id}")
    public CommonResult<MemberLevelDefinitionDO> getLevelDefinition(@PathVariable("id") Integer id) {
        return CommonResult.success(memberLevelService.getLevelDefinition(id));
    }

    @GetMapping("/list")
    public CommonResult<List<MemberLevelDefinitionDO>> getLevelDefinitionList(
            @RequestParam(value = "tenantId", required = false) Integer tenantId,
            @RequestParam(value = "domainId", required = false) Integer domainId) {
        return CommonResult.success(memberLevelService.getLevelDefinitionList(tenantId, domainId));
    }

    @GetMapping("/member-current-level")
    public CommonResult<MemberLevelInfoVO> getMemberCurrentLevel(@RequestParam("memberId") Integer memberId) {
        return CommonResult.success(memberLevelService.getMemberCurrentLevel(ThreadLocalTenantContext.getDomainId(),memberId));
    }

    @PostMapping("/set-member-level")
    public CommonResult<Boolean> setMemberLevel(
            @RequestParam("memberId") Integer memberId,
            @RequestParam("levelId") Integer levelId,
            @RequestParam("reason") String reason,
            @RequestParam("operator") String operator) {
        memberLevelService.setMemberLevel(ThreadLocalTenantContext.getTenantId(),ThreadLocalTenantContext.getDomainId(),memberId, levelId, reason, operator);
        return CommonResult.success(true);
    }

    @GetMapping("/calculate-member-level")
    public CommonResult<Integer> calculateMemberLevel(@RequestParam("memberId") Integer memberId) {
        return CommonResult.success(memberLevelService.calculateMemberLevel(ThreadLocalTenantContext.getDomainId(),memberId));
    }

    @GetMapping("/member-level-change-history")
    public CommonResult<PageResult<MemberLevelChangeRecordVO>> getMemberLevelChangeHistory(
            @RequestParam("memberId") Integer memberId, PageParam pageParam) {
        return CommonResult.success(memberLevelService.getMemberLevelChangeHistory(
                memberId, pageParam.getPageNo(), pageParam.getPageSize()));
    }

    @GetMapping("/calculate-member-level-progress")
    public CommonResult<MemberLevelProgressVO> calculateMemberLevelProgress(@RequestParam("memberId") Integer memberId) {
        return CommonResult.success(memberLevelService.calculateMemberLevelProgress(ThreadLocalTenantContext.getDomainId(),memberId));
    }
}
