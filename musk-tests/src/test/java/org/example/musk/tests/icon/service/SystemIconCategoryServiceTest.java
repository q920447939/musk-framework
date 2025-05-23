package org.example.musk.tests.icon.service;

import jakarta.annotation.Resource;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.functions.icon.constant.IconConstant;
import org.example.musk.functions.icon.controller.vo.SystemIconCategoryTreeVO;
import org.example.musk.functions.icon.entity.SystemIconCategoryDO;
import org.example.musk.functions.icon.exception.IconException;
import org.example.musk.functions.icon.service.SystemIconCategoryService;
import org.example.musk.tests.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

/**
 * 图标分类服务测试类
 *
 * @author musk-functions-icon
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemIconCategoryServiceTest extends BaseTest {

    @Resource
    private SystemIconCategoryService systemIconCategoryService;

    private static Integer testCategoryId;
    private static Integer testChildCategoryId;
    /**
     * 测试创建图标分类
     */
    @Test
    @Order(1)
    public void testCreateCategory() {
        // 创建一个根分类
        SystemIconCategoryDO category = new SystemIconCategoryDO();
        category.setCategoryName("测试分类");
        category.setCategoryCode("test_category_" + System.currentTimeMillis());
        category.setDisplayOrder(1);
        category.setStatus(IconConstant.STATUS_NORMAL);
        category.setRemark("测试分类备注");

        testCategoryId = systemIconCategoryService.createCategory(category);
        Assertions.assertNotNull(testCategoryId);
        Assertions.assertTrue(testCategoryId > 0);

        // 创建一个子分类
        SystemIconCategoryDO childCategory = new SystemIconCategoryDO();
        childCategory.setCategoryName("测试子分类");
        childCategory.setCategoryCode("test_child_category_" + System.currentTimeMillis());
        childCategory.setParentId(testCategoryId);
        childCategory.setDisplayOrder(1);
        childCategory.setStatus(IconConstant.STATUS_NORMAL);
        childCategory.setRemark("测试子分类备注");

        testChildCategoryId = systemIconCategoryService.createCategory(childCategory);
        Assertions.assertNotNull(testChildCategoryId);
        Assertions.assertTrue(testChildCategoryId > 0);
    }

    /**
     * 测试获取图标分类
     */
    @Test
    @Order(2)
    public void testGetCategory() {
        // 获取创建的分类
        SystemIconCategoryDO category = systemIconCategoryService.getCategory(testCategoryId);

        Assertions.assertNotNull(category);
        Assertions.assertEquals(testCategoryId, category.getId());
        Assertions.assertEquals(ThreadLocalTenantContext.getTenantId(), category.getTenantId());
        Assertions.assertEquals(ThreadLocalTenantContext.getDomainId(), category.getDomainId());
        Assertions.assertEquals(IconConstant.STATUS_NORMAL, category.getStatus());
    }

    /**
     * 测试更新图标分类
     */
    @Test
    @Order(3)
    public void testUpdateCategory() {
        // 获取创建的分类
        SystemIconCategoryDO category = systemIconCategoryService.getCategory(testCategoryId);

        // 更新分类信息
        String updatedName = "更新后的测试分类";
        String updatedRemark = "更新后的测试分类备注";
        category.setCategoryName(updatedName);
        category.setRemark(updatedRemark);

        systemIconCategoryService.updateCategory(category);

        // 重新获取分类，验证更新是否成功
        SystemIconCategoryDO updatedCategory = systemIconCategoryService.getCategory(testCategoryId);
        Assertions.assertNotNull(updatedCategory);
        Assertions.assertEquals(updatedName, updatedCategory.getCategoryName());
        Assertions.assertEquals(updatedRemark, updatedCategory.getRemark());
    }

    /**
     * 测试获取根分类列表
     */
    @Test
    @Order(4)
    public void testGetRootCategories() {
        // 获取根分类列表
        List<SystemIconCategoryDO> rootCategories = systemIconCategoryService.getRootCategories();

        Assertions.assertNotNull(rootCategories);
        Assertions.assertFalse(rootCategories.isEmpty());

        // 验证根分类的parentId为null
        for (SystemIconCategoryDO category : rootCategories) {
            Assertions.assertNull(category.getParentId());
        }

        // 验证我们创建的根分类在列表中
        boolean foundTestCategory = rootCategories.stream()
                .anyMatch(category -> category.getId().equals(testCategoryId));
        Assertions.assertTrue(foundTestCategory);
    }

    /**
     * 测试获取子分类列表
     */
    @Test
    @Order(5)
    public void testGetChildCategories() {
        // 获取子分类列表
        List<SystemIconCategoryDO> childCategories = systemIconCategoryService.getChildCategories(testCategoryId);

        Assertions.assertNotNull(childCategories);
        Assertions.assertFalse(childCategories.isEmpty());

        // 验证子分类的parentId等于testCategoryId
        for (SystemIconCategoryDO category : childCategories) {
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
        List<SystemIconCategoryTreeVO> categoryTree = systemIconCategoryService.getCategoryTree();

        Assertions.assertNotNull(categoryTree);
        Assertions.assertFalse(categoryTree.isEmpty());

        // 验证我们创建的根分类在树中
        boolean foundTestCategory = categoryTree.stream()
                .anyMatch(category -> category.getId().equals(testCategoryId));
        Assertions.assertTrue(foundTestCategory);

        // 验证子分类在树的children中
        boolean foundTestChildCategory = false;
        for (SystemIconCategoryTreeVO treeNode : categoryTree) {
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
     * 测试删除分类 - 先删除子分类，再删除父分类
     */
    @Test
    @Order(7)
    public void testDeleteCategory() {
        // 先删除子分类
        systemIconCategoryService.deleteCategory(testChildCategoryId);

        // 验证子分类已被删除
        try {
            SystemIconCategoryDO childCategory = systemIconCategoryService.getCategory(testChildCategoryId);
            Assertions.assertNull(childCategory); // 如果返回null，说明已删除
        } catch (IconException e) {
            // 如果抛出异常，验证是否为分类不存在的异常
            Assertions.assertEquals(IconException.CATEGORY_NOT_EXISTS.getCode(), e.getCode());
        }

        // 再删除父分类
        systemIconCategoryService.deleteCategory(testCategoryId);

        // 验证父分类已被删除
        try {
            SystemIconCategoryDO parentCategory = systemIconCategoryService.getCategory(testCategoryId);
            Assertions.assertNull(parentCategory); // 如果返回null，说明已删除
        } catch (IconException e) {
            // 如果抛出异常，验证是否为分类不存在的异常
            Assertions.assertEquals(IconException.CATEGORY_NOT_EXISTS.getCode(), e.getCode());
        }
    }

    /**
     * 测试删除有子分类的分类（应该失败）
     */
    @Test
    @Order(8)
    public void testDeleteCategoryWithChildren() {
        // 创建一个新的父分类
        SystemIconCategoryDO parentCategory = new SystemIconCategoryDO();
        parentCategory.setCategoryName("测试父分类");
        parentCategory.setCategoryCode("test_parent_category_" + System.currentTimeMillis());
        parentCategory.setDisplayOrder(1);
        parentCategory.setStatus(IconConstant.STATUS_NORMAL);

        Integer parentId = systemIconCategoryService.createCategory(parentCategory);

        // 创建一个子分类
        SystemIconCategoryDO childCategory = new SystemIconCategoryDO();
        childCategory.setCategoryName("测试子分类");
        childCategory.setCategoryCode("test_child_category_" + System.currentTimeMillis());
        childCategory.setParentId(parentId);
        childCategory.setDisplayOrder(1);
        childCategory.setStatus(IconConstant.STATUS_NORMAL);

        Integer childId = systemIconCategoryService.createCategory(childCategory);

        // 尝试删除父分类，应该失败
        try {
            systemIconCategoryService.deleteCategory(parentId);
            Assertions.fail("应该抛出异常，因为分类下有子分类");
        } catch (IconException e) {
            // 验证是否为分类有子分类的异常
            Assertions.assertEquals(IconException.CATEGORY_HAS_CHILDREN.getCode(), e.getCode());
        }

        // 清理测试数据
        systemIconCategoryService.deleteCategory(childId);
        systemIconCategoryService.deleteCategory(parentId);
    }
}
