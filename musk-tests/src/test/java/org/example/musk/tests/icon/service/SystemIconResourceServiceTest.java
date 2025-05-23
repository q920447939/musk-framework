package org.example.musk.tests.icon.service;

import jakarta.annotation.Resource;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.functions.icon.constant.IconConstant;
import org.example.musk.functions.icon.entity.SystemIconDO;
import org.example.musk.functions.icon.entity.SystemIconResourceDO;
import org.example.musk.functions.icon.enums.IconResourceTypeEnum;
import org.example.musk.functions.icon.exception.IconException;
import org.example.musk.functions.icon.service.SystemIconResourceService;
import org.example.musk.functions.icon.service.SystemIconService;
import org.example.musk.tests.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

/**
 * 图标资源服务测试类
 *
 * @author musk-functions-icon
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemIconResourceServiceTest extends BaseTest {

    @Resource
    private SystemIconResourceService systemIconResourceService;

    @Resource
    private SystemIconService systemIconService;

    private static Integer testIconId;
    private static Integer testResourceId;
    private static Integer testDefaultResourceId;

    /**
     * 测试创建图标资源
     */
    @Test
    @Order(1)
    public void testCreateIconResource() {
        // 先创建一个图标
        SystemIconDO icon = new SystemIconDO();
        icon.setIconName("测试图标");
        icon.setIconCode("test_icon_" + System.currentTimeMillis());
        icon.setCategoryId(1); // 假设分类ID为1
        icon.setStatus(IconConstant.STATUS_NORMAL);
        icon.setDescription("测试图标描述");
        
        testIconId = systemIconService.createIcon(icon);
        Assertions.assertNotNull(testIconId);
        
        // 创建图标资源
        SystemIconResourceDO resource = new SystemIconResourceDO();
        resource.setIconId(testIconId);
        resource.setResourceType(IconResourceTypeEnum.URL.getType());
        resource.setResourceUrl("https://example.com/test-icon.png");
        resource.setWidth(100);
        resource.setHeight(100);
        resource.setVersion("1.0");
        resource.setIsDefault(false);
        
        testResourceId = systemIconResourceService.createIconResource(resource);
        
        Assertions.assertNotNull(testResourceId);
        Assertions.assertTrue(testResourceId > 0);
        
        // 创建默认资源
        SystemIconResourceDO defaultResource = new SystemIconResourceDO();
        defaultResource.setIconId(testIconId);
        defaultResource.setResourceType(IconResourceTypeEnum.URL.getType());
        defaultResource.setResourceUrl("https://example.com/test-icon-default.png");
        defaultResource.setWidth(200);
        defaultResource.setHeight(200);
        defaultResource.setVersion("1.0");
        defaultResource.setIsDefault(true);
        
        testDefaultResourceId = systemIconResourceService.createIconResource(defaultResource);
        
        Assertions.assertNotNull(testDefaultResourceId);
        Assertions.assertTrue(testDefaultResourceId > 0);
    }

    /**
     * 测试获取图标资源
     */
    @Test
    @Order(2)
    public void testGetIconResource() {
        // 获取创建的资源
        SystemIconResourceDO resource = systemIconResourceService.getIconResource(testResourceId);
        
        Assertions.assertNotNull(resource);
        Assertions.assertEquals(testResourceId, resource.getId());
        Assertions.assertEquals(testIconId, resource.getIconId());
        Assertions.assertEquals(ThreadLocalTenantContext.getTenantId(), resource.getTenantId());
        Assertions.assertEquals(ThreadLocalTenantContext.getDomainId(), resource.getDomainId());
        Assertions.assertEquals(IconResourceTypeEnum.URL.getType(), resource.getResourceType());
        Assertions.assertEquals("https://example.com/test-icon.png", resource.getResourceUrl());
        Assertions.assertEquals(100, resource.getWidth());
        Assertions.assertEquals(100, resource.getHeight());
        Assertions.assertEquals("1.0", resource.getVersion());
        Assertions.assertEquals(false, resource.getIsDefault());
    }

    /**
     * 测试获取图标的所有资源
     */
    @Test
    @Order(3)
    public void testGetResourcesByIconId() {
        // 获取图标的所有资源
        List<SystemIconResourceDO> resources = systemIconResourceService.getResourcesByIconId(testIconId);
        
        Assertions.assertNotNull(resources);
        Assertions.assertEquals(2, resources.size());
        
        // 验证资源列表中包含我们创建的资源
        boolean foundTestResource = resources.stream()
                .anyMatch(r -> r.getId().equals(testResourceId));
        boolean foundDefaultResource = resources.stream()
                .anyMatch(r -> r.getId().equals(testDefaultResourceId));
                
        Assertions.assertTrue(foundTestResource);
        Assertions.assertTrue(foundDefaultResource);
    }

    /**
     * 测试获取默认资源
     */
    @Test
    @Order(4)
    public void testGetDefaultResource() {
        // 获取默认资源
        SystemIconResourceDO defaultResource = systemIconResourceService.getDefaultResource(testIconId);
        
        Assertions.assertNotNull(defaultResource);
        Assertions.assertEquals(testDefaultResourceId, defaultResource.getId());
        Assertions.assertEquals(testIconId, defaultResource.getIconId());
        Assertions.assertEquals(true, defaultResource.getIsDefault());
    }

    /**
     * 测试更新图标资源
     */
    @Test
    @Order(5)
    public void testUpdateIconResource() {
        // 获取资源
        SystemIconResourceDO resource = systemIconResourceService.getIconResource(testResourceId);
        
        // 更新资源
        resource.setResourceUrl("https://example.com/updated-icon.png");
        resource.setWidth(150);
        resource.setHeight(150);
        resource.setVersion("1.1");
        
        systemIconResourceService.updateIconResource(resource);
        
        // 验证更新结果
        SystemIconResourceDO updatedResource = systemIconResourceService.getIconResource(testResourceId);
        
        Assertions.assertNotNull(updatedResource);
        Assertions.assertEquals("https://example.com/updated-icon.png", updatedResource.getResourceUrl());
        Assertions.assertEquals(150, updatedResource.getWidth());
        Assertions.assertEquals(150, updatedResource.getHeight());
        Assertions.assertEquals("1.1", updatedResource.getVersion());
    }

    /**
     * 测试将普通资源设置为默认资源
     */
    @Test
    @Order(6)
    public void testSetResourceAsDefault() {
        // 获取非默认资源
        SystemIconResourceDO resource = systemIconResourceService.getIconResource(testResourceId);
        
        // 设置为默认资源
        resource.setIsDefault(true);
        systemIconResourceService.updateIconResource(resource);
        
        // 验证该资源已设为默认
        SystemIconResourceDO updatedResource = systemIconResourceService.getIconResource(testResourceId);
        Assertions.assertTrue(updatedResource.getIsDefault());
        
        // 验证原默认资源已不再是默认
        SystemIconResourceDO previousDefault = systemIconResourceService.getIconResource(testDefaultResourceId);
        Assertions.assertFalse(previousDefault.getIsDefault());
        
        // 验证获取默认资源返回的是新的默认资源
        SystemIconResourceDO defaultResource = systemIconResourceService.getDefaultResource(testIconId);
        Assertions.assertEquals(testResourceId, defaultResource.getId());
    }

    /**
     * 测试删除非默认图标资源
     */
    @Test
    @Order(7)
    public void testDeleteNonDefaultResource() {
        // 将testResourceId设为默认资源，testDefaultResourceId为非默认资源
        SystemIconResourceDO resource = systemIconResourceService.getIconResource(testDefaultResourceId);
        
        // 删除非默认资源
        systemIconResourceService.deleteIconResource(testDefaultResourceId);
        
        // 验证资源已删除
        List<SystemIconResourceDO> resources = systemIconResourceService.getResourcesByIconId(testIconId);
        Assertions.assertEquals(1, resources.size());
        Assertions.assertEquals(testResourceId, resources.get(0).getId());
    }

    /**
     * 测试删除默认图标资源（应该抛出异常）
     */
    @Test
    @Order(8)
    public void testDeleteDefaultResource() {
        // 尝试删除默认资源，应该抛出异常
        Exception exception = Assertions.assertThrows(IconException.class, () -> {
            systemIconResourceService.deleteIconResource(testResourceId);
        });
        
        // 验证异常信息
        Assertions.assertTrue(exception.getMessage().contains("默认图标资源不能删除"));
    }
}
