package org.example.musk.framework.tenant.controller.tenant;


import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.musk.common.enums.tenant.CommonStatusEnum;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.pojo.tenant.TenantPackageDO;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.framework.tenant.controller.tenant.vo.packages.TenantPackagePageReqVO;
import org.example.musk.framework.tenant.controller.tenant.vo.packages.TenantPackageRespVO;
import org.example.musk.framework.tenant.controller.tenant.vo.packages.TenantPackageSaveReqVO;
import org.example.musk.framework.tenant.controller.tenant.vo.packages.TenantPackageSimpleRespVO;
import org.example.musk.framework.tenant.service.tenant.TenantPackageService;
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


@RestController
@RequestMapping("/system/tenant-package")
@Validated
public class TenantPackageController {

    @Resource
    private TenantPackageService tenantPackageService;
/*

    @PostMapping("/create")
    public CommonResult<Long> createTenantPackage(@Valid @RequestBody TenantPackageSaveReqVO createReqVO) {
        return success(tenantPackageService.createTenantPackage(createReqVO));
    }

    @PutMapping("/update")
    public CommonResult<Boolean> updateTenantPackage(@Valid @RequestBody TenantPackageSaveReqVO updateReqVO) {
        tenantPackageService.updateTenantPackage(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteTenantPackage(@RequestParam("id") Long id) {
        tenantPackageService.deleteTenantPackage(id);
        return success(true);
    }

    @GetMapping("/get")
    public CommonResult<TenantPackageRespVO> getTenantPackage(@RequestParam("id") Long id) {
        TenantPackageDO tenantPackage = tenantPackageService.getTenantPackage(id);
        return success(BeanUtils.toBean(tenantPackage, TenantPackageRespVO.class));
    }

    @GetMapping("/page")
    public CommonResult<PageResult<TenantPackageRespVO>> getTenantPackagePage(@Valid TenantPackagePageReqVO pageVO) {
        PageResult<TenantPackageDO> pageResult = tenantPackageService.getTenantPackagePage(pageVO);
        return success(BeanUtils.toBean(pageResult, TenantPackageRespVO.class));
    }

    @GetMapping({"/get-simple-list", "simple-list"})
    public CommonResult<List<TenantPackageSimpleRespVO>> getTenantPackageList() {
        List<TenantPackageDO> list = tenantPackageService.getTenantPackageListByStatus(CommonStatusEnum.ENABLE.getStatus());
        return success(BeanUtils.toBean(list, TenantPackageSimpleRespVO.class));
    }
*/

}
