package org.example.musk.tests.resource.service;

import jakarta.annotation.Resource;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.functions.resource.constant.ResourceConstant;
import org.example.musk.functions.resource.entity.SystemResourceCategoryDO;
import org.example.musk.functions.resource.exception.ResourceException;
import org.example.musk.functions.resource.service.ResourceCategoryService;
import org.example.musk.functions.resource.vo.ResourceCategoryTreeVO;
import org.example.musk.tests.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

/**
 * 资源分类服务测试类
 *
 * @author musk-functions-resource
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ResourceCategoryServiceTest extends BaseTest {

    @Resource
    private ResourceCategoryService resourceCategoryService;

    private static Integer testCategoryId;
    private static Integer testChildCategoryId;

    /**
     * 测试创建资源分类
     */
    @Test
    @Order(1)
    public void testCreateCategory() {
        // 创建一个根分类
        SystemResourceCategoryDO category = new SystemResourceCategoryDO();
        category.setCategoryName("测试资源分类");
        category.setCategoryCode("test_resource_category_" + System.currentTimeMillis());
        category.setDisplayOrder(1);
        category.setStatus(0); // 正常状态
        category.setRemark("测试资源分类备注");

        testCategoryId = resourceCategoryService.createCategory(category);
        Assertions.assertNotNull(testCategoryId);
        Assertions.assertTrue(testCategoryId > 0);

        // 创建一个子分类
        SystemResourceCategoryDO childCategory = new SystemResourceCategoryDO();
        childCategory.setCategoryName("测试子资源分类");
        childCategory.setCategoryCode("test_child_resource_category_" + System.currentTimeMillis());
        childCategory.setParentId(testCategoryId);
        childCategory.setDisplayOrder(1);
        childCategory.setStatus(0); // 正常状态
        childCategory.setRemark("测试子资源分类备注");

        testChildCategoryId = resourceCategoryService.createCategory(childCategory);
        Assertions.assertNotNull(testChildCategoryId);
        Assertions.assertTrue(testChildCategoryId > 0);
    }

    /**
     * 测试获取资源分类
     */
    @Test
    @Order(2)
    public void testGetCategory() {
        // 获取创建的分类
        SystemResourceCategoryDO category = resourceCategoryService.getCategory(testCategoryId);

        Assertions.assertNotNull(category);
        Assertions.assertEquals(testCategoryId, category.getId());
        Assertions.assertEquals(ThreadLocalTenantContext.getTenantId(), category.getTenantId());
        Assertions.assertEquals(ThreadLocalTenantContext.getDomainId(), category.getDomainId());
        Assertions.assertEquals(0, category.getStatus()); // 正常状态
        Assertions.assertEquals("测试资源分类", category.getCategoryName());
    }

    /**
     * 测试更新资源分类
     */
    @Test
    @Order(3)
    public void testUpdateCategory() {
        // 获取创建的分类
        SystemResourceCategoryDO category = resourceCategoryService.getCategory(testCategoryId);

        // 更新分类信息
        String updatedName = "更新后的测试资源分类";
        String updatedRemark = "更新后的测试资源分类备注";
        category.setCategoryName(updatedName);
        category.setRemark(updatedRemark);

        resourceCategoryService.updateCategory(category);

        // 重新获取分类，验证更新是否成功
        SystemResourceCategoryDO updatedCategory = resourceCategoryService.getCategory(testCategoryId);
        Assertions.assertNotNull(updatedCategory);
        Assertions.assertEquals(updatedName, updatedCategory.getCategoryName());
        Assertions.assertEquals(updatedRemark, updatedCategory.getRemark());
    }

    /**
     * 测试获取分类列表
     */
    @Test
    @Order(4)
    public void testListCategories() {
        // 获取分类列表
        List<SystemResourceCategoryDO> categories = resourceCategoryService.listCategories();

        Assertions.assertNotNull(categories);
        Assertions.assertFalse(categories.isEmpty());

        // 验证我们创建的分类在列表中
        boolean foundTestCategory = categories.stream()
                .anyMatch(category -> category.getId().equals(testCategoryId));
        Assertions.assertTrue(foundTestCategory);

        boolean foundTestChildCategory = categories.stream()
                .anyMatch(category -> category.getId().equals(testChildCategoryId));
        Assertions.assertTrue(foundTestChildCategory);
    }

    /**
     * 测试获取子分类列表
     */
    @Test
    @Order(5)
    public void testListChildCategories() {
        // 获取子分类列表
        List<SystemResourceCategoryDO> childCategories = resourceCategoryService.listChildCategories(testCategoryId);

        Assertions.assertNotNull(childCategories);
        Assertions.assertFalse(childCategories.isEmpty());

        // 验证子分类的parentId等于testCategoryId
        for (SystemResourceCategoryDO category : childCategories) {
            Assertions.assertEquals(testCategoryId, category.getParentId());
        }

        // 验证我们创建的子分类在列表中
        boolean foundTestChildCategory = childCategories.stream()
                .anyMatch(category -> category.getId().equals(testChildCategoryId));
        Assertions.assertTrue(foundTestChildCategory);
    }

    /**
     * 测试获取分类树
     */
    @Test
    @Order(6)
    public void testGetCategoryTree() {
        // 获取分类树
        List<ResourceCategoryTreeVO> categoryTree = resourceCategoryService.getCategoryTree();

        Assertions.assertNotNull(categoryTree);
        Assertions.assertFalse(categoryTree.isEmpty());

        // 验证我们创建的根分类在树中
        boolean foundTestCategory = categoryTree.stream()
                .anyMatch(category -> category.getId().equals(testCategoryId));
        Assertions.assertTrue(foundTestCategory);

        // 验证子分类在树的children中
        boolean foundTestChildCategory = false;
        for (ResourceCategoryTreeVO treeNode : categoryTree) {
            if (treeNode.getId().equals(testCategoryId) && treeNode.getChildren() != null) {
                foundTestChildCategory = treeNode.getChildren().stream()
                        .anyMatch(child -> child.getId().equals(testChildCategoryId));
                if (foundTestChildCategory) {
                    break;
                }
            }
        }
        Assertions.assertTrue(foundTestChildCategory);
    }

    /**
     * 测试更新分类状态
     */
    @Test
    @Order(7)
    public void testUpdateCategoryStatus() {
        // 更新分类状态为禁用
        resourceCategoryService.updateCategoryStatus(testCategoryId, 1);

        // 验证状态更新成功
        SystemResourceCategoryDO category = resourceCategoryService.getCategory(testCategoryId);
        Assertions.assertNotNull(category);
        Assertions.assertEquals(1, category.getStatus());

        // 恢复状态为正常
        resourceCategoryService.updateCategoryStatus(testCategoryId, 0);

        // 验证状态恢复成功
        SystemResourceCategoryDO restoredCategory = resourceCategoryService.getCategory(testCategoryId);
        Assertions.assertNotNull(restoredCategory);
        Assertions.assertEquals(0, restoredCategory.getStatus());
    }

    /**
     * 测试删除分类 - 先删除子分类，再删除父分类
     */
    @Test
    @Order(8)
    public void testDeleteCategory() {
        // 先删除子分类
        resourceCategoryService.deleteCategory(testChildCategoryId);

        // 验证子分类已被删除
        try {
            SystemResourceCategoryDO childCategory = resourceCategoryService.getCategory(testChildCategoryId);
            Assertions.assertNull(childCategory); // 如果返回null，说明已删除
        } catch (ResourceException e) {
            // 如果抛出异常，验证是否为分类不存在的异常
            Assertions.assertEquals(ResourceException.CATEGORY_NOT_FOUND, e.getErrorCode());
        }

        // 再删除父分类
        resourceCategoryService.deleteCategory(testCategoryId);

        // 验证父分类已被删除
        try {
            SystemResourceCategoryDO parentCategory = resourceCategoryService.getCategory(testCategoryId);
            Assertions.assertNull(parentCategory); // 如果返回null，说明已删除
        } catch (ResourceException e) {
            // 如果抛出异常，验证是否为分类不存在的异常
            Assertions.assertEquals(ResourceException.CATEGORY_NOT_FOUND, e.getErrorCode());
        }
    }

    /**
     * 测试删除有子分类的分类（应该失败）
     */
    @Test
    @Order(9)
    public void testDeleteCategoryWithChildren() {
        // 创建一个新的父分类
        SystemResourceCategoryDO parentCategory = new SystemResourceCategoryDO();
        parentCategory.setCategoryName("测试父资源分类");
        parentCategory.setCategoryCode("test_parent_resource_category_" + System.currentTimeMillis());
        parentCategory.setDisplayOrder(1);
        parentCategory.setStatus(0);

        Integer parentId = resourceCategoryService.createCategory(parentCategory);

        // 创建一个子分类
        SystemResourceCategoryDO childCategory = new SystemResourceCategoryDO();
        childCategory.setCategoryName("测试子资源分类");
        childCategory.setCategoryCode("test_child_resource_category_" + System.currentTimeMillis());
        childCategory.setParentId(parentId);
        childCategory.setDisplayOrder(1);
        childCategory.setStatus(0);

        Integer childId = resourceCategoryService.createCategory(childCategory);

        // 尝试删除父分类，应该失败
        try {
            resourceCategoryService.deleteCategory(parentId);
            Assertions.fail("应该抛出异常，因为分类下有子分类");
        } catch (ResourceException e) {
            // 验证是否为分类有子分类的异常
            Assertions.assertEquals("category.has.children", e.getErrorMsg());
        }

        // 清理测试数据
        resourceCategoryService.deleteCategory(childId);
        resourceCategoryService.deleteCategory(parentId);
    }
}
