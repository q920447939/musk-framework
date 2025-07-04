package org.example.musk.functions.member.level.controller;

import jakarta.annotation.Resource;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.pojo.db.PageParam;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.member.level.enums.GrowthValueSourceTypeEnum;
import org.example.musk.functions.member.level.model.vo.MemberGrowthValueRecordVO;
import org.example.musk.functions.member.level.model.vo.MemberGrowthValueVO;
import org.example.musk.functions.member.level.service.MemberGrowthValueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 会员成长值 Controller
 *
 * @author musk-functions-member-level
 */
@RestController
@RequestMapping("/member/growth-value")
public class MemberGrowthValueController {

    @Resource
    private MemberGrowthValueService memberGrowthValueService;

    @PostMapping("/add")
    public CommonResult<Integer> addGrowthValue(
            @RequestParam("memberId") Integer memberId,
            @RequestParam("growthValue") Integer growthValue,
            @RequestParam("sourceType") Integer sourceType,
            @RequestParam("sourceId") String sourceId,
            @RequestParam("description") String description,
            @RequestParam("operator") String operator) {
        // 将Integer类型的sourceType转换为枚举类型
        GrowthValueSourceTypeEnum sourceTypeEnum = GrowthValueSourceTypeEnum.getByValue(sourceType);
        if (sourceTypeEnum == null) {
            throw new IllegalArgumentException("无效的来源类型: " + sourceType);
        }
        return CommonResult.success(memberGrowthValueService.addGrowthValue(
                memberId, growthValue, sourceTypeEnum, sourceId, description, operator));
    }

    @PostMapping("/deduct")
    public CommonResult<Integer> deductGrowthValue(
            @RequestParam("memberId") Integer memberId,
            @RequestParam("growthValue") Integer growthValue,
            @RequestParam("sourceType") Integer sourceType,
            @RequestParam("sourceId") String sourceId,
            @RequestParam("description") String description,
            @RequestParam("operator") String operator) {
        // 将Integer类型的sourceType转换为枚举类型
        GrowthValueSourceTypeEnum sourceTypeEnum = GrowthValueSourceTypeEnum.getByValue(sourceType);
        if (sourceTypeEnum == null) {
            throw new IllegalArgumentException("无效的来源类型: " + sourceType);
        }
        return CommonResult.success(memberGrowthValueService.deductGrowthValue(
                memberId, growthValue, sourceTypeEnum, sourceId, description, operator));
    }

    @GetMapping("/get")
    public CommonResult<MemberGrowthValueVO> getMemberGrowthValue(@RequestParam("memberId") Integer memberId) {
        return CommonResult.success(memberGrowthValueService.getMemberGrowthValue(memberId));
    }

    @GetMapping("/history")
    public CommonResult<PageResult<MemberGrowthValueRecordVO>> getMemberGrowthValueHistory(
            @RequestParam("memberId") Integer memberId, PageParam pageParam) {
        return CommonResult.success(memberGrowthValueService.getMemberGrowthValueHistory(
                memberId, pageParam.getPageNo(), pageParam.getPageSize()));
    }
}
