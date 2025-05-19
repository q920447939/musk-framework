package org.example.musk.functions.icon.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.framework.permission.enums.OperationTypeEnum;
import org.example.musk.framework.permission.enums.ResourceTypeEnum;
import org.example.musk.framework.permission.web.anno.RequirePermission;
import org.example.musk.functions.icon.controller.vo.SystemIconCategoryCreateReqVO;
import org.example.musk.functions.icon.controller.vo.SystemIconCategoryRespVO;
import org.example.musk.functions.icon.controller.vo.SystemIconCategoryTreeVO;
import org.example.musk.functions.icon.controller.vo.SystemIconCategoryUpdateReqVO;
import org.example.musk.functions.icon.entity.SystemIconCategoryDO;
import org.example.musk.functions.icon.service.SystemIconCategoryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.example.musk.common.pojo.CommonResult.success;

/**
 * 图标分类 Controller
 *
 * @author musk-functions-icon
 */
@RestController
@RequestMapping("/api/icon-category")
@Validated
@Slf4j
public class SystemIconCategoryController {

    @Resource
    private SystemIconCategoryService systemIconCategoryService;

    @PostMapping("/create")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.CREATE)
    public CommonResult<Integer> createCategory(@Valid @RequestBody SystemIconCategoryCreateReqVO createReqVO) {
        // 转换请求VO为DO
        SystemIconCategoryDO category = BeanUtils.toBean(createReqVO, SystemIconCategoryDO.class);
        // 设置当前租户和域
        category.setTenantId(ThreadLocalTenantContext.getTenantId());
        category.setDomainId(ThreadLocalTenantContext.getDomainId());
        // 创建分类
        Integer categoryId = systemIconCategoryService.createCategory(category);
        return success(categoryId);
    }

    @PutMapping("/update")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.UPDATE)
    public CommonResult<Boolean> updateCategory(@Valid @RequestBody SystemIconCategoryUpdateReqVO updateReqVO) {
        // 转换请求VO为DO
        SystemIconCategoryDO category = BeanUtils.toBean(updateReqVO, SystemIconCategoryDO.class);
        // 更新分类
        systemIconCategoryService.updateCategory(category);
        return success(true);
    }

    @DeleteMapping("/delete")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.DELETE)
    public CommonResult<Boolean> deleteCategory(@RequestParam("id") Integer id) {
        systemIconCategoryService.deleteCategory(id);
        return success(true);
    }

    @GetMapping("/get")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.READ)
    public CommonResult<SystemIconCategoryRespVO> getCategory(@RequestParam("id") Integer id) {
        SystemIconCategoryDO category = systemIconCategoryService.getCategory(id);
        return success(BeanUtils.toBean(category, SystemIconCategoryRespVO.class));
    }

    @GetMapping("/list-root")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.READ)
    public CommonResult<List<SystemIconCategoryRespVO>> getRootCategories() {
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();
        List<SystemIconCategoryDO> categories = systemIconCategoryService.getRootCategories(tenantId, domainId);
        return success(BeanUtils.toBean(categories, SystemIconCategoryRespVO.class));
    }

    @GetMapping("/list-children")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.READ)
    public CommonResult<List<SystemIconCategoryRespVO>> getChildCategories(@RequestParam("parentId") Integer parentId) {
        List<SystemIconCategoryDO> categories = systemIconCategoryService.getChildCategories(parentId);
        return success(BeanUtils.toBean(categories, SystemIconCategoryRespVO.class));
    }

    @GetMapping("/tree")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.READ)
    public CommonResult<List<SystemIconCategoryTreeVO>> getCategoryTree() {
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();
        List<SystemIconCategoryTreeVO> tree = systemIconCategoryService.getCategoryTree(tenantId, domainId);
        return success(tree);
    }
}
