package org.example.musk.functions.member.level.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.functions.member.level.model.entity.MemberLevelBenefitDO;
import org.example.musk.functions.member.level.model.vo.MemberLevelBenefitCreateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelBenefitUpdateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelBenefitVO;
import org.example.musk.functions.member.level.service.MemberLevelBenefitService;
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
 * 会员等级权益 Controller
 *
 * @author musk-functions-member-level
 */
@RestController
@RequestMapping("/member/level/benefit")
public class MemberLevelBenefitController {

    @Resource
    private MemberLevelBenefitService memberLevelBenefitService;

    @PostMapping("/create")
    public CommonResult<Integer> createLevelBenefit(@Valid @RequestBody MemberLevelBenefitCreateReqVO createReqVO) {
        return CommonResult.success(memberLevelBenefitService.createLevelBenefit(ThreadLocalTenantContext.getTenantId(),ThreadLocalTenantContext.getDomainId(),createReqVO));
    }

    @PutMapping("/update")
    public CommonResult<Boolean> updateLevelBenefit(@Valid @RequestBody MemberLevelBenefitUpdateReqVO updateReqVO) {
        memberLevelBenefitService.updateLevelBenefit(ThreadLocalTenantContext.getTenantId(),ThreadLocalTenantContext.getDomainId(),updateReqVO);
        return CommonResult.success(true);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResult<Boolean> deleteLevelBenefit(@PathVariable("id") Integer id) {
        memberLevelBenefitService.deleteLevelBenefit(ThreadLocalTenantContext.getDomainId(),id);
        return CommonResult.success(true);
    }

    @GetMapping("/get/{id}")
    public CommonResult<MemberLevelBenefitDO> getLevelBenefit(@PathVariable("id") Integer id) {
        return CommonResult.success(memberLevelBenefitService.getLevelBenefit(id));
    }

    @GetMapping("/list")
    public CommonResult<List<MemberLevelBenefitDO>> getLevelBenefitList(@RequestParam("levelId") Integer levelId) {
        return CommonResult.success(memberLevelBenefitService.getLevelBenefitList(levelId));
    }

    @GetMapping("/member-current-benefits")
    public CommonResult<List<MemberLevelBenefitVO>> getMemberCurrentBenefits(@RequestParam("memberId") Integer memberId) {
        return CommonResult.success(memberLevelBenefitService.getMemberCurrentBenefits(ThreadLocalTenantContext.getDomainId(),memberId));
    }

    @GetMapping("/has-benefit")
    public CommonResult<Boolean> hasBenefit(
            @RequestParam("memberId") Integer memberId,
            @RequestParam("benefitType") Integer benefitType) {
        return CommonResult.success(memberLevelBenefitService.hasBenefit(ThreadLocalTenantContext.getDomainId(),memberId, benefitType));
    }

    @GetMapping("/get-benefit-value")
    public CommonResult<String> getBenefitValue(
            @RequestParam("memberId") Integer memberId,
            @RequestParam("benefitType") Integer benefitType) {
        return CommonResult.success(memberLevelBenefitService.getBenefitValue(ThreadLocalTenantContext.getDomainId(),memberId, benefitType));
    }
}
