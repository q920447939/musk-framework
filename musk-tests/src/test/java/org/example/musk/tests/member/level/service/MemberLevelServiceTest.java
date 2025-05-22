package org.example.musk.tests.member.level.service;

import jakarta.annotation.Resource;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.member.level.model.entity.MemberLevelDefinitionDO;
import org.example.musk.functions.member.level.model.vo.MemberLevelChangeRecordVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelDefinitionCreateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelDefinitionUpdateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelInfoVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelProgressVO;
import org.example.musk.functions.member.level.service.MemberLevelService;
import org.example.musk.tests.BaseTest;
import org.example.musk.tests.TestApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 会员等级服务测试类
 *
 * @author musk-functions-member-level
 */

public class MemberLevelServiceTest extends BaseTest {

    @Resource
    private MemberLevelService memberLevelService;

    /**
     * 测试创建会员等级定义
     */
    @Test
    public void testCreateLevelDefinition() {
        // 创建会员等级定义
        MemberLevelDefinitionCreateReqVO createReqVO = new MemberLevelDefinitionCreateReqVO();
        createReqVO.setLevelCode("TEST_LEVEL");
        createReqVO.setLevelName("测试等级");
        createReqVO.setLevelValue(999);
        createReqVO.setGrowthValueThreshold(10000);
        createReqVO.setLevelIconId(1); // 使用图标ID而不是URL
        createReqVO.setLevelDescription("这是一个测试等级");
        createReqVO.setLevelColor("#FF5500"); // 设置等级颜色
        createReqVO.setDisplayIndex(1);
        createReqVO.setStatus(0); // 启用

        Integer levelId = memberLevelService.createLevelDefinition(createReqVO);
        Assertions.assertNotNull(levelId);

        // 清理测试数据
        memberLevelService.deleteLevelDefinition(levelId);
    }

    /**
     * 测试更新会员等级定义
     */
    @Test
    public void testUpdateLevelDefinition() {
        // 先创建一个等级
        MemberLevelDefinitionCreateReqVO createReqVO = new MemberLevelDefinitionCreateReqVO();
        createReqVO.setLevelCode("UPDATE_LEVEL");
        createReqVO.setLevelName("待更新等级");
        createReqVO.setLevelValue(888);
        createReqVO.setGrowthValueThreshold(8000);
        createReqVO.setLevelIconId(1); // 使用图标ID而不是URL
        createReqVO.setLevelDescription("这是一个待更新的测试等级");
        createReqVO.setLevelColor("#FF5500"); // 设置等级颜色
        createReqVO.setDisplayIndex(1);
        createReqVO.setStatus(0); // 启用

        Integer levelId = memberLevelService.createLevelDefinition(createReqVO);

        // 更新等级
        MemberLevelDefinitionUpdateReqVO updateReqVO = new MemberLevelDefinitionUpdateReqVO();
        updateReqVO.setId(levelId);
        updateReqVO.setLevelCode("UPDATE_LEVEL");
        updateReqVO.setLevelName("已更新等级");
        updateReqVO.setLevelValue(888);
        updateReqVO.setGrowthValueThreshold(8000);
        updateReqVO.setLevelIconId(1);
        updateReqVO.setLevelDescription("这是一个已更新的测试等级");
        updateReqVO.setLevelColor("#FF5500");
        updateReqVO.setDisplayIndex(1);
        updateReqVO.setStatus(0);

        memberLevelService.updateLevelDefinition(updateReqVO);

        // 获取更新后的等级
        MemberLevelDefinitionDO levelDefinition = memberLevelService.getLevelDefinition(levelId);
        Assertions.assertNotNull(levelDefinition);
        Assertions.assertEquals("已更新等级", levelDefinition.getLevelName());
        Assertions.assertEquals("这是一个已更新的测试等级", levelDefinition.getLevelDescription());

        // 清理测试数据
        memberLevelService.deleteLevelDefinition(levelId);
    }

    /**
     * 测试获取会员等级定义列表
     */
    @Test
    public void testGetLevelDefinitionList() {
        // 获取会员等级定义列表
        List<MemberLevelDefinitionDO> levelDefinitions = memberLevelService.getLevelDefinitionList();
        Assertions.assertNotNull(levelDefinitions);
        Assertions.assertFalse(levelDefinitions.isEmpty());
    }

    /**
     * 测试获取会员当前等级
     */
    @Test
    public void testGetMemberCurrentLevel() {
        // 获取会员当前等级
        Integer memberId = 1001;

        MemberLevelInfoVO levelInfo = memberLevelService.getMemberCurrentLevel(memberId);
        Assertions.assertNotNull(levelInfo);
        Assertions.assertEquals(memberId, levelInfo.getMemberId());
        Assertions.assertNotNull(levelInfo.getCurrentLevelId());
        Assertions.assertNotNull(levelInfo.getCurrentLevelName());
    }

    /**
     * 测试手动设置会员等级
     */
    @Test
    public void testSetMemberLevel() {
        // 获取等级列表
        List<MemberLevelDefinitionDO> levelDefinitions = memberLevelService.getLevelDefinitionList();
        if (levelDefinitions.isEmpty()) {
            return; // 跳过测试
        }

        // 选择一个等级
        MemberLevelDefinitionDO targetLevel = levelDefinitions.get(0);
        Integer memberId = 1001;
        String reason = "测试手动设置等级";
        String operator = "system";

        // 设置会员等级
        memberLevelService.setMemberLevel(memberId, targetLevel.getId(), reason, operator);

        // 获取会员当前等级
        MemberLevelInfoVO levelInfo = memberLevelService.getMemberCurrentLevel(memberId);
        Assertions.assertNotNull(levelInfo);
        Assertions.assertEquals(targetLevel.getId(), levelInfo.getCurrentLevelId());
    }

    /**
     * 测试计算会员等级
     */
    @Test
    public void testCalculateMemberLevel() {
        // 计算会员等级
        Integer memberId = 1001;

        Integer levelId = memberLevelService.calculateMemberLevel(memberId);
        Assertions.assertNotNull(levelId);
    }

    /**
     * 测试获取会员等级变更历史
     */
    @Test
    public void testGetMemberLevelChangeHistory() {
        // 获取会员等级变更历史
        Integer memberId = 1001;
        Integer pageNum = 1;
        Integer pageSize = 10;

        PageResult<MemberLevelChangeRecordVO> pageResult = memberLevelService.getMemberLevelChangeHistory(
                memberId, pageNum, pageSize);

        Assertions.assertNotNull(pageResult);
        // 不断言列表非空，因为可能没有历史记录
    }

    /**
     * 测试计算会员等级进度
     */
    @Test
    public void testCalculateMemberLevelProgress() {
        // 计算会员等级进度
        Integer memberId = 1001;

        MemberLevelProgressVO progressVO = memberLevelService.calculateMemberLevelProgress(memberId);

        Assertions.assertNotNull(progressVO);
        Assertions.assertNotNull(progressVO.getCurrentLevelId());
        Assertions.assertNotNull(progressVO.getCurrentLevelName());
        Assertions.assertNotNull(progressVO.getUpgradeProgressPercent());
    }
}
