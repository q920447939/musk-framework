package org.example.musk.functions.resource.controller;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.functions.resource.entity.SystemResourceDO;
import org.example.musk.functions.resource.service.ResourceManageService;
import org.example.musk.functions.resource.service.ResourceQueryService;
import org.example.musk.functions.resource.vo.ResourceRespVO;
import org.example.musk.functions.resource.vo.ResourceUpdateReqVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

import static org.example.musk.common.pojo.CommonResult.success;

/**
 * 资源管理控制器
 *
 * @author musk-functions-resource
 */
@RestController
@RequestMapping("/api/resource")
@Validated
@Slf4j
public class ResourceManageController {

    @Resource
    private ResourceManageService resourceManageService;

    @Resource
    private ResourceQueryService resourceQueryService;

    /**
     * 更新资源信息
     *
     * @param updateReqVO 更新请求
     * @return 更新结果
     */
    @PutMapping("/update")
    public CommonResult<Boolean> updateResource(@Validated @RequestBody ResourceUpdateReqVO updateReqVO) {
        // 获取原资源信息
        SystemResourceDO resource = resourceQueryService.getResource(updateReqVO.getId());
        // 更新资源信息
        BeanUtil.copyProperties(updateReqVO, resource);
        resourceManageService.updateResource(resource);
        return success(true);
    }

    /**
     * 删除资源
     *
     * @param id 资源ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public CommonResult<Boolean> deleteResource(@PathVariable("id") Integer id) {
        resourceManageService.deleteResource(id);
        return success(true);
    }

    /**
     * 批量删除资源
     *
     * @param ids 资源ID数组
     * @return 删除结果
     */
    @DeleteMapping("/batch-delete")
    public CommonResult<Boolean> batchDeleteResources(@RequestParam("ids") Integer[] ids) {
        resourceManageService.batchDeleteResources(ids);
        return success(true);
    }

    /**
     * 更新资源状态
     *
     * @param id     资源ID
     * @param status 状态
     * @return 更新结果
     */
    @PutMapping("/update-status")
    public CommonResult<Boolean> updateResourceStatus(
            @RequestParam("id") Integer id,
            @RequestParam("status") Integer status) {
        resourceManageService.updateResourceStatus(id, status);
        return success(true);
    }

    /**
     * 更新资源分类
     *
     * @param id         资源ID
     * @param categoryId 分类ID
     * @return 更新结果
     */
    @PutMapping("/update-category")
    public CommonResult<Boolean> updateResourceCategory(
            @RequestParam("id") Integer id,
            @RequestParam("categoryId") Integer categoryId) {
        resourceManageService.updateResourceCategory(id, categoryId);
        return success(true);
    }

    /**
     * 更新资源标签
     *
     * @param id   资源ID
     * @param tags 标签
     * @return 更新结果
     */
    @PutMapping("/update-tags")
    public CommonResult<Boolean> updateResourceTags(
            @RequestParam("id") Integer id,
            @RequestParam("tags") String tags) {
        resourceManageService.updateResourceTags(id, tags);
        return success(true);
    }
}
