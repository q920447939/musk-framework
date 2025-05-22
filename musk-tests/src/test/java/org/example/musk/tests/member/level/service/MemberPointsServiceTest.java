package org.example.musk.tests.member.level.service;

import jakarta.annotation.Resource;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.member.level.enums.PointsSourceTypeEnum;
import org.example.musk.functions.member.level.model.vo.MemberPointsRecordVO;
import org.example.musk.functions.member.level.model.vo.MemberPointsVO;
import org.example.musk.functions.member.level.service.MemberPointsService;
import org.example.musk.tests.BaseTest;
import org.example.musk.tests.TestApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 会员积分服务测试类
 *
 * @author musk-functions-member-level
 */
public class MemberPointsServiceTest extends BaseTest {

    @Resource
    private MemberPointsService memberPointsService;

    /**
     * 测试增加会员积分
     */
    @Test
    public void testAddPoints() {
        // 增加会员积分
        Integer memberId = 1001;
        Integer points = 100;
        PointsSourceTypeEnum sourceType = PointsSourceTypeEnum.CONSUMPTION;
        String sourceId = "ORDER123456";
        String description = "购物奖励积分";
        String operator = "system";

        Integer availablePoints = memberPointsService.addPoints(
                memberId, points, sourceType, sourceId, description, operator);

        Assertions.assertNotNull(availablePoints);
        Assertions.assertTrue(availablePoints >= points);
    }

    /**
     * 测试减少会员积分
     */
    @Test
    public void testDeductPoints() {
        // 先增加积分，确保有足够的积分可以减少
        Integer memberId = 1001;
        Integer points = 50;
        PointsSourceTypeEnum sourceType = PointsSourceTypeEnum.ADMIN_ADJUST; // 使用管理员调整类型
        String sourceId = "ADJUST123456";
        String description = "系统调整积分";
        String operator = "system";

        // 先增加积分
        memberPointsService.addPoints(
                memberId, 100, sourceType, sourceId, "增加积分用于测试", operator);

        // 减少积分
        Integer availablePoints = memberPointsService.deductPoints(
                memberId, points, sourceType, sourceId, description, operator);

        Assertions.assertNotNull(availablePoints);
    }

    /**
     * 测试冻结会员积分
     */
    @Test
    public void testFreezePoints() {
        // 先增加积分，确保有足够的积分可以冻结
        Integer memberId = 1001;
        Integer points = 50;
        PointsSourceTypeEnum sourceType = PointsSourceTypeEnum.ADMIN_ADJUST; // 使用管理员调整类型
        String sourceId = "FREEZE123456";
        String description = "冻结积分";
        String operator = "system";

        // 先增加积分
        memberPointsService.addPoints(
                memberId, 100, sourceType, sourceId, "增加积分用于测试", operator);

        // 冻结积分
        Integer frozenPoints = memberPointsService.freezePoints(
                memberId, points, sourceType, sourceId, description, operator);

        Assertions.assertNotNull(frozenPoints);
        Assertions.assertTrue(frozenPoints >= points);
    }

    /**
     * 测试解冻会员积分
     */
    @Test
    public void testUnfreezePoints() {
        // 先增加并冻结积分，确保有足够的冻结积分可以解冻
        Integer memberId = 1001;
        Integer points = 50;
        PointsSourceTypeEnum sourceType = PointsSourceTypeEnum.ADMIN_ADJUST; // 使用管理员调整类型
        String sourceId = "UNFREEZE123456";
        String description = "解冻积分";
        String operator = "system";

        // 先增加积分
        memberPointsService.addPoints(
                memberId, 100, sourceType, sourceId, "增加积分用于测试", operator);

        // 冻结积分
        memberPointsService.freezePoints(
                memberId, points, sourceType, sourceId, "冻结积分用于测试", operator);

        // 解冻积分
        Integer availablePoints = memberPointsService.unfreezePoints(
                memberId, points, sourceType, sourceId, description, operator);

        Assertions.assertNotNull(availablePoints);
    }

    /**
     * 测试过期会员积分
     */
    @Test
    public void testExpirePoints() {
        // 先增加积分，确保有足够的积分可以过期
        Integer memberId = 1001;
        Integer points = 50;
        PointsSourceTypeEnum sourceType = PointsSourceTypeEnum.ADMIN_ADJUST; // 使用管理员调整类型
        String sourceId = "EXPIRE123456";
        String description = "过期积分";
        String operator = "system";

        // 先增加积分
        memberPointsService.addPoints(
                memberId, 100, sourceType, sourceId, "增加积分用于测试", operator);

        // 过期积分
        Integer availablePoints = memberPointsService.expirePoints(
                memberId, points, sourceType, sourceId, description, operator);

        Assertions.assertNotNull(availablePoints);
    }

    /**
     * 测试获取会员积分信息
     */
    @Test
    public void testGetMemberPoints() {
        // 获取会员积分信息
        Integer memberId = 1001;

        MemberPointsVO pointsVO = memberPointsService.getMemberPoints(memberId);

        Assertions.assertNotNull(pointsVO);
        Assertions.assertEquals(memberId, pointsVO.getMemberId());
        Assertions.assertNotNull(pointsVO.getAvailablePoints());
        Assertions.assertNotNull(pointsVO.getFrozenPoints());
        Assertions.assertNotNull(pointsVO.getTotalPoints());
    }

    /**
     * 测试获取会员积分变更历史
     */
    @Test
    public void testGetMemberPointsHistory() {
        // 获取会员积分变更历史
        Integer memberId = 1001;
        Integer pageNum = 1;
        Integer pageSize = 10;

        PageResult<MemberPointsRecordVO> pageResult = memberPointsService.getMemberPointsHistory(
                memberId, pageNum, pageSize);

        Assertions.assertNotNull(pageResult);
        // 不断言列表非空，因为可能没有历史记录
    }
}
