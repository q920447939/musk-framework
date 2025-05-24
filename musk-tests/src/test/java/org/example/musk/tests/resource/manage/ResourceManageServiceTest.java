package org.example.musk.tests.resource.manage;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.functions.resource.entity.SystemResourceCategoryDO;
import org.example.musk.functions.resource.entity.SystemResourceDO;
import org.example.musk.functions.resource.exception.ResourceException;
import org.example.musk.functions.resource.service.ResourceCategoryService;
import org.example.musk.functions.resource.service.ResourceManageService;
import org.example.musk.functions.resource.service.ResourceQueryService;
import org.example.musk.functions.resource.service.ResourceUploadService;
import org.example.musk.tests.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 资源管理服务测试类
 *
 * @author musk-functions-resource
 */
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ResourceManageServiceTest extends BaseTest {

    @Resource
    private ResourceManageService resourceManageService;

    @Resource
    private ResourceQueryService resourceQueryService;

    @Resource
    private ResourceUploadService resourceUploadService;

    @Resource
    private ResourceCategoryService resourceCategoryService;

    private static Integer testCategoryId;
    private static Integer testResourceId;
    private static Integer testResourceId2;

    /**
     * 测试前准备 - 创建测试分类和资源
     */
    @Test
    @Order(1)
    public void testPrepareData() {
        // 创建测试分类
        SystemResourceCategoryDO category = new SystemResourceCategoryDO();
        category.setCategoryName("测试资源管理分类");
        category.setCategoryCode("test_resource_manage_category_" + System.currentTimeMillis());
        category.setDisplayOrder(1);
        category.setStatus(0);
        category.setRemark("测试资源管理分类备注");

        testCategoryId = resourceCategoryService.createCategory(category);
        Assertions.assertNotNull(testCategoryId);
        Assertions.assertTrue(testCategoryId > 0);

        // 创建测试资源1
        MockMultipartFile file1 = new MockMultipartFile(
                "file",
                "test1.txt",
                "text/plain",
                "测试文件内容1".getBytes(StandardCharsets.UTF_8)
        );

        SystemResourceDO resource1 = resourceUploadService.uploadFile(file1, testCategoryId, "测试标签1", "测试描述1");
        testResourceId = resource1.getId();
        Assertions.assertNotNull(testResourceId);
        Assertions.assertTrue(testResourceId > 0);

        // 创建测试资源2
        MockMultipartFile file2 = new MockMultipartFile(
                "file",
                "test2.txt",
                "text/plain",
                "测试文件内容2".getBytes(StandardCharsets.UTF_8)
        );

        SystemResourceDO resource2 = resourceUploadService.uploadFile(file2, testCategoryId, "测试标签2", "测试描述2");
        testResourceId2 = resource2.getId();
        Assertions.assertNotNull(testResourceId2);
        Assertions.assertTrue(testResourceId2 > 0);
    }

    /**
     * 测试更新资源信息
     */
    @Test
    @Order(2)
    public void testUpdateResource() {
        // 获取原资源信息
        SystemResourceDO originalResource = resourceQueryService.getResource(testResourceId);
        Assertions.assertNotNull(originalResource);

        // 更新资源信息
        SystemResourceDO updateResource = new SystemResourceDO();
        updateResource.setId(testResourceId);
        updateResource.setResourceName("更新后的资源名称");
        updateResource.setDescription("更新后的描述");
        updateResource.setTags("更新后的标签");

        resourceManageService.updateResource(updateResource);

        // 验证更新结果
        SystemResourceDO updatedResource = resourceQueryService.getResource(testResourceId);
        Assertions.assertNotNull(updatedResource);
        Assertions.assertEquals("更新后的资源名称", updatedResource.getResourceName());
        Assertions.assertEquals("更新后的描述", updatedResource.getDescription());
        Assertions.assertEquals("更新后的标签", updatedResource.getTags());
        Assertions.assertEquals(ThreadLocalTenantContext.getTenantId(), updatedResource.getTenantId());
        Assertions.assertEquals(ThreadLocalTenantContext.getDomainId(), updatedResource.getDomainId());
    }

    /**
     * 测试更新不存在的资源（应该失败）
     */
    @Test
    @Order(3)
    public void testUpdateNonExistentResource() {
        SystemResourceDO updateResource = new SystemResourceDO();
        updateResource.setId(999999); // 不存在的ID
        updateResource.setResourceName("不存在的资源");

        try {
            resourceManageService.updateResource(updateResource);
            Assertions.fail("应该抛出异常，因为资源不存在");
        } catch (ResourceException e) {
            Assertions.assertEquals(ResourceException.RESOURCE_NOT_FOUND, e.getErrorCode());
        }
    }

    /**
     * 测试更新资源状态
     */
    @Test
    @Order(4)
    public void testUpdateResourceStatus() {
        // 更新资源状态为禁用
        resourceManageService.updateResourceStatus(testResourceId, 1);

        // 验证状态更新成功
        SystemResourceDO resource = resourceQueryService.getResource(testResourceId);
        Assertions.assertNotNull(resource);
        Assertions.assertEquals(1, resource.getStatus());

        // 恢复状态为正常
        resourceManageService.updateResourceStatus(testResourceId, 0);

        // 验证状态恢复成功
        SystemResourceDO restoredResource = resourceQueryService.getResource(testResourceId);
        Assertions.assertNotNull(restoredResource);
        Assertions.assertEquals(0, restoredResource.getStatus());
    }

    /**
     * 测试更新不存在资源的状态（应该失败）
     */
    @Test
    @Order(5)
    public void testUpdateNonExistentResourceStatus() {
        try {
            resourceManageService.updateResourceStatus(999999, 1); // 不存在的ID
            Assertions.fail("应该抛出异常，因为资源不存在");
        } catch (ResourceException e) {
            Assertions.assertEquals(ResourceException.RESOURCE_NOT_FOUND, e.getErrorCode());
        }
    }

    /**
     * 测试更新资源分类
     */
    @Test
    @Order(6)
    public void testUpdateResourceCategory() {
        // 创建新的分类
        SystemResourceCategoryDO newCategory = new SystemResourceCategoryDO();
        newCategory.setCategoryName("新的测试分类");
        newCategory.setCategoryCode("new_test_category_" + System.currentTimeMillis());
        newCategory.setDisplayOrder(2);
        newCategory.setStatus(0);

        Integer newCategoryId = resourceCategoryService.createCategory(newCategory);

        // 更新资源分类
        resourceManageService.updateResourceCategory(testResourceId, newCategoryId);

        // 验证分类更新成功
        SystemResourceDO resource = resourceQueryService.getResource(testResourceId);
        Assertions.assertNotNull(resource);
        Assertions.assertEquals(newCategoryId, resource.getCategoryId());

        // 恢复原分类
        resourceManageService.updateResourceCategory(testResourceId, testCategoryId);

        // 清理新分类
        resourceCategoryService.deleteCategory(newCategoryId);
    }

    /**
     * 测试更新不存在资源的分类（应该失败）
     */
    @Test
    @Order(7)
    public void testUpdateNonExistentResourceCategory() {
        try {
            resourceManageService.updateResourceCategory(999999, testCategoryId); // 不存在的ID
            Assertions.fail("应该抛出异常，因为资源不存在");
        } catch (ResourceException e) {
            Assertions.assertEquals(ResourceException.RESOURCE_NOT_FOUND, e.getErrorCode());
        }
    }

    /**
     * 测试更新资源标签
     */
    @Test
    @Order(8)
    public void testUpdateResourceTags() {
        String newTags = "新标签1,新标签2,新标签3";

        // 更新资源标签
        resourceManageService.updateResourceTags(testResourceId, newTags);

        // 验证标签更新成功
        SystemResourceDO resource = resourceQueryService.getResource(testResourceId);
        Assertions.assertNotNull(resource);
        Assertions.assertEquals(newTags, resource.getTags());
    }

    /**
     * 测试更新不存在资源的标签（应该失败）
     */
    @Test
    @Order(9)
    public void testUpdateNonExistentResourceTags() {
        try {
            resourceManageService.updateResourceTags(999999, "标签"); // 不存在的ID
            Assertions.fail("应该抛出异常，因为资源不存在");
        } catch (ResourceException e) {
            Assertions.assertEquals(ResourceException.RESOURCE_NOT_FOUND, e.getErrorCode());
        }
    }

    /**
     * 测试批量删除资源
     */
    @Test
    @Order(10)
    public void testBatchDeleteResources() {
        Integer[] ids = {testResourceId, testResourceId2};

        // 批量删除资源
        resourceManageService.batchDeleteResources(ids);

        // 验证资源已被删除
        for (Integer id : ids) {
            try {
                SystemResourceDO resource = resourceQueryService.getResource(id);
                Assertions.assertNull(resource); // 如果返回null，说明已删除
            } catch (ResourceException e) {
                // 如果抛出异常，验证是否为资源不存在的异常
                Assertions.assertEquals(ResourceException.RESOURCE_NOT_FOUND, e.getErrorCode());
            }
        }
    }

    /**
     * 测试删除单个资源
     */
    @Test
    @Order(11)
    public void testDeleteResource() {
        // 创建新的测试资源用于删除测试
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "delete_test.txt",
                "text/plain",
                "删除测试文件内容".getBytes(StandardCharsets.UTF_8)
        );

        SystemResourceDO resource = resourceUploadService.uploadFile(file, testCategoryId, "删除测试标签", "删除测试描述");
        Integer deleteResourceId = resource.getId();

        // 删除资源
        resourceManageService.deleteResource(deleteResourceId);

        // 验证资源已被删除
        try {
            SystemResourceDO deletedResource = resourceQueryService.getResource(deleteResourceId);
            Assertions.assertNull(deletedResource); // 如果返回null，说明已删除
        } catch (ResourceException e) {
            // 如果抛出异常，验证是否为资源不存在的异常
            Assertions.assertEquals(ResourceException.RESOURCE_NOT_FOUND, e.getErrorCode());
        }
    }

    /**
     * 测试删除不存在的资源（应该失败）
     */
    @Test
    @Order(12)
    public void testDeleteNonExistentResource() {
        try {
            resourceManageService.deleteResource(999999); // 不存在的ID
            Assertions.fail("应该抛出异常，因为资源不存在");
        } catch (ResourceException e) {
            Assertions.assertEquals(ResourceException.RESOURCE_NOT_FOUND, e.getErrorCode());
        }
    }

    /**
     * 清理测试数据
     */
    @Test
    @Order(13)
    public void testCleanupData() {
        // 清理测试分类
        if (testCategoryId != null) {
            try {
                resourceCategoryService.deleteCategory(testCategoryId);
            } catch (Exception e) {
                log.warn("清理测试分类失败: {}", e.getMessage());
            }
        }
    }
}
