package org.example.musk.tests.member.level.service;

import jakarta.annotation.Resource;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.member.level.enums.GrowthValueSourceTypeEnum;
import org.example.musk.functions.member.level.model.vo.MemberGrowthValueRecordVO;
import org.example.musk.functions.member.level.model.vo.MemberGrowthValueVO;
import org.example.musk.functions.member.level.service.MemberGrowthValueService;
import org.example.musk.tests.BaseTest;
import org.example.musk.tests.TestApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 会员成长值服务测试类
 *
 * @author musk-functions-member-level
 */

public class MemberGrowthValueServiceTest extends BaseTest {

    @Resource
    private MemberGrowthValueService memberGrowthValueService;

    /**
     * 测试增加会员成长值
     */
    @Test
    public void testAddGrowthValue() {
        // 增加会员成长值
        Integer memberId = 1001;
        Integer growthValue = 100;
        GrowthValueSourceTypeEnum sourceType = GrowthValueSourceTypeEnum.CONSUMPTION;
        String sourceId = "ORDER123456";
        String description = "购物奖励成长值";
        String operator = "system";

        Integer totalGrowthValue = memberGrowthValueService.addGrowthValue(
                memberId, growthValue, sourceType, sourceId, description, operator);

        Assertions.assertNotNull(totalGrowthValue);
        Assertions.assertTrue(totalGrowthValue >= growthValue);
    }

    /**
     * 测试减少会员成长值
     */
    @Test
    public void testDeductGrowthValue() {
        // 先增加成长值，确保有足够的成长值可以减少
        Integer memberId = 1001;
        Integer growthValue = 50;
        GrowthValueSourceTypeEnum sourceType = GrowthValueSourceTypeEnum.ADMIN_ADJUST; // 使用管理员调整类型
        String sourceId = "ADJUST123456";
        String description = "系统调整成长值";
        String operator = "system";

        // 先增加成长值
        memberGrowthValueService.addGrowthValue(
                memberId, 100, sourceType, sourceId, "增加成长值用于测试", operator);

        // 减少成长值
        Integer totalGrowthValue = memberGrowthValueService.deductGrowthValue(
                memberId, growthValue, sourceType, sourceId, description, operator);

        Assertions.assertNotNull(totalGrowthValue);
    }

    /**
     * 测试获取会员成长值信息
     */
    @Test
    public void testGetMemberGrowthValue() {
        // 获取会员成长值信息
        Integer memberId = 1001;

        MemberGrowthValueVO growthValueVO = memberGrowthValueService.getMemberGrowthValue(memberId);

        Assertions.assertNotNull(growthValueVO);
        Assertions.assertEquals(memberId, growthValueVO.getMemberId());
        Assertions.assertNotNull(growthValueVO.getTotalGrowthValue());
        Assertions.assertNotNull(growthValueVO.getCurrentLevelId());
        Assertions.assertNotNull(growthValueVO.getCurrentLevelName());
    }

    /**
     * 测试获取会员成长值变更历史
     */
    @Test
    public void testGetMemberGrowthValueHistory() {
        // 获取会员成长值变更历史
        Integer memberId = 1001;
        Integer pageNum = 1;
        Integer pageSize = 10;

        PageResult<MemberGrowthValueRecordVO> pageResult = memberGrowthValueService.getMemberGrowthValueHistory(
                memberId, pageNum, pageSize);

        Assertions.assertNotNull(pageResult);
        // 不断言列表非空，因为可能没有历史记录
    }
}
