package org.example.musk.functions.navigation.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.util.commonResult.CommonResultUtils;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.framework.permission.enums.OperationTypeEnum;
import org.example.musk.framework.permission.enums.ResourceTypeEnum;
import org.example.musk.framework.permission.web.anno.RequirePermission;
import org.example.musk.functions.navigation.controller.vo.NavigationConfigCreateReqVO;
import org.example.musk.functions.navigation.controller.vo.NavigationConfigRespVO;
import org.example.musk.functions.navigation.controller.vo.NavigationConfigUpdateReqVO;
import org.example.musk.functions.navigation.controller.vo.NavigationTreeVO;
import org.example.musk.functions.navigation.entity.NavigationConfigDO;
import org.example.musk.functions.navigation.service.NavigationConfigService;
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

/**
 * 导航配置 Controller
 *
 * @author musk-functions-navigation
 */
@RestController
@RequestMapping("/api/navigation")
@Validated
@Slf4j
public class NavigationConfigController {

    @Resource
    private NavigationConfigService navigationConfigService;

    /**
     * 创建导航配置
     *
     * @param createReqVO 创建导航配置请求
     * @return 导航配置ID
     */
    @PostMapping("/create")
    @RequirePermission(resourceType = ResourceTypeEnum.MENU, operationType = OperationTypeEnum.CREATE)
    public CommonResult<Integer> createNavigationConfig(@Valid @RequestBody NavigationConfigCreateReqVO createReqVO) {
        NavigationConfigDO navigationConfig = BeanUtils.toBean(createReqVO, NavigationConfigDO.class);
        // 设置租户ID
        navigationConfig.setTenantId(ThreadLocalTenantContext.getTenantId());
        return CommonResult.success(navigationConfigService.createNavigationConfig(navigationConfig));
    }

    /**
     * 更新导航配置
     *
     * @param updateReqVO 更新导航配置请求
     * @return 是否成功
     */
    @PutMapping("/update")
    @RequirePermission(resourceType = ResourceTypeEnum.MENU, operationType = OperationTypeEnum.UPDATE)
    public CommonResult<Boolean> updateNavigationConfig(@Valid @RequestBody NavigationConfigUpdateReqVO updateReqVO) {
        NavigationConfigDO navigationConfig = BeanUtils.toBean(updateReqVO, NavigationConfigDO.class);
        navigationConfigService.updateNavigationConfig(navigationConfig);
        return CommonResult.success(true);
    }

    /**
     * 删除导航配置
     *
     * @param id 导航配置ID
     * @return 是否成功
     */
    @DeleteMapping("/delete")
    @RequirePermission(resourceType = ResourceTypeEnum.MENU, operationType = OperationTypeEnum.DELETE)
    public CommonResult<Boolean> deleteNavigationConfig(@RequestParam("id") Integer id) {
        navigationConfigService.deleteNavigationConfig(id);
        return CommonResult.success(true);
    }

    /**
     * 获取导航配置
     *
     * @param id 导航配置ID
     * @return 导航配置
     */
    @GetMapping("/get")
    @RequirePermission(resourceType = ResourceTypeEnum.MENU, operationType = OperationTypeEnum.READ)
    public CommonResult<NavigationConfigRespVO> getNavigationConfig(@RequestParam("id") Integer id) {
        NavigationConfigDO navigationConfig = navigationConfigService.getNavigationConfig(id);
        return CommonResultUtils.wrapEmptyObjResult(navigationConfig, 
                () -> BeanUtils.toBean(navigationConfig, NavigationConfigRespVO.class));
    }

    /**
     * 获取指定域和平台下的所有导航配置
     *
     * @param domain 域
     * @param platformType 平台类型
     * @return 导航配置列表
     */
    @GetMapping("/list-by-domain-platform")
    @RequirePermission(resourceType = ResourceTypeEnum.MENU, operationType = OperationTypeEnum.READ)
    public CommonResult<List<NavigationConfigRespVO>> getNavigationConfigsByDomainAndPlatform(
            @RequestParam("domain") Integer domain,
            @RequestParam("platformType") Integer platformType) {
        List<NavigationConfigDO> navigations = navigationConfigService.getNavigationConfigsByTenantAndDomainAndPlatform(
                null, domain, platformType);
        return CommonResult.success(BeanUtils.toBean(navigations, NavigationConfigRespVO.class));
    }

    /**
     * 获取指定父导航下的子导航
     *
     * @param parentId 父导航ID
     * @return 子导航列表
     */
    @GetMapping("/list-by-parent")
    @RequirePermission(resourceType = ResourceTypeEnum.MENU, operationType = OperationTypeEnum.READ)
    public CommonResult<List<NavigationConfigRespVO>> getChildNavigations(@RequestParam("parentId") Integer parentId) {
        List<NavigationConfigDO> navigations = navigationConfigService.getChildNavigations(parentId);
        return CommonResult.success(BeanUtils.toBean(navigations, NavigationConfigRespVO.class));
    }

    /**
     * 获取导航树
     *
     * @param domain 域
     * @param platformType 平台类型
     * @return 导航树
     */
    @GetMapping("/tree")
    @RequirePermission(resourceType = ResourceTypeEnum.MENU, operationType = OperationTypeEnum.READ)
    public CommonResult<List<NavigationTreeVO>> getNavigationTree(
            @RequestParam("domain") Integer domain,
            @RequestParam("platformType") Integer platformType) {
        return CommonResult.success(navigationConfigService.getNavigationTree(domain, platformType));
    }

    /**
     * 获取当前租户的导航树
     *
     * @param domain 域
     * @param platformType 平台类型
     * @return 导航树
     */
    @GetMapping("/tree/current-tenant")
    public CommonResult<List<NavigationTreeVO>> getCurrentTenantNavigationTree(
            @RequestParam("domain") Integer domain,
            @RequestParam("platformType") Integer platformType) {
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        return CommonResult.success(navigationConfigService.getNavigationTreeByTenant(tenantId, domain, platformType));
    }
}
