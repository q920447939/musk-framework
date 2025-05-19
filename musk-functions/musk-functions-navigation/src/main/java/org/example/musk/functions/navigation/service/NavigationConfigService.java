package org.example.musk.functions.navigation.service;

import org.example.musk.functions.navigation.controller.vo.NavigationTreeVO;
import org.example.musk.functions.navigation.entity.NavigationConfigDO;

import java.util.List;

/**
 * 导航配置 Service 接口
 *
 * @author musk-functions-navigation
 */
public interface NavigationConfigService {

    /**
     * 创建导航配置
     *
     * @param navigationConfig 导航配置
     * @return 导航配置ID
     */
    Integer createNavigationConfig(NavigationConfigDO navigationConfig);

    /**
     * 更新导航配置
     *
     * @param navigationConfig 导航配置
     */
    void updateNavigationConfig(NavigationConfigDO navigationConfig);

    /**
     * 删除导航配置
     *
     * @param id 导航配置ID
     */
    void deleteNavigationConfig(Integer id);

    /**
     * 获取导航配置
     *
     * @param id 导航配置ID
     * @return 导航配置
     */
    NavigationConfigDO getNavigationConfig(Integer id);

    /**
     * 获取指定域下的所有导航配置
     *
     * @param domain 域
     * @return 导航配置列表
     */
    List<NavigationConfigDO> getNavigationConfigsByDomain(Integer domain);

    /**
     * 获取指定租户、域和平台下的所有导航配置
     *
     * @param tenantId 租户ID
     * @param domain 域
     * @param platformType 平台类型
     * @return 导航配置列表
     */
    List<NavigationConfigDO> getNavigationConfigsByTenantAndDomainAndPlatform(
            Integer tenantId, Integer domain, Integer platformType);
    
    /**
     * 获取指定父导航下的子导航
     *
     * @param parentId 父导航ID
     * @return 子导航列表
     */
    List<NavigationConfigDO> getChildNavigations(Integer parentId);
    
    /**
     * 获取导航树
     *
     * @param domain 域
     * @param platformType 平台类型
     * @return 导航树
     */
    List<NavigationTreeVO> getNavigationTree(Integer domain, Integer platformType);
    
    /**
     * 获取指定租户的导航树
     *
     * @param tenantId 租户ID
     * @param domain 域
     * @param platformType 平台类型
     * @return 导航树
     */
    List<NavigationTreeVO> getNavigationTreeByTenant(
            Integer tenantId, Integer domain, Integer platformType);
}
