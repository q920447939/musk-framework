package org.example.musk.tests.icon.service;

import jakarta.annotation.Resource;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.functions.icon.constant.IconConstant;
import org.example.musk.functions.icon.entity.SystemIconDO;
import org.example.musk.functions.icon.entity.SystemIconResourceDO;
import org.example.musk.functions.icon.service.SystemIconResourceService;
import org.example.musk.functions.icon.service.SystemIconService;
import org.example.musk.tests.BaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 图标服务测试类
 *
 * @author musk-functions-icon
 */
public class SystemIconServiceTest extends BaseTest {

    @Resource
    private SystemIconService systemIconService;

    @Resource
    private SystemIconResourceService systemIconResourceService;

    private Integer testIconId;
    private String testIconCode;

    @BeforeEach
    public void setUp() {
        // 设置测试数据
        testIconCode = "TEST_ICON_" + System.currentTimeMillis();
    }

    @AfterEach
    public void tearDown() {
        // 清理测试数据
        if (testIconId != null) {
            try {
                // 先删除关联的资源
                List<SystemIconResourceDO> resources = systemIconResourceService.getResourcesByIconId(testIconId);
                for (SystemIconResourceDO resource : resources) {
                    systemIconResourceService.deleteIconResource(resource.getId());
                }
                // 再删除图标
                systemIconService.deleteIcon(testIconId);
            } catch (Exception e) {
                // 忽略删除失败的异常，可能是因为测试中已经删除了
            }
        }
    }

    /**
     * 测试创建图标
     */
    @Test
    public void testCreateIcon() {
        // 创建图标
        SystemIconDO icon = createTestIcon();

        // 验证图标ID不为空
        Assertions.assertNotNull(testIconId);

        // 验证图标信息
        SystemIconDO savedIcon = systemIconService.getIcon(testIconId);
        Assertions.assertNotNull(savedIcon);
        Assertions.assertEquals(testIconCode, savedIcon.getIconCode());
        Assertions.assertEquals("测试图标", savedIcon.getIconName());
        Assertions.assertEquals(1, savedIcon.getCategoryId());
        Assertions.assertEquals(IconConstant.STATUS_NORMAL, savedIcon.getStatus());
        Assertions.assertEquals(ThreadLocalTenantContext.getTenantId(), savedIcon.getTenantId());
        Assertions.assertEquals(ThreadLocalTenantContext.getDomainId(), savedIcon.getDomainId());
    }

    /**
     * 测试更新图标
     */
    @Test
    public void testUpdateIcon() {
        // 先创建图标
        SystemIconDO icon = createTestIcon();

        // 更新图标
        icon.setIconName("更新后的图标名称");
        icon.setDescription("更新后的描述");
        systemIconService.updateIcon(icon);

        // 验证更新结果
        SystemIconDO updatedIcon = systemIconService.getIcon(testIconId);
        Assertions.assertNotNull(updatedIcon);
        Assertions.assertEquals("更新后的图标名称", updatedIcon.getIconName());
        Assertions.assertEquals("更新后的描述", updatedIcon.getDescription());
    }

    /**
     * 测试删除图标
     */
    @Test
    public void testDeleteIcon() {
        // 先创建图标
        SystemIconDO icon = createTestIcon();

        // 删除图标
        systemIconService.deleteIcon(testIconId);

        // 验证图标已被删除
        SystemIconDO deletedIcon = systemIconService.getIcon(testIconId);
        Assertions.assertNull(deletedIcon);

        // 防止tearDown再次尝试删除
        testIconId = null;
    }

    /**
     * 测试根据编码获取图标
     */
    @Test
    public void testGetIconByCode() {
        // 先创建图标
        SystemIconDO icon = createTestIcon();

        // 根据编码获取图标
        SystemIconDO iconByCode = systemIconService.getIconByCode(testIconCode);

        // 验证图标信息
        Assertions.assertNotNull(iconByCode);
        Assertions.assertEquals(testIconId, iconByCode.getId());
        Assertions.assertEquals(testIconCode, iconByCode.getIconCode());
    }

    /**
     * 测试获取分类下的图标列表
     */
    @Test
    public void testGetIconsByCategory() {
        // 先创建图标
        SystemIconDO icon = createTestIcon();

        // 获取分类下的图标列表
        List<SystemIconDO> icons = systemIconService.getIconsByCategory(1);

        // 验证列表不为空
        Assertions.assertNotNull(icons);
        // 至少包含我们创建的图标
        Assertions.assertTrue(icons.stream().anyMatch(i -> i.getId().equals(testIconId)));
    }

    /**
     * 测试搜索图标
     */
    @Test
    public void testSearchIcons() {
        // 先创建图标
        SystemIconDO icon = createTestIcon();

        // 搜索图标
        List<SystemIconDO> icons = systemIconService.searchIcons("测试图标");

        // 验证列表不为空
        Assertions.assertNotNull(icons);
        // 至少包含我们创建的图标
        Assertions.assertTrue(icons.stream().anyMatch(i -> i.getId().equals(testIconId)));
    }

    /**
     * 创建测试图标
     */
    private SystemIconDO createTestIcon() {
        SystemIconDO icon = new SystemIconDO();
        icon.setIconName("测试图标");
        icon.setIconCode(testIconCode);
        icon.setCategoryId(1);
        icon.setDescription("这是一个测试图标");
        icon.setStatus(IconConstant.STATUS_NORMAL);

        testIconId = systemIconService.createIcon(icon);
        icon.setId(testIconId);

        return icon;
    }
}
