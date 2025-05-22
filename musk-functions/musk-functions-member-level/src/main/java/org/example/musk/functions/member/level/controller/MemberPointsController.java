package org.example.musk.functions.member.level.controller;

import jakarta.annotation.Resource;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.pojo.db.PageParam;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.member.level.enums.PointsSourceTypeEnum;
import org.example.musk.functions.member.level.model.vo.MemberPointsRecordVO;
import org.example.musk.functions.member.level.model.vo.MemberPointsVO;
import org.example.musk.functions.member.level.service.MemberPointsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 会员积分 Controller
 *
 * @author musk-functions-member-level
 */
@RestController
@RequestMapping("/member/points")
public class MemberPointsController {

    @Resource
    private MemberPointsService memberPointsService;

    @PostMapping("/add")
    public CommonResult<Integer> addPoints(
            @RequestParam("memberId") Integer memberId,
            @RequestParam("points") Integer points,
            @RequestParam("sourceType") Integer sourceType,
            @RequestParam("sourceId") String sourceId,
            @RequestParam("description") String description,
            @RequestParam("operator") String operator) {
        // 将Integer类型的sourceType转换为枚举类型
        PointsSourceTypeEnum sourceTypeEnum = PointsSourceTypeEnum.getByValue(sourceType);
        if (sourceTypeEnum == null) {
            throw new IllegalArgumentException("无效的来源类型: " + sourceType);
        }
        return CommonResult.success(memberPointsService.addPoints(memberId, points, sourceTypeEnum, sourceId, description, operator));
    }

    @PostMapping("/deduct")
    public CommonResult<Integer> deductPoints(
            @RequestParam("memberId") Integer memberId,
            @RequestParam("points") Integer points,
            @RequestParam("sourceType") Integer sourceType,
            @RequestParam("sourceId") String sourceId,
            @RequestParam("description") String description,
            @RequestParam("operator") String operator) {
        // 将Integer类型的sourceType转换为枚举类型
        PointsSourceTypeEnum sourceTypeEnum = PointsSourceTypeEnum.getByValue(sourceType);
        if (sourceTypeEnum == null) {
            throw new IllegalArgumentException("无效的来源类型: " + sourceType);
        }
        return CommonResult.success(memberPointsService.deductPoints(memberId, points, sourceTypeEnum, sourceId, description, operator));
    }

    @PostMapping("/freeze")
    public CommonResult<Integer> freezePoints(
            @RequestParam("memberId") Integer memberId,
            @RequestParam("points") Integer points,
            @RequestParam("sourceType") Integer sourceType,
            @RequestParam("sourceId") String sourceId,
            @RequestParam("description") String description,
            @RequestParam("operator") String operator) {
        // 将Integer类型的sourceType转换为枚举类型
        PointsSourceTypeEnum sourceTypeEnum = PointsSourceTypeEnum.getByValue(sourceType);
        if (sourceTypeEnum == null) {
            throw new IllegalArgumentException("无效的来源类型: " + sourceType);
        }
        return CommonResult.success(memberPointsService.freezePoints(memberId, points, sourceTypeEnum, sourceId, description, operator));
    }

    @PostMapping("/unfreeze")
    public CommonResult<Integer> unfreezePoints(
            @RequestParam("memberId") Integer memberId,
            @RequestParam("points") Integer points,
            @RequestParam("sourceType") Integer sourceType,
            @RequestParam("sourceId") String sourceId,
            @RequestParam("description") String description,
            @RequestParam("operator") String operator) {
        // 将Integer类型的sourceType转换为枚举类型
        PointsSourceTypeEnum sourceTypeEnum = PointsSourceTypeEnum.getByValue(sourceType);
        if (sourceTypeEnum == null) {
            throw new IllegalArgumentException("无效的来源类型: " + sourceType);
        }
        return CommonResult.success(memberPointsService.unfreezePoints(memberId, points, sourceTypeEnum, sourceId, description, operator));
    }

    @PostMapping("/expire")
    public CommonResult<Integer> expirePoints(
            @RequestParam("memberId") Integer memberId,
            @RequestParam("points") Integer points,
            @RequestParam("sourceType") Integer sourceType,
            @RequestParam("sourceId") String sourceId,
            @RequestParam("description") String description,
            @RequestParam("operator") String operator) {
        // 将Integer类型的sourceType转换为枚举类型
        PointsSourceTypeEnum sourceTypeEnum = PointsSourceTypeEnum.getByValue(sourceType);
        if (sourceTypeEnum == null) {
            throw new IllegalArgumentException("无效的来源类型: " + sourceType);
        }
        return CommonResult.success(memberPointsService.expirePoints(memberId, points, sourceTypeEnum, sourceId, description, operator));
    }

    @GetMapping("/get")
    public CommonResult<MemberPointsVO> getMemberPoints(@RequestParam("memberId") Integer memberId) {
        return CommonResult.success(memberPointsService.getMemberPoints(memberId));
    }

    @GetMapping("/history")
    public CommonResult<PageResult<MemberPointsRecordVO>> getMemberPointsHistory(
            @RequestParam("memberId") Integer memberId, PageParam pageParam) {
        return CommonResult.success(memberPointsService.getMemberPointsHistory(
                memberId, pageParam.getPageNo(), pageParam.getPageSize()));
    }
}
