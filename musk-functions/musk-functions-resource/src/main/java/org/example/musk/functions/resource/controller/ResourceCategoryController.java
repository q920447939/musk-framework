package org.example.musk.functions.resource.controller;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.functions.resource.entity.SystemResourceCategoryDO;
import org.example.musk.functions.resource.service.ResourceCategoryService;
import org.example.musk.functions.resource.vo.ResourceCategoryCreateReqVO;
import org.example.musk.functions.resource.vo.ResourceCategoryRespVO;
import org.example.musk.functions.resource.vo.ResourceCategoryTreeVO;
import org.example.musk.functions.resource.vo.ResourceCategoryUpdateReqVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.musk.common.pojo.CommonResult.success;

/**
 * 资源分类控制器
 *
 * @author musk-functions-resource
 */
@RestController
@RequestMapping("/api/resource/category")
@Validated
@Slf4j
public class ResourceCategoryController {

    @Resource
    private ResourceCategoryService resourceCategoryService;

    /**
     * 创建分类
     *
     * @param createReqVO 创建请求
     * @return 分类ID
     */
    @PostMapping("/create")
    public CommonResult<Integer> createCategory(@Validated @RequestBody ResourceCategoryCreateReqVO createReqVO) {
        SystemResourceCategoryDO category = BeanUtil.copyProperties(createReqVO, SystemResourceCategoryDO.class);
        Integer categoryId = resourceCategoryService.createCategory(category);
        return success(categoryId);
    }

    /**
     * 更新分类
     *
     * @param updateReqVO 更新请求
     * @return 更新结果
     */
    @PutMapping("/update")
    public CommonResult<Boolean> updateCategory(@Validated @RequestBody ResourceCategoryUpdateReqVO updateReqVO) {
        SystemResourceCategoryDO category = BeanUtil.copyProperties(updateReqVO, SystemResourceCategoryDO.class);
        resourceCategoryService.updateCategory(category);
        return success(true);
    }

    /**
     * 删除分类
     *
     * @param id 分类ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public CommonResult<Boolean> deleteCategory(@PathVariable("id") Integer id) {
        resourceCategoryService.deleteCategory(id);
        return success(true);
    }

    /**
     * 获取分类信息
     *
     * @param id 分类ID
     * @return 分类信息
     */
    @GetMapping("/get/{id}")
    public CommonResult<ResourceCategoryRespVO> getCategory(@PathVariable("id") Integer id) {
        SystemResourceCategoryDO category = resourceCategoryService.getCategory(id);
        ResourceCategoryRespVO respVO = BeanUtil.copyProperties(category, ResourceCategoryRespVO.class);
        return success(respVO);
    }

    /**
     * 获取分类列表
     *
     * @return 分类列表
     */
    @GetMapping("/list")
    public CommonResult<List<ResourceCategoryRespVO>> listCategories() {
        List<SystemResourceCategoryDO> categories = resourceCategoryService.listCategories();
        List<ResourceCategoryRespVO> respVOs = categories.stream()
                .map(category -> BeanUtil.copyProperties(category, ResourceCategoryRespVO.class))
                .collect(Collectors.toList());
        return success(respVOs);
    }

    /**
     * 获取子分类列表
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    @GetMapping("/list-children")
    public CommonResult<List<ResourceCategoryRespVO>> listChildCategories(@RequestParam("parentId") Integer parentId) {
        List<SystemResourceCategoryDO> categories = resourceCategoryService.listChildCategories(parentId);
        List<ResourceCategoryRespVO> respVOs = categories.stream()
                .map(category -> BeanUtil.copyProperties(category, ResourceCategoryRespVO.class))
                .collect(Collectors.toList());
        return success(respVOs);
    }

    /**
     * 获取分类树
     *
     * @return 分类树
     */
    @GetMapping("/tree")
    public CommonResult<List<ResourceCategoryTreeVO>> getCategoryTree() {
        List<ResourceCategoryTreeVO> tree = resourceCategoryService.getCategoryTree();
        return success(tree);
    }

    /**
     * 更新分类状态
     *
     * @param id     分类ID
     * @param status 状态
     * @return 更新结果
     */
    @PutMapping("/update-status")
    public CommonResult<Boolean> updateCategoryStatus(
            @RequestParam("id") Integer id,
            @RequestParam("status") Integer status) {
        resourceCategoryService.updateCategoryStatus(id, status);
        return success(true);
    }
}
