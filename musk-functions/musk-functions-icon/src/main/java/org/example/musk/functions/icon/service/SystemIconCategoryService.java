package org.example.musk.functions.icon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.musk.functions.icon.controller.vo.SystemIconCategoryTreeVO;
import org.example.musk.functions.icon.entity.SystemIconCategoryDO;

import java.util.List;

/**
 * 图标分类 Service 接口
 *
 * @author musk-functions-icon
 */
public interface SystemIconCategoryService extends IService<SystemIconCategoryDO> {

    /**
     * 创建图标分类
     *
     * @param category 图标分类信息
     * @return 分类ID
     */
    Integer createCategory(SystemIconCategoryDO category);

    /**
     * 更新图标分类
     *
     * @param category 图标分类信息
     */
    void updateCategory(SystemIconCategoryDO category);

    /**
     * 删除图标分类
     *
     * @param id 分类ID
     */
    void deleteCategory(Integer id);

    /**
     * 获取图标分类
     *
     * @param id 分类ID
     * @return 图标分类信息
     */
    SystemIconCategoryDO getCategory(Integer id);

    /**
     * 获取所有顶级分类
     *
     * @return 顶级分类列表
     */
    List<SystemIconCategoryDO> getRootCategories();

    /**
     * 获取子分类
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<SystemIconCategoryDO> getChildCategories(Integer parentId);

    /**
     * 获取分类树
     *
     * @return 分类树
     */
    List<SystemIconCategoryTreeVO> getCategoryTree();
}
