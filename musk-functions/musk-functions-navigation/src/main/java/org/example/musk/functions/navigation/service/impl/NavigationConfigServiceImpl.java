package org.example.musk.functions.navigation.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.enums.navigator.NavigatorStatusEnum;
import org.example.musk.functions.navigation.constant.NavigationConstant;
import org.example.musk.functions.navigation.controller.vo.NavigationTreeVO;
import org.example.musk.functions.navigation.entity.NavigationConfigDO;
import org.example.musk.functions.navigation.exception.NavigationException;
import org.example.musk.functions.navigation.mapper.NavigationConfigMapper;
import org.example.musk.functions.navigation.service.NavigationConfigService;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.cache.core.CacheKeyBuilder;
import org.example.musk.functions.cache.core.CacheManager;
import org.example.musk.functions.cache.model.CacheNamespace;
import org.example.musk.functions.cache.model.CacheOptions;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.example.musk.middleware.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
@DS(DBConstant.SYSTEM)
public class NavigationConfigServiceImpl extends ServiceImpl<NavigationConfigMapper, NavigationConfigDO>
        implements NavigationConfigService {

    @Resource
    private NavigationConfigMapper navigationConfigMapper;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CacheKeyBuilder cacheKeyBuilder;

    @Override
    @CacheEvict(namespace = "NAVIGATION", pattern = "'*:' + #navigationConfig.tenantId + ':' + #navigationConfig.domainId + ':' + #navigationConfig.platformType")
    public Integer createNavigationConfig(NavigationConfigDO navigationConfig) {
        // 校验导航层级和父导航ID的一致性
        validateNavigationLevel(navigationConfig);
        // 保存导航
        save(navigationConfig);
        return navigationConfig.getId();
    }

    @Override
    @CacheEvict(namespace = "NAVIGATION", pattern = "'*:' + #navigationConfig.tenantId + ':' + #navigationConfig.domainId + ':' + #navigationConfig.platformType")
    public void updateNavigationConfig(NavigationConfigDO navigationConfig) {
        // 校验导航存在
        validateNavigationExists(navigationConfig.getId());
        // 校验导航层级和父导航ID的一致性
        validateNavigationLevel(navigationConfig);
        // 更新导航
        updateById(navigationConfig);
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
        String pattern = cacheKeyBuilder.buildPattern(CacheNamespace.NAVIGATION, "*",
                navigation.getTenantId(), navigation.getDomainId(), navigation.getPlatformType());
        cacheManager.removeByPattern(pattern);
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
    @Cacheable(namespace = "NAVIGATION", key = "'list:' + #tenantId + ':' + #domain + ':' + #platformType", expireSeconds = NavigationConstant.NAVIGATION_CACHE_EXPIRE_SECONDS)
    public List<NavigationConfigDO> getNavigationConfigsByTenantAndDomainAndPlatform(
            Integer tenantId, Integer domain, Integer platformType) {
        // 从数据库获取
        return list(new LambdaQueryWrapperX<NavigationConfigDO>()
                .eq(NavigationConfigDO::getTenantId, tenantId)
                .eq(NavigationConfigDO::getDomainId, domain)
                .eq(NavigationConfigDO::getPlatformType, platformType)
                .eq(NavigationConfigDO::getStatus, NavigatorStatusEnum.ENABLE.getCode())
                .orderByAsc(NavigationConfigDO::getDisplayIndex));
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
    @Cacheable(namespace = "NAVIGATION", key = "'tree:' + #tenantId + ':' + #domain + ':' + #platformType", expireSeconds = NavigationConstant.NAVIGATION_CACHE_EXPIRE_SECONDS)
    public List<NavigationTreeVO> getNavigationTreeByTenant(
            Integer tenantId, Integer domain, Integer platformType) {
        // 从数据库获取
        List<NavigationConfigDO> navigations = list(new LambdaQueryWrapperX<NavigationConfigDO>()
                .eq(NavigationConfigDO::getTenantId, tenantId)
                .eq(NavigationConfigDO::getDomainId, domain)
                .eq(NavigationConfigDO::getPlatformType, platformType)
                .eq(NavigationConfigDO::getStatus, NavigatorStatusEnum.ENABLE.getCode())
                .orderByAsc(NavigationConfigDO::getDisplayIndex));

        // 构建导航树
        return buildNavigationTree(navigations, null);
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


}
