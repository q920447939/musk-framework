package org.example.musk.tests;

import jakarta.annotation.Resource;
import org.example.musk.functions.member.level.enums.GrowthValueSourceTypeEnum;
import org.example.musk.functions.member.level.enums.PointsRuleTypeEnum;
import org.example.musk.functions.member.level.enums.PointsSourceTypeEnum;
import org.example.musk.functions.member.level.model.entity.MemberLevelDefinitionDO;
import org.example.musk.functions.member.level.model.vo.MemberGrowthValueVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelInfoVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelProgressVO;
import org.example.musk.functions.member.level.model.vo.MemberPointsRuleCreateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberPointsVO;
import org.example.musk.functions.member.level.service.MemberGrowthValueService;
import org.example.musk.functions.member.level.service.MemberLevelService;
import org.example.musk.functions.member.level.service.MemberPointsRuleService;
import org.example.musk.functions.member.level.service.MemberPointsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员等级模块集成测试类
 *
 * @author musk-functions-member-level
 */
@SpringBootTest(
    classes = TestApplication.class,
    properties = {
        "spring.profiles.active=test"
    }
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberLevelModuleIntegrationTest extends BaseTest {

    @Resource
    private MemberGrowthValueService memberGrowthValueService;

    @Resource
    private MemberLevelService memberLevelService;

    @Resource
    private MemberPointsService memberPointsService;

    @Resource
    private MemberPointsRuleService memberPointsRuleService;

    private static final Integer TEST_MEMBER_ID = 1001;
    private static final Integer TEST_TENANT_ID = 1;
    private static final Integer TEST_DOMAIN_ID = 1;
    private static final String TEST_OPERATOR = "system";

    private static Integer testRuleId;

    /**
     * 测试创建积分规则
     */
    @Test
    @Order(1)
    public void testCreatePointsRule() {
        // 创建消费积分规则
        MemberPointsRuleCreateReqVO createReqVO = new MemberPointsRuleCreateReqVO();
        createReqVO.setRuleCode("TEST_CONSUMPTION_" + System.currentTimeMillis());
        createReqVO.setRuleName("测试消费积分规则");
        createReqVO.setRuleType(PointsRuleTypeEnum.CONSUMPTION.getValue());
        createReqVO.setPointsValue(10);
        createReqVO.setGrowthValue(5);
        createReqVO.setRuleFormula("amount / 100");
        createReqVO.setRuleDescription("消费100元奖励10积分和5成长值");
        createReqVO.setEffectiveStartTime(LocalDateTime.now());
        createReqVO.setEffectiveEndTime(LocalDateTime.now().plusMonths(1));
        createReqVO.setStatus(0); // 启用

        testRuleId = memberPointsRuleService.createPointsRule(createReqVO);
        Assertions.assertNotNull(testRuleId);
    }

    /**
     * 测试会员消费获取积分和成长值
     */
    @Test
    @Order(2)
    public void testMemberConsumption() {
        // 模拟会员消费
        Integer amount = 10000; // 100元
        String orderId = "ORDER_" + System.currentTimeMillis();

        // 计算并发放积分和成长值
        memberPointsRuleService.calculateConsumptionPointsAndGrowth(TEST_MEMBER_ID, amount, orderId, TEST_OPERATOR);

        // 获取会员积分信息
        MemberPointsVO pointsVO = memberPointsService.getMemberPoints(TEST_MEMBER_ID);
        Assertions.assertNotNull(pointsVO);
        Assertions.assertTrue(pointsVO.getAvailablePoints() > 0);

        // 获取会员成长值信息
        MemberGrowthValueVO growthValueVO = memberGrowthValueService.getMemberGrowthValue(TEST_MEMBER_ID);
        Assertions.assertNotNull(growthValueVO);
        Assertions.assertTrue(growthValueVO.getTotalGrowthValue() > 0);
    }

    /**
     * 测试会员等级计算
     */
    @Test
    @Order(3)
    public void testMemberLevelCalculation() {
        // 获取会员当前等级
        MemberLevelInfoVO beforeLevelInfo = memberLevelService.getMemberCurrentLevel(TEST_MEMBER_ID);
        Assertions.assertNotNull(beforeLevelInfo);

        // 增加足够的成长值以提升等级
        List<MemberLevelDefinitionDO> levelDefinitions = memberLevelService.getLevelDefinitionList();
        if (levelDefinitions.size() > 1) {
            // 找到比当前等级高一级的等级
            MemberLevelDefinitionDO nextLevel = null;
            for (MemberLevelDefinitionDO level : levelDefinitions) {
                if (level.getLevelValue() > beforeLevelInfo.getCurrentLevelValue() &&
                    (nextLevel == null || level.getLevelValue() < nextLevel.getLevelValue())) {
                    nextLevel = level;
                }
            }

            if (nextLevel != null) {
                // 增加足够的成长值以达到下一等级
                Integer growthValueNeeded = nextLevel.getGrowthValueThreshold() - beforeLevelInfo.getCurrentLevelValue() + 10;
                if (growthValueNeeded > 0) {
                    memberGrowthValueService.addGrowthValue(
                            TEST_MEMBER_ID,
                            growthValueNeeded,
                            GrowthValueSourceTypeEnum.ADMIN_ADJUST, // 使用管理员调整类型
                            "LEVEL_TEST",
                            "测试等级提升",
                            TEST_OPERATOR);
                }

                // 计算会员等级
                Integer newLevelId = memberLevelService.calculateMemberLevel(TEST_MEMBER_ID);
                Assertions.assertNotNull(newLevelId);

                // 获取更新后的会员等级
                MemberLevelInfoVO afterLevelInfo = memberLevelService.getMemberCurrentLevel(TEST_MEMBER_ID);
                Assertions.assertNotNull(afterLevelInfo);

                // 检查等级是否提升
                if (nextLevel.getId().equals(afterLevelInfo.getCurrentLevelId())) {
                    Assertions.assertTrue(afterLevelInfo.getCurrentLevelValue() > beforeLevelInfo.getCurrentLevelValue());
                }
            }
        }
    }

    /**
     * 测试会员等级进度
     */
    @Test
    @Order(4)
    public void testMemberLevelProgress() {
        // 获取会员等级进度
        MemberLevelProgressVO progressVO = memberLevelService.calculateMemberLevelProgress(TEST_MEMBER_ID);
        Assertions.assertNotNull(progressVO);
        Assertions.assertNotNull(progressVO.getCurrentLevelId());
        Assertions.assertNotNull(progressVO.getCurrentLevelName());
        Assertions.assertNotNull(progressVO.getUpgradeProgressPercent());
    }

    /**
     * 清理测试数据
     */
    @Test
    @Order(5)
    public void cleanupTestData() {
        if (testRuleId != null) {
            memberPointsRuleService.deletePointsRule(testRuleId);
        }
    }
}
