package org.example.musk.functions.icon.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.framework.permission.enums.OperationTypeEnum;
import org.example.musk.framework.permission.enums.ResourceTypeEnum;
import org.example.musk.framework.permission.web.anno.RequirePermission;
import org.example.musk.functions.icon.controller.vo.SystemIconResourceCreateReqVO;
import org.example.musk.functions.icon.controller.vo.SystemIconResourceRespVO;
import org.example.musk.functions.icon.controller.vo.SystemIconResourceUpdateReqVO;
import org.example.musk.functions.icon.entity.SystemIconResourceDO;
import org.example.musk.functions.icon.service.SystemIconResourceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.example.musk.common.pojo.CommonResult.success;

/**
 * 图标资源 Controller
 *
 * @author musk-functions-icon
 */
@RestController
@RequestMapping("/api/icon-resource")
@Validated
@Slf4j
public class SystemIconResourceController {

    @Resource
    private SystemIconResourceService systemIconResourceService;

    @PostMapping("/create")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.CREATE)
    public CommonResult<Integer> createIconResource(@Valid @RequestBody SystemIconResourceCreateReqVO createReqVO) {
        // 转换请求VO为DO
        SystemIconResourceDO resource = BeanUtils.toBean(createReqVO, SystemIconResourceDO.class);
        // 创建图标资源
        Integer resourceId = systemIconResourceService.createIconResource(resource);
        return success(resourceId);
    }

    @PutMapping("/update")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.UPDATE)
    public CommonResult<Boolean> updateIconResource(@Valid @RequestBody SystemIconResourceUpdateReqVO updateReqVO) {
        // 转换请求VO为DO
        SystemIconResourceDO resource = BeanUtils.toBean(updateReqVO, SystemIconResourceDO.class);
        // 更新图标资源
        systemIconResourceService.updateIconResource(resource);
        return success(true);
    }

    @DeleteMapping("/delete")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.DELETE)
    public CommonResult<Boolean> deleteIconResource(@RequestParam("id") Integer id) {
        systemIconResourceService.deleteIconResource(id);
        return success(true);
    }

    @GetMapping("/get")
    @RequirePermission(resourceType = ResourceTypeEnum.ICON, operationType = OperationTypeEnum.READ)
    public CommonResult<SystemIconResourceRespVO> getIconResource(@RequestParam("id") Integer id) {
        SystemIconResourceDO resource = systemIconResourceService.getIconResource(id);
        return success(BeanUtils.toBean(resource, SystemIconResourceRespVO.class));
    }
}
