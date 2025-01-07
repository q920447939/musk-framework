package org.example.musk.framework.tenant.controller.tenant;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.pojo.tenant.TenantDO;
import org.example.musk.common.util.commonResult.CommonResultUtils;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.framework.tenant.controller.tenant.vo.tenant.TenantRespVO;
import org.example.musk.framework.tenant.controller.tenant.vo.tenant.TenantSaveReqVO;
import org.example.musk.framework.tenant.service.tenant.TenantService;
import org.example.musk.framework.tenant.service.tenant.bo.TenantSaveReqBO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.example.musk.common.pojo.CommonResult.success;

@RestController
@RequestMapping("/api/system/tenant")
public class TenantController {

    @Resource
    private TenantService tenantService;

    @GetMapping("/get-id-by-name")
    public CommonResult<Long> getTenantIdByName(@RequestParam("name") String name) {
        TenantDO tenant = tenantService.getTenantByName(name);
        return CommonResultUtils.wrapEmptyObjResult(tenant, tenant::getId);
    }


    @PostMapping("/create")
    public CommonResult<Long> createTenant(@Valid @RequestBody TenantSaveReqVO createReqVO) {
        return success(tenantService.createTenant(BeanUtils.toBean(createReqVO, TenantSaveReqBO.class)));
    }

    @GetMapping("/get")
    public CommonResult<TenantRespVO> getTenant(@RequestParam("id") Long id) {
        TenantDO tenant = tenantService.getTenant(id);
        return success(BeanUtils.toBean(tenant, TenantRespVO.class));
    }

}
