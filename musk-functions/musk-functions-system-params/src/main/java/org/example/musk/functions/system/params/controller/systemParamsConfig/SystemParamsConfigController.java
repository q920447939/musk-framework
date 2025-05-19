package org.example.musk.functions.system.params.controller.systemParamsConfig;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.enums.appConfig.SystemDomain;
import org.example.musk.enums.appConfig.AppParamsConfigTypeEnums;
import org.example.musk.functions.system.params.services.system.params.config.SystemParamsConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 参数配置 Controller
 *
 * @author 代码生成器
 */
@RestController
@RequestMapping("/api/systemParamsConfig")
@Validated
@Slf4j
public class SystemParamsConfigController {

    @Resource
    private SystemParamsConfigService systemParamsConfigService;

    @GetMapping("/get")
    public CommonResult<String> querySystemParamsConfigByTypeValue1(@RequestParam("type") String type) {
        return CommonResult.success(systemParamsConfigService.querySystemParamsConfigByTypeValue1(ThreadLocalTenantContext.getTenantId(), SystemDomain.APP, AppParamsConfigTypeEnums.fromType(type)));
    }
}
