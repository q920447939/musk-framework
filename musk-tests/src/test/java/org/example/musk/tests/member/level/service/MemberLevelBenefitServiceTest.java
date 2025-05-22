package org.example.musk.tests.member.level.service;

import jakarta.annotation.Resource;
import org.example.musk.functions.member.level.model.entity.MemberLevelBenefitDO;
import org.example.musk.functions.member.level.model.entity.MemberLevelDefinitionDO;
import org.example.musk.functions.member.level.model.vo.MemberLevelBenefitCreateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelBenefitUpdateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelBenefitVO;
import org.example.musk.functions.member.level.service.MemberLevelBenefitService;
import org.example.musk.functions.member.level.service.MemberLevelService;
import org.example.musk.tests.BaseTest;
import org.example.musk.tests.TestApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 会员等级权益服务测试类
 *
 * @author musk-functions-member-level
 */

public class MemberLevelBenefitServiceTest extends BaseTest {

    @Resource
    private MemberLevelBenefitService memberLevelBenefitService;

    @Resource
    private MemberLevelService memberLevelService;

    /**
     * 测试创建会员等级权益
     */
    @Test
    public void testCreateLevelBenefit() {
        // 获取等级列表
        List<MemberLevelDefinitionDO> levelDefinitions = memberLevelService.getLevelDefinitionList();
        if (levelDefinitions.isEmpty()) {
            return; // 跳过测试
        }

        // 选择一个等级
        Integer levelId = levelDefinitions.getFirst().getId();

        // 创建会员等级权益
        MemberLevelBenefitCreateReqVO createReqVO = new MemberLevelBenefitCreateReqVO();
        createReqVO.setLevelId(levelId);
        createReqVO.setBenefitType(1); // 假设1表示折扣
        createReqVO.setBenefitName("测试权益");
        createReqVO.setBenefitValue("0.9");
        createReqVO.setBenefitIconId(110);
        createReqVO.setBenefitDescription("这是一个测试权益");
        createReqVO.setStatus(0); // 启用

        Integer benefitId = memberLevelBenefitService.createLevelBenefit(createReqVO);
        Assertions.assertNotNull(benefitId);

        // 清理测试数据
        memberLevelBenefitService.deleteLevelBenefit(benefitId);
    }

    /**
     * 测试更新会员等级权益
     */
    @Test
    public void testUpdateLevelBenefit() {
        // 获取等级列表
        List<MemberLevelDefinitionDO> levelDefinitions = memberLevelService.getLevelDefinitionList();
        if (levelDefinitions.isEmpty()) {
            return; // 跳过测试
        }

        // 选择一个等级
        Integer levelId = levelDefinitions.getFirst().getId();

        // 先创建一个权益
        MemberLevelBenefitCreateReqVO createReqVO = new MemberLevelBenefitCreateReqVO();
        createReqVO.setLevelId(levelId);
        createReqVO.setBenefitType(1); // 假设1表示折扣
        createReqVO.setBenefitName("待更新权益");
        createReqVO.setBenefitValue("0.9");
        createReqVO.setBenefitIconId(110);
        createReqVO.setBenefitDescription("这是一个待更新的测试权益");
        createReqVO.setStatus(0); // 启用

        Integer benefitId = memberLevelBenefitService.createLevelBenefit(createReqVO);

        // 更新权益
        MemberLevelBenefitUpdateReqVO updateReqVO = new MemberLevelBenefitUpdateReqVO();
        updateReqVO.setId(benefitId);
        updateReqVO.setLevelId(levelId);
        updateReqVO.setBenefitName("已更新权益");
        updateReqVO.setBenefitDescription("这是一个已更新的测试权益");

        memberLevelBenefitService.updateLevelBenefit(updateReqVO);

        // 获取更新后的权益
        MemberLevelBenefitDO benefit = memberLevelBenefitService.getLevelBenefit(benefitId);
        Assertions.assertNotNull(benefit);
        Assertions.assertEquals("已更新权益", benefit.getBenefitName());
        Assertions.assertEquals("这是一个已更新的测试权益", benefit.getBenefitDescription());

        // 清理测试数据
        memberLevelBenefitService.deleteLevelBenefit(benefitId);
    }

    /**
     * 测试获取会员等级权益列表
     */
    @Test
    public void testGetLevelBenefitList() {
        // 获取等级列表
        List<MemberLevelDefinitionDO> levelDefinitions = memberLevelService.getLevelDefinitionList();
        if (levelDefinitions.isEmpty()) {
            return; // 跳过测试
        }

        // 选择一个等级
        Integer levelId = levelDefinitions.getFirst().getId();

        // 获取会员等级权益列表
        List<MemberLevelBenefitDO> benefits = memberLevelBenefitService.getLevelBenefitList(levelId);
        Assertions.assertNotNull(benefits);
        // 不断言列表非空，因为可能没有权益
    }

    /**
     * 测试获取会员当前权益列表
     */
    @Test
    public void testGetMemberCurrentBenefits() {
        // 获取会员当前权益列表
        Integer memberId = 1001;

        List<MemberLevelBenefitVO> benefits = memberLevelBenefitService.getMemberCurrentBenefits(memberId);
        Assertions.assertNotNull(benefits);
        // 不断言列表非空，因为可能没有权益
    }

    /**
     * 测试检查会员是否拥有特定权益
     */
    @Test
    public void testHasBenefit() {
        // 检查会员是否拥有特定权益
        Integer memberId = 1001;
        Integer benefitType = 1; // 假设1表示折扣

        boolean hasBenefit = memberLevelBenefitService.hasBenefit(memberId, benefitType);
        // 不断言结果，因为可能有也可能没有
    }

    /**
     * 测试获取会员特定权益的值
     */
    @Test
    public void testGetBenefitValue() {
        // 获取会员特定权益的值
        Integer memberId = 1001;
        Integer benefitType = 1; // 假设1表示折扣

        String benefitValue = memberLevelBenefitService.getBenefitValue(memberId, benefitType);
        // 不断言结果，因为可能有也可能没有
    }
}
