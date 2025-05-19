package org.example.musk.functions.menu.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.framework.permission.enums.OperationTypeEnum;
import org.example.musk.framework.permission.enums.ResourceTypeEnum;
import org.example.musk.framework.permission.service.DomainPermissionService;
import org.example.musk.framework.permission.web.anno.RequirePermission;
import org.example.musk.functions.menu.controller.vo.MenuCreateReqVO;
import org.example.musk.functions.menu.controller.vo.MenuRespVO;
import org.example.musk.functions.menu.controller.vo.MenuTreeVO;
import org.example.musk.functions.menu.controller.vo.MenuUpdateReqVO;
import org.example.musk.functions.menu.entity.MenuDO;
import org.example.musk.functions.menu.service.MenuService;
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
 * 菜单 Controller（带权限控制）
 *
 * 这是一个示例控制器，展示如何使用领域权限控制
 *
 * @author musk-framework-permission
 */
@RestController
@RequestMapping("/api/menu-with-permission")
@Validated
@Slf4j
public class MenuControllerWithPermission {

    @Resource
    private MenuService menuService;

    @Resource
    private DomainPermissionService domainPermissionService;

    /**
     * 创建菜单
     *
     * 使用注解方式进行权限控制
     *
     * @param createReqVO 创建信息
     * @return 菜单编号
     */
    @PostMapping("/create")
    @RequirePermission(resourceType = ResourceTypeEnum.MENU, operationType = OperationTypeEnum.CREATE)
    public CommonResult<Long> createMenu(@Valid @RequestBody MenuCreateReqVO createReqVO) {
        MenuDO menu = BeanUtils.toBean(createReqVO, MenuDO.class);
        return CommonResult.success(menuService.createMenu(menu));
    }

    /**
     * 更新菜单
     *
     * 使用注解方式进行权限控制，并从方法参数中获取资源ID
     *
     * @param updateReqVO 更新信息
     * @return 是否成功
     */
    @PutMapping("/update")
    @RequirePermission(resourceType = ResourceTypeEnum.MENU, resourceIdParam = "id", operationType = OperationTypeEnum.UPDATE)
    public CommonResult<Boolean> updateMenu(@Valid @RequestBody MenuUpdateReqVO updateReqVO) {
        MenuDO menu = BeanUtils.toBean(updateReqVO, MenuDO.class);
        menuService.updateMenu(menu);
        return CommonResult.success(true);
    }

    /**
     * 删除菜单
     *
     * 使用服务方式进行权限控制
     *
     * @param id 菜单编号
     * @return 是否成功
     */
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteMenu(@RequestParam("id") Long id) {
        // 手动检查权限
        if (!domainPermissionService.hasPermission(ResourceTypeEnum.MENU, id.toString(), OperationTypeEnum.DELETE)) {
            return CommonResult.error(400,"没有删除此菜单的权限");
        }

        menuService.deleteMenu(id);
        return CommonResult.success(true);
    }

    /**
     * 获取菜单
     *
     * 使用注解方式进行权限控制
     *
     * @param id 菜单编号
     * @return 菜单
     */
    @GetMapping("/get")
    @RequirePermission(resourceType = ResourceTypeEnum.MENU, resourceIdParam = "id", operationType = OperationTypeEnum.READ)
    public CommonResult<MenuRespVO> getMenu(@RequestParam("id") Long id) {
        MenuDO menu = menuService.getMenu(id);
        return CommonResult.success(BeanUtils.toBean(menu, MenuRespVO.class));
    }

    /**
     * 获取菜单树
     *
     * 使用注解方式进行权限控制
     *
     * @param domain 域
     * @return 菜单树
     */
    @GetMapping("/tree")
    @RequirePermission(resourceType = ResourceTypeEnum.MENU, operationType = OperationTypeEnum.READ)
    public CommonResult<List<MenuTreeVO>> getMenuTree(@RequestParam("domain") Integer domain) {
        return CommonResult.success(menuService.getMenuTree(domain));
    }

    /**
     * 获取当前租户的菜单树
     *
     * 使用注解方式进行权限控制
     *
     * @param domain 域
     * @return 菜单树
     */
    @GetMapping("/tree/current-tenant")
    @RequirePermission(resourceType = ResourceTypeEnum.MENU, operationType = OperationTypeEnum.READ)
    public CommonResult<List<MenuTreeVO>> getCurrentTenantMenuTree(@RequestParam("domain") Integer domain) {
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        return CommonResult.success(menuService.getMenuTreeByTenant(tenantId, domain));
    }
}
