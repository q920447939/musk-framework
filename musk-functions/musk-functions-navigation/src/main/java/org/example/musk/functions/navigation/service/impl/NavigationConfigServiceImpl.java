package org.example.musk.functions.navigation.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.enums.navigator.NavigatorStatusEnum;
import org.example.musk.functions.navigation.constant.NavigationConstant;
import org.example.musk.functions.navigation.controller.vo.NavigationTreeVO;
import org.example.musk.functions.navigation.entity.NavigationConfigDO;
import org.example.musk.functions.navigation.exception.NavigationException;
import org.example.musk.functions.navigation.mapper.NavigationConfigMapper;
import org.example.musk.functions.navigation.service.NavigationConfigService;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.example.musk.middleware.redis.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 导航配置 Service 实现类
 *
 * @author musk-functions-navigation
 */
@Service
@Validated
@Slf4j
@DS(NavigationConstant.DB_NAME)
public class NavigationConfigServiceImpl extends ServiceImpl<NavigationConfigMapper, NavigationConfigDO>
        implements NavigationConfigService {

    @Resource
    private NavigationConfigMapper navigationConfigMapper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public Integer createNavigationConfig(NavigationConfigDO navigationConfig) {
        // 校验导航层级和父导航ID的一致性
        validateNavigationLevel(navigationConfig);
        // 保存导航
        save(navigationConfig);
        // 清除缓存
        clearNavigationCache(navigationConfig.getTenantId(),
                            navigationConfig.getDomainId(),
                            navigationConfig.getPlatformType());
        return navigationConfig.getId();
    }

    @Override
    public void updateNavigationConfig(NavigationConfigDO navigationConfig) {
        // 校验导航存在
        validateNavigationExists(navigationConfig.getId());
        // 校验导航层级和父导航ID的一致性
        validateNavigationLevel(navigationConfig);
        // 更新导航
        updateById(navigationConfig);
        // 清除缓存
        clearNavigationCache(navigationConfig.getTenantId(),
                            navigationConfig.getDomainId(),
                            navigationConfig.getPlatformType());
    }

    @Override
    public void deleteNavigationConfig(Integer id) {
        // 校验导航存在
        NavigationConfigDO navigation = validateNavigationExists(id);
        // 检查是否有子导航
        if (hasChildNavigation(id)) {
            throw new NavigationException(NavigationException.NAVIGATION_HAS_CHILDREN);
        }
        // 删除导航
        removeById(id);
        // 清除缓存
        clearNavigationCache(navigation.getTenantId(),
                            navigation.getDomainId(),
                            navigation.getPlatformType());
    }

    @Override
    public NavigationConfigDO getNavigationConfig(Integer id) {
        return getById(id);
    }

    @Override
    public List<NavigationConfigDO> getNavigationConfigsByDomain(Integer domain) {
        return list(new LambdaQueryWrapperX<NavigationConfigDO>()
                .eq(NavigationConfigDO::getDomainId, domain));
    }

    @Override
    public List<NavigationConfigDO> getNavigationConfigsByTenantAndDomainAndPlatform(
            Integer tenantId, Integer domain, Integer platformType) {
        // 尝试从缓存获取
        String cacheKey = String.format(NavigationConstant.NAVIGATION_CACHE_KEY,
                                       tenantId, domain, platformType);
        @SuppressWarnings("unchecked")
        List<NavigationConfigDO> navigations = (List<NavigationConfigDO>) redisUtil.get(cacheKey);
        if (navigations != null) {
            return navigations;
        }

        // 缓存未命中，从数据库获取
        navigations = list(new LambdaQueryWrapperX<NavigationConfigDO>()
                .eq(NavigationConfigDO::getTenantId, tenantId)
                .eq(NavigationConfigDO::getDomainId, domain)
                .eq(NavigationConfigDO::getPlatformType, platformType)
                .eq(NavigationConfigDO::getStatus, NavigatorStatusEnum.ENABLE.getCode())
                .orderByAsc(NavigationConfigDO::getDisplayIndex));

        // 存入缓存
        redisUtil.set(cacheKey, navigations, NavigationConstant.NAVIGATION_CACHE_EXPIRE_SECONDS);

        return navigations;
    }

    @Override
    public List<NavigationConfigDO> getChildNavigations(Integer parentId) {
        return list(new LambdaQueryWrapperX<NavigationConfigDO>()
                .eq(NavigationConfigDO::getParentNavigatorId, parentId)
                .eq(NavigationConfigDO::getStatus, NavigatorStatusEnum.ENABLE.getCode())
                .orderByAsc(NavigationConfigDO::getDisplayIndex));
    }

    @Override
    public List<NavigationTreeVO> getNavigationTree(Integer domain, Integer platformType) {
        // 获取所有导航
        List<NavigationConfigDO> navigations = list(new LambdaQueryWrapperX<NavigationConfigDO>()
                .eq(NavigationConfigDO::getDomainId, domain)
                .eq(NavigationConfigDO::getPlatformType, platformType)
                .eq(NavigationConfigDO::getStatus, NavigatorStatusEnum.ENABLE.getCode())
                .orderByAsc(NavigationConfigDO::getDisplayIndex));

        // 构建导航树
        return buildNavigationTree(navigations, null);
    }

    @Override
    public List<NavigationTreeVO> getNavigationTreeByTenant(
            Integer tenantId, Integer domain, Integer platformType) {
        // 尝试从缓存获取
        String cacheKey = String.format(NavigationConstant.NAVIGATION_TREE_CACHE_KEY,
                                       tenantId, domain, platformType);
        @SuppressWarnings("unchecked")
        List<NavigationTreeVO> navigationTree = (List<NavigationTreeVO>) redisUtil.get(cacheKey);
        if (navigationTree != null) {
            return navigationTree;
        }

        // 缓存未命中，从数据库获取
        List<NavigationConfigDO> navigations = list(new LambdaQueryWrapperX<NavigationConfigDO>()
                .eq(NavigationConfigDO::getTenantId, tenantId)
                .eq(NavigationConfigDO::getDomainId, domain)
                .eq(NavigationConfigDO::getPlatformType, platformType)
                .eq(NavigationConfigDO::getStatus, NavigatorStatusEnum.ENABLE.getCode())
                .orderByAsc(NavigationConfigDO::getDisplayIndex));

        // 构建导航树
        navigationTree = buildNavigationTree(navigations, null);

        // 存入缓存
        redisUtil.set(cacheKey, navigationTree, NavigationConstant.NAVIGATION_CACHE_EXPIRE_SECONDS);

        return navigationTree;
    }

    /**
     * 构建导航树
     *
     * @param navigations 导航列表
     * @param parentId 父导航ID
     * @return 导航树
     */
    private List<NavigationTreeVO> buildNavigationTree(List<NavigationConfigDO> navigations, Integer parentId) {
        return navigations.stream()
                .filter(nav -> (parentId == null && nav.getNavigatorLevel() == NavigationConstant.ROOT_NAVIGATION_LEVEL) ||
                        (parentId != null && parentId.equals(nav.getParentNavigatorId())))
                .map(nav -> {
                    NavigationTreeVO treeNode = BeanUtils.toBean(nav, NavigationTreeVO.class);
                    // 递归获取子导航
                    List<NavigationTreeVO> children = buildNavigationTree(navigations, nav.getId());
                    treeNode.setChildren(children);
                    return treeNode;
                })
                .collect(Collectors.toList());
    }

    /**
     * 校验导航层级和父导航ID的一致性
     */
    private void validateNavigationLevel(NavigationConfigDO navigation) {
        if (NavigationConstant.ROOT_NAVIGATION_LEVEL.equals(navigation.getNavigatorLevel())) {
            // 根导航的父导航ID必须为空
            if (navigation.getParentNavigatorId() != null) {
                throw new NavigationException(NavigationException.INVALID_PARENT_ID);
            }
        } else {
            // 非根导航的父导航ID不能为空
            if (navigation.getParentNavigatorId() == null) {
                throw new NavigationException(NavigationException.PARENT_ID_REQUIRED);
            }
            // 检查父导航是否存在
            NavigationConfigDO parentNav = getById(navigation.getParentNavigatorId());
            if (parentNav == null) {
                throw new NavigationException(NavigationException.PARENT_NOT_EXISTS);
            }
            // 检查父导航的层级是否正确
            if (parentNav.getNavigatorLevel() != navigation.getNavigatorLevel() - 1) {
                throw new NavigationException(NavigationException.INVALID_LEVEL);
            }
        }
    }

    /**
     * 校验导航是否存在
     */
    private NavigationConfigDO validateNavigationExists(Integer id) {
        NavigationConfigDO navigation = getById(id);
        if (navigation == null) {
            throw new NavigationException(NavigationException.NAVIGATION_NOT_EXISTS);
        }
        return navigation;
    }

    /**
     * 检查是否有子导航
     */
    private boolean hasChildNavigation(Integer id) {
        return count(new LambdaQueryWrapperX<NavigationConfigDO>()
                .eq(NavigationConfigDO::getParentNavigatorId, id)) > 0;
    }

    /**
     * 清除导航缓存
     */
    private void clearNavigationCache(Integer tenantId, Integer domain, Integer platformType) {
        String cacheKey = String.format(NavigationConstant.NAVIGATION_CACHE_KEY,
                                       tenantId, domain, platformType);
        String treeCacheKey = String.format(NavigationConstant.NAVIGATION_TREE_CACHE_KEY,
                                          tenantId, domain, platformType);
        redisUtil.del(cacheKey);
        redisUtil.del(treeCacheKey);
    }
}
