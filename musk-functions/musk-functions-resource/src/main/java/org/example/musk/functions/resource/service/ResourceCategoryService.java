package org.example.musk.functions.resource.service;

import org.example.musk.functions.resource.entity.SystemResourceCategoryDO;
import org.example.musk.functions.resource.vo.ResourceCategoryTreeVO;

import java.util.List;

/**
 * 资源分类服务接口
 *
 * @author musk-functions-resource
 */
public interface ResourceCategoryService {

    /**
     * 创建分类
     *
     * @param category 分类信息
     * @return 分类ID
     */
    Integer createCategory(SystemResourceCategoryDO category);

    /**
     * 更新分类
     *
     * @param category 分类信息
     */
    void updateCategory(SystemResourceCategoryDO category);

    /**
     * 删除分类
     *
     * @param id 分类ID
     */
    void deleteCategory(Integer id);

    /**
     * 获取分类信息
     *
     * @param id 分类ID
     * @return 分类信息
     */
    SystemResourceCategoryDO getCategory(Integer id);

    /**
     * 获取分类列表
     *
     * @return 分类列表
     */
    List<SystemResourceCategoryDO> listCategories();

    /**
     * 获取子分类列表
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<SystemResourceCategoryDO> listChildCategories(Integer parentId);

    /**
     * 获取分类树
     *
     * @return 分类树
     */
    List<ResourceCategoryTreeVO> getCategoryTree();

    /**
     * 更新分类状态
     *
     * @param id     分类ID
     * @param status 状态
     */
    void updateCategoryStatus(Integer id, Integer status);
}
