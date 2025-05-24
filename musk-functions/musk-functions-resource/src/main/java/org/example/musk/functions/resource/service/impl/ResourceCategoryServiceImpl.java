package org.example.musk.functions.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.resource.constant.ResourceConstant;
import org.example.musk.functions.resource.dao.ResourceCategoryMapper;
import org.example.musk.functions.resource.dao.ResourceMapper;
import org.example.musk.functions.resource.entity.SystemResourceCategoryDO;
import org.example.musk.functions.resource.entity.SystemResourceDO;
import org.example.musk.functions.resource.exception.ResourceException;
import org.example.musk.functions.resource.service.ResourceCategoryService;
import org.example.musk.functions.resource.vo.ResourceCategoryTreeVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 资源分类服务实现
 *
 * @author musk-functions-resource
 */
@Service
@Slf4j
@DS(DBConstant.SYSTEM)
public class ResourceCategoryServiceImpl implements ResourceCategoryService {

    @Resource
    private ResourceCategoryMapper resourceCategoryMapper;

    @Resource
    private ResourceMapper resourceMapper;

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = ResourceConstant.CACHE_NAMESPACE, pattern = "category:*")
    public Integer createCategory(SystemResourceCategoryDO category) {
        // 检查分类编码唯一性
        if (StrUtil.isNotBlank(category.getCategoryCode())) {
            LambdaQueryWrapper<SystemResourceCategoryDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SystemResourceCategoryDO::getCategoryCode, category.getCategoryCode());
            queryWrapper.eq(SystemResourceCategoryDO::getTenantId, ThreadLocalTenantContext.getTenantId());
            queryWrapper.eq(SystemResourceCategoryDO::getDomainId, ThreadLocalTenantContext.getDomainId());
            SystemResourceCategoryDO existCategory = resourceCategoryMapper.selectOne(queryWrapper);
            if (existCategory != null) {
                throw new ResourceException(ResourceException.CATEGORY_CODE_DUPLICATE, category.getCategoryCode());
            }
        }

        // 如果未设置父分类ID，则设置为0（根分类）
        if (category.getParentId() == null) {
            category.setParentId(0);
        }

        // 如果未设置显示顺序，则设置为0
        if (category.getDisplayOrder() == null) {
            category.setDisplayOrder(0);
        }

        // 如果未设置状态，则设置为0（正常）
        if (category.getStatus() == null) {
            category.setStatus(0);
        }

        // 保存分类
        resourceCategoryMapper.insert(category);
        return category.getId();
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = ResourceConstant.CACHE_NAMESPACE, pattern = "category:*")
    public void updateCategory(SystemResourceCategoryDO category) {
        // 获取原分类信息
        SystemResourceCategoryDO oldCategory = getCategory(category.getId());
        if (oldCategory == null) {
            throw new ResourceException(ResourceException.CATEGORY_NOT_FOUND, category.getId());
        }

        // 检查分类编码唯一性
        if (StrUtil.isNotBlank(category.getCategoryCode()) && !category.getCategoryCode().equals(oldCategory.getCategoryCode())) {
            LambdaQueryWrapper<SystemResourceCategoryDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SystemResourceCategoryDO::getCategoryCode, category.getCategoryCode());
            queryWrapper.eq(SystemResourceCategoryDO::getTenantId, ThreadLocalTenantContext.getTenantId());
            queryWrapper.eq(SystemResourceCategoryDO::getDomainId, ThreadLocalTenantContext.getDomainId());
            SystemResourceCategoryDO existCategory = resourceCategoryMapper.selectOne(queryWrapper);
            if (existCategory != null && !existCategory.getId().equals(category.getId())) {
                throw new ResourceException(ResourceException.CATEGORY_CODE_DUPLICATE, category.getCategoryCode());
            }
        }

        // 更新分类
        resourceCategoryMapper.updateById(category);
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = ResourceConstant.CACHE_NAMESPACE, pattern = "category:*")
    public void deleteCategory(Integer id) {
        // 获取分类信息
        SystemResourceCategoryDO category = getCategory(id);
        if (category == null) {
            throw new ResourceException(ResourceException.CATEGORY_NOT_FOUND, id);
        }

        // 检查是否有子分类
        List<SystemResourceCategoryDO> childCategories = listChildCategories(id);
        if (CollUtil.isNotEmpty(childCategories)) {
            throw new ResourceException("category.has.children");
        }

        // 检查是否有关联的资源
        LambdaQueryWrapper<SystemResourceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemResourceDO::getCategoryId, id);
        queryWrapper.eq(SystemResourceDO::getTenantId, ThreadLocalTenantContext.getTenantId());
        queryWrapper.eq(SystemResourceDO::getDomainId, ThreadLocalTenantContext.getDomainId());
        long count = resourceMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ResourceException("category.has.resources");
        }

        // 删除分类
        resourceCategoryMapper.deleteById(id);
    }

    @Override
    @Cacheable(namespace = ResourceConstant.CACHE_NAMESPACE, key = "'category:' + #id", expireSeconds = ResourceConstant.CATEGORY_CACHE_EXPIRE_SECONDS)
    public SystemResourceCategoryDO getCategory(Integer id) {
        return resourceCategoryMapper.selectById(id);
    }

    @Override
    @Cacheable(namespace = ResourceConstant.CACHE_NAMESPACE, key = "'category:list'", expireSeconds = ResourceConstant.CATEGORY_CACHE_EXPIRE_SECONDS)
    public List<SystemResourceCategoryDO> listCategories() {
        LambdaQueryWrapper<SystemResourceCategoryDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemResourceCategoryDO::getTenantId, ThreadLocalTenantContext.getTenantId());
        queryWrapper.eq(SystemResourceCategoryDO::getDomainId, ThreadLocalTenantContext.getDomainId());
        queryWrapper.orderByAsc(SystemResourceCategoryDO::getParentId);
        queryWrapper.orderByAsc(SystemResourceCategoryDO::getDisplayOrder);
        return resourceCategoryMapper.selectList(queryWrapper);
    }

    @Override
    @Cacheable(namespace = ResourceConstant.CACHE_NAMESPACE, key = "'category:children:' + #parentId", expireSeconds = ResourceConstant.CATEGORY_CACHE_EXPIRE_SECONDS)
    public List<SystemResourceCategoryDO> listChildCategories(Integer parentId) {
        LambdaQueryWrapper<SystemResourceCategoryDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemResourceCategoryDO::getParentId, parentId);
        queryWrapper.eq(SystemResourceCategoryDO::getTenantId, ThreadLocalTenantContext.getTenantId());
        queryWrapper.eq(SystemResourceCategoryDO::getDomainId, ThreadLocalTenantContext.getDomainId());
        queryWrapper.orderByAsc(SystemResourceCategoryDO::getDisplayOrder);
        return resourceCategoryMapper.selectList(queryWrapper);
    }

    @Override
    @Cacheable(namespace = ResourceConstant.CACHE_NAMESPACE, key = "'category:tree'", expireSeconds = ResourceConstant.CATEGORY_CACHE_EXPIRE_SECONDS)
    public List<ResourceCategoryTreeVO> getCategoryTree() {
        // 获取所有分类
        List<SystemResourceCategoryDO> allCategories = listCategories();
        if (CollUtil.isEmpty(allCategories)) {
            return new ArrayList<>();
        }

        // 转换为树形结构
        List<ResourceCategoryTreeVO> treeList = new ArrayList<>();
        Map<Integer, List<ResourceCategoryTreeVO>> childrenMap = allCategories.stream()
                .map(category -> {
                    ResourceCategoryTreeVO treeVO = new ResourceCategoryTreeVO();
                    BeanUtil.copyProperties(category, treeVO);
                    return treeVO;
                })
                .collect(Collectors.groupingBy(ResourceCategoryTreeVO::getParentId));

        // 获取根分类
        List<ResourceCategoryTreeVO> rootCategories = childrenMap.getOrDefault(0, new ArrayList<>());
        rootCategories.sort(Comparator.comparing(ResourceCategoryTreeVO::getDisplayOrder));
        treeList.addAll(rootCategories);

        // 递归设置子分类
        for (ResourceCategoryTreeVO treeVO : treeList) {
            setChildren(treeVO, childrenMap);
        }

        return treeList;
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = ResourceConstant.CACHE_NAMESPACE, pattern = "category:*")
    public void updateCategoryStatus(Integer id, Integer status) {
        // 获取分类信息
        SystemResourceCategoryDO category = getCategory(id);
        if (category == null) {
            throw new ResourceException(ResourceException.CATEGORY_NOT_FOUND, id);
        }

        // 更新状态
        SystemResourceCategoryDO updateCategory = new SystemResourceCategoryDO();
        updateCategory.setId(id);
        updateCategory.setStatus(status);
        resourceCategoryMapper.updateById(updateCategory);
    }

    /**
     * 递归设置子分类
     *
     * @param parent      父分类
     * @param childrenMap 子分类映射
     */
    private void setChildren(ResourceCategoryTreeVO parent, Map<Integer, List<ResourceCategoryTreeVO>> childrenMap) {
        List<ResourceCategoryTreeVO> children = childrenMap.getOrDefault(parent.getId(), new ArrayList<>());
        if (CollUtil.isNotEmpty(children)) {
            children.sort(Comparator.comparing(ResourceCategoryTreeVO::getDisplayOrder));
            parent.setChildren(children);
            for (ResourceCategoryTreeVO child : children) {
                setChildren(child, childrenMap);
            }
        }
    }
}
