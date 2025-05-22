package org.example.musk.tests.member.level.service;

import jakarta.annotation.Resource;
import org.example.musk.functions.member.level.enums.PointsRuleTypeEnum;
import org.example.musk.functions.member.level.model.entity.MemberPointsRuleDO;
import org.example.musk.functions.member.level.model.vo.MemberPointsRuleCreateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberPointsRuleUpdateReqVO;
import org.example.musk.functions.member.level.service.MemberPointsRuleService;
import org.example.musk.tests.BaseTest;
import org.example.musk.tests.TestApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 积分规则服务测试类
 *
 * @author musk-functions-member-level
 */
public class MemberPointsRuleServiceTest extends BaseTest {

    @Resource
    private MemberPointsRuleService memberPointsRuleService;

    /**
     * 测试创建积分规则
     */
    @Test
    public void testCreatePointsRule() {
        // 创建积分规则
        MemberPointsRuleCreateReqVO createReqVO = new MemberPointsRuleCreateReqVO();
        createReqVO.setRuleCode("TEST_RULE_" + System.currentTimeMillis());
        createReqVO.setRuleName("测试规则");
        createReqVO.setRuleType(PointsRuleTypeEnum.CONSUMPTION.getValue());
        createReqVO.setPointsValue(10);
        createReqVO.setGrowthValue(5);
        createReqVO.setRuleFormula("amount / 100");
        createReqVO.setRuleDescription("这是一个测试规则");
        createReqVO.setEffectiveStartTime(LocalDateTime.now());
        createReqVO.setEffectiveEndTime(LocalDateTime.now().plusMonths(1));
        createReqVO.setDailyLimit(10);
        createReqVO.setStatus(0); // 启用

        Integer ruleId = memberPointsRuleService.createPointsRule(createReqVO);
        Assertions.assertNotNull(ruleId);

        // 清理测试数据
        memberPointsRuleService.deletePointsRule(ruleId);
    }

    /**
     * 测试更新积分规则
     */
    @Test
    public void testUpdatePointsRule() {
        // 先创建一个规则
        MemberPointsRuleCreateReqVO createReqVO = new MemberPointsRuleCreateReqVO();
        createReqVO.setRuleCode("TEST_UPDATE_RULE_" + System.currentTimeMillis());
        createReqVO.setRuleName("待更新规则");
        createReqVO.setRuleType(PointsRuleTypeEnum.CONSUMPTION.getValue());
        createReqVO.setPointsValue(10);
        createReqVO.setGrowthValue(5);
        createReqVO.setRuleDescription("这是一个待更新的测试规则");
        createReqVO.setStatus(0); // 启用

        Integer ruleId = memberPointsRuleService.createPointsRule(createReqVO);

        // 更新规则
        MemberPointsRuleUpdateReqVO updateReqVO = new MemberPointsRuleUpdateReqVO();
        updateReqVO.setId(ruleId);
        updateReqVO.setRuleCode(createReqVO.getRuleCode()); // 保持规则编码不变
        updateReqVO.setRuleName("已更新规则");
        updateReqVO.setRuleType(PointsRuleTypeEnum.CONSUMPTION.getValue());
        updateReqVO.setPointsValue(20);
        updateReqVO.setGrowthValue(10);
        updateReqVO.setRuleDescription("这是一个已更新的测试规则");
        updateReqVO.setStatus(0); // 启用

        memberPointsRuleService.updatePointsRule(updateReqVO);

        // 获取更新后的规则
        MemberPointsRuleDO rule = memberPointsRuleService.getPointsRule(ruleId);
        Assertions.assertNotNull(rule);
        Assertions.assertEquals("已更新规则", rule.getRuleName());
        Assertions.assertEquals(20, rule.getPointsValue());
        Assertions.assertEquals(10, rule.getGrowthValue());
        Assertions.assertEquals("这是一个已更新的测试规则", rule.getRuleDescription());

        // 清理测试数据
        memberPointsRuleService.deletePointsRule(ruleId);
    }

    /**
     * 测试获取积分规则列表
     */
    @Test
    public void testGetPointsRuleList() {
        // 获取积分规则列表
        List<MemberPointsRuleDO> rules = memberPointsRuleService.getPointsRuleList();
        Assertions.assertNotNull(rules);
        // 不断言列表非空，因为可能没有规则
    }

    /**
     * 测试计算消费积分和成长值
     */
    @Test
    public void testCalculateConsumptionPointsAndGrowth() {
        // 计算消费积分和成长值
        Integer memberId = 1001;
        Integer amount = 10000; // 100元
        String sourceId = "ORDER123456";
        String operator = "system";

        // 执行计算
        memberPointsRuleService.calculateConsumptionPointsAndGrowth(memberId, amount, sourceId, operator);
        // 无需断言，只要不抛异常即可
    }

    /**
     * 测试计算签到积分和成长值
     */
    @Test
    public void testCalculateSignInPointsAndGrowth() {
        // 计算签到积分和成长值
        Integer memberId = 1001;
        Integer continuousDays = 3;
        String operator = "system";

        // 执行计算
        memberPointsRuleService.calculateSignInPointsAndGrowth(memberId, continuousDays, operator);
        // 无需断言，只要不抛异常即可
    }
}
