package org.example.musk.functions.member.level.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.functions.member.level.model.entity.MemberPointsRuleDO;
import org.example.musk.functions.member.level.model.vo.MemberPointsRuleCreateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberPointsRuleUpdateReqVO;
import org.example.musk.functions.member.level.service.MemberPointsRuleService;
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
 * 积分规则 Controller
 *
 * @author musk-functions-member-level
 */
@RestController
@RequestMapping("/member/points/rule")
public class MemberPointsRuleController {

    @Resource
    private MemberPointsRuleService memberPointsRuleService;

    @PostMapping("/create")
    public CommonResult<Integer> createPointsRule(@Valid @RequestBody MemberPointsRuleCreateReqVO createReqVO) {
        return CommonResult.success(memberPointsRuleService.createPointsRule(createReqVO));
    }

    @PutMapping("/update")
    public CommonResult<Boolean> updatePointsRule(@Valid @RequestBody MemberPointsRuleUpdateReqVO updateReqVO) {
        memberPointsRuleService.updatePointsRule(updateReqVO);
        return CommonResult.success(true);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResult<Boolean> deletePointsRule(@PathVariable("id") Integer id) {
        memberPointsRuleService.deletePointsRule(id);
        return CommonResult.success(true);
    }

    @GetMapping("/get/{id}")
    public CommonResult<MemberPointsRuleDO> getPointsRule(@PathVariable("id") Integer id) {
        return CommonResult.success(memberPointsRuleService.getPointsRule(id));
    }

    @GetMapping("/list")
    public CommonResult<List<MemberPointsRuleDO>> getPointsRuleList() {
        return CommonResult.success(memberPointsRuleService.getPointsRuleList());
    }

    @PostMapping("/calculate-consumption-points")
    public CommonResult<Boolean> calculateConsumptionPointsAndGrowth(
            @RequestParam("memberId") Integer memberId,
            @RequestParam("amount") Integer amount,
            @RequestParam("sourceId") String sourceId,
            @RequestParam("operator") String operator) {
        memberPointsRuleService.calculateConsumptionPointsAndGrowth(memberId, amount, sourceId, operator);
        return CommonResult.success(true);
    }

    @PostMapping("/calculate-sign-in-points")
    public CommonResult<Boolean> calculateSignInPointsAndGrowth(
            @RequestParam("memberId") Integer memberId,
            @RequestParam("continuousDays") Integer continuousDays,
            @RequestParam("operator") String operator) {
        memberPointsRuleService.calculateSignInPointsAndGrowth(memberId, continuousDays, operator);
        return CommonResult.success(true);
    }
}
