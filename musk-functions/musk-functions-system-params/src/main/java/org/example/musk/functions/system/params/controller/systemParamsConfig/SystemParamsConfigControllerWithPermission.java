/*
package org.example.musk.functions.system.params.controller.systemParamsConfig;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.enums.appConfig.SystemDomain;
import org.example.musk.enums.appConfig.AppParamsConfigTypeEnums;
import org.example.musk.framework.permission.enums.OperationTypeEnum;
import org.example.musk.framework.permission.service.DomainPermissionService;
import org.example.musk.functions.system.params.controller.systemParamsConfig.vo.SystemParamsConfigUpdateReqVO;
import org.example.musk.functions.system.params.services.system.params.config.SystemParamsConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

*/
/**
 * 参数配置 Controller（带权限控制）
 *
 * 这是一个示例控制器，展示如何使用领域权限控制
 *
 * @author musk-framework-permission
 *//*

@RestController
@RequestMapping("/api/appParamsConfig-with-permission")
@Validated
@Slf4j
public class SystemParamsConfigControllerWithPermission {

    @Resource
    private SystemParamsConfigService systemParamsConfigService;

    @Resource
    private DomainPermissionService domainPermissionService;

    */
/**
     * 获取系统参数
     *
     * @param type 参数类型
     * @return 参数值
     *//*

    @GetMapping("/get")
    public CommonResult<String> queryAppParamsConfigByTypeValue1(@RequestParam("type") String type) {
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();

        // 检查是否有权限读取此配置项
        if (!domainPermissionService.hasSystemParamPermission(type, OperationTypeEnum.READ.getCode())) {
            return CommonResult.error("没有权限查看此配置项");
        }

        return CommonResult.success(systemParamsConfigService.querySystemParamsConfigByTypeValue1(
            tenantId, SystemDomain.APP, AppParamsConfigTypeEnums.fromType(type)));
    }

    */
/**
     * 更新系统参数
     *
     * @param reqVO 更新请求
     * @return 是否成功
     *//*

    @PutMapping("/update")
    public CommonResult<Boolean> updateAppParamsConfig(@RequestBody @Valid SystemParamsConfigUpdateReqVO reqVO) {
        // 检查是否有权限更新此配置项
        if (!domainPermissionService.hasSystemParamPermission(reqVO.getType(), OperationTypeEnum.UPDATE.getCode())) {
            return CommonResult.error("没有权限修改此配置项");
        }

        // 执行更新操作
        // 注意：这里只是示例，实际需要实现 AppParamsConfigService.updateAppParamsConfig 方法
        // appParamsConfigService.updateAppParamsConfig(reqVO);

        return CommonResult.success(true);
    }
}
*/
