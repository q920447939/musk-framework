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
import org.example.musk.functions.icon.controller.vo.SystemIconCreateReqVO;
import org.example.musk.functions.icon.controller.vo.SystemIconResourceRespVO;
import org.example.musk.functions.icon.controller.vo.SystemIconRespVO;
import org.example.musk.functions.icon.controller.vo.SystemIconUpdateReqVO;
import org.example.musk.functions.icon.entity.SystemIconDO;
import org.example.musk.functions.icon.entity.SystemIconResourceDO;
import org.example.musk.functions.icon.service.SystemIconResourceService;
import org.example.musk.functions.icon.service.SystemIconService;
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
 * 图标 Controller
 *
 * @author musk-functions-icon
 */
@RestController
@RequestMapping("/api/icon")
@Validated
@Slf4j
public class SystemIconController {

    @Resource
    private SystemIconService systemIconService;

    @Resource
    private SystemIconResourceService systemIconResourceService;

    @PostMapping("/create")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.CREATE)
    public CommonResult<Integer> createIcon(@Valid @RequestBody SystemIconCreateReqVO createReqVO) {
        // 转换请求VO为DO
        SystemIconDO icon = BeanUtils.toBean(createReqVO, SystemIconDO.class);
        // 创建图标
        Integer iconId = systemIconService.createIcon(icon);
        return success(iconId);
    }

    @PutMapping("/update")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.UPDATE)
    public CommonResult<Boolean> updateIcon(@Valid @RequestBody SystemIconUpdateReqVO updateReqVO) {
        // 转换请求VO为DO
        SystemIconDO icon = BeanUtils.toBean(updateReqVO, SystemIconDO.class);
        // 更新图标
        systemIconService.updateIcon(icon);
        return success(true);
    }

    @DeleteMapping("/delete")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.DELETE)
    public CommonResult<Boolean> deleteIcon(@RequestParam("id") Integer id) {
        systemIconService.deleteIcon(id);
        return success(true);
    }

    @GetMapping("/get")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.READ)
    public CommonResult<SystemIconRespVO> getIcon(@RequestParam("id") Integer id) {
        SystemIconDO icon = systemIconService.getIcon(id);
        return success(BeanUtils.toBean(icon, SystemIconRespVO.class));
    }

    @GetMapping("/get-by-code")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.READ)
    public CommonResult<SystemIconRespVO> getIconByCode(@RequestParam("code") String code) {
        SystemIconDO icon = systemIconService.getIconByCode(code);
        return success(BeanUtils.toBean(icon, SystemIconRespVO.class));
    }

    @GetMapping("/list-by-category")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.READ)
    public CommonResult<List<SystemIconRespVO>> getIconsByCategory(@RequestParam("categoryId") Integer categoryId) {
        List<SystemIconDO> icons = systemIconService.getIconsByCategory(categoryId);
        return success(BeanUtils.toBean(icons, SystemIconRespVO.class));
    }

    @GetMapping("/search")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.READ)
    public CommonResult<List<SystemIconRespVO>> searchIcons(@RequestParam("keyword") String keyword) {
        List<SystemIconDO> icons = systemIconService.searchIcons(keyword);
        return success(BeanUtils.toBean(icons, SystemIconRespVO.class));
    }

    @GetMapping("/get-resources")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.READ)
    public CommonResult<List<SystemIconResourceRespVO>> getIconResources(@RequestParam("iconId") Integer iconId) {
        List<SystemIconResourceDO> resources = systemIconResourceService.getResourcesByIconId(iconId);
        return success(BeanUtils.toBean(resources, SystemIconResourceRespVO.class));
    }

}
