package org.example.musk.functions.icon.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.icon.constant.IconConstant;
import org.example.musk.functions.icon.controller.vo.SystemIconCategoryTreeVO;
import org.example.musk.functions.icon.dao.SystemIconCategoryMapper;
import org.example.musk.functions.icon.entity.SystemIconCategoryDO;
import org.example.musk.functions.icon.entity.SystemIconDO;
import org.example.musk.functions.icon.exception.IconException;
import org.example.musk.functions.icon.service.SystemIconCategoryService;
import org.example.musk.functions.icon.service.SystemIconService;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 图标分类 Service 实现类
 *
 * @author musk-functions-icon
 */
@Service
@Validated
@Slf4j
@DS(DBConstant.SYSTEM)
public class SystemIconCategoryServiceImpl extends ServiceImpl<SystemIconCategoryMapper, SystemIconCategoryDO> implements SystemIconCategoryService {

    @Resource
    private SystemIconService systemIconService;


    @Override
    @CacheEvict(namespace = "ICON", pattern = "category:*", beforeInvocation = true)
    public Integer createCategory(SystemIconCategoryDO category) {
        // 校验分类编码唯一性
        validateCategoryCodeUnique(category);
        // 保存分类
        save(category);
        return category.getId();
    }

    @Override
    @CacheEvict(namespace = "ICON", pattern = "category:*", beforeInvocation = true)
    public void updateCategory(SystemIconCategoryDO category) {
        // 校验分类存在
        validateCategoryExists(category.getId());
        // 校验分类编码唯一性
        validateCategoryCodeUnique(category);
        // 更新分类
        updateById(category);
    }

    @Override
    @CacheEvict(namespace = "ICON", pattern = "category:*", beforeInvocation = true)
    public void deleteCategory(Integer id) {
        // 检查是否有子分类
        if (hasChildCategory(id)) {
            throw new IconException(IconException.CATEGORY_HAS_CHILDREN);
        }
        // 检查是否有图标引用
        if (hasIcons(id)) {
            throw new IconException(IconException.CATEGORY_HAS_ICONS);
        }
        // 删除分类
        removeById(id);
    }

    @Override
    @Cacheable(namespace = "ICON", key = "'category:' + #id", expireSeconds = IconConstant.CATEGORY_CACHE_EXPIRE_SECONDS)
    public SystemIconCategoryDO getCategory(Integer id) {
        return getById(id);
    }

    @Override
    @Cacheable(namespace = "ICON", key = "'category:root:'", expireSeconds = IconConstant.CATEGORY_CACHE_EXPIRE_SECONDS)
    public List<SystemIconCategoryDO> getRootCategories() {
        return list(new LambdaQueryWrapperX<SystemIconCategoryDO>()
                .isNull(SystemIconCategoryDO::getParentId)
                .eq(SystemIconCategoryDO::getStatus, IconConstant.STATUS_NORMAL)
                .orderByAsc(SystemIconCategoryDO::getDisplayOrder));
    }

    @Override
    @Cacheable(namespace = "ICON", key = "'category:children:'+ #parentId", expireSeconds = IconConstant.CATEGORY_CACHE_EXPIRE_SECONDS)
    public List<SystemIconCategoryDO> getChildCategories(Integer parentId) {
        return list(new LambdaQueryWrapperX<SystemIconCategoryDO>()
                .eq(SystemIconCategoryDO::getParentId, parentId)
                .eq(SystemIconCategoryDO::getStatus, IconConstant.STATUS_NORMAL)
                .orderByAsc(SystemIconCategoryDO::getDisplayOrder));
    }

    @Override
    @Cacheable(namespace = "ICON", key = "'category:tree:'", expireSeconds = IconConstant.CATEGORY_CACHE_EXPIRE_SECONDS)
    public List<SystemIconCategoryTreeVO> getCategoryTree() {
        // 获取所有分类
        List<SystemIconCategoryDO> allCategories = list(new LambdaQueryWrapperX<SystemIconCategoryDO>()
                .eq(SystemIconCategoryDO::getStatus, IconConstant.STATUS_NORMAL)
                .orderByAsc(SystemIconCategoryDO::getDisplayOrder));

        // 构建分类树
        return buildCategoryTree(allCategories);
    }

    /**
     * 构建分类树
     */
    private List<SystemIconCategoryTreeVO> buildCategoryTree(List<SystemIconCategoryDO> category) {
        // 转换为VO
        List<SystemIconCategoryTreeVO> categoryVOs = BeanUtils.toBean(category, SystemIconCategoryTreeVO.class);

        // 按父ID分组
        Map<Integer, List<SystemIconCategoryTreeVO>> parentIdMap = categoryVOs.stream()
                .collect(Collectors.groupingBy(vo -> vo.getParentId() == null ? 0 : vo.getParentId()));

        // 设置子分类
        categoryVOs.forEach(vo -> vo.setChildren(parentIdMap.getOrDefault(vo.getId(), new ArrayList<>())));

        // 返回顶级分类
        return parentIdMap.getOrDefault(0, new ArrayList<>()).stream()
                .sorted(Comparator.comparing(SystemIconCategoryTreeVO::getDisplayOrder))
                .collect(Collectors.toList());
    }

    /**
     * 校验分类编码唯一性
     */
    private void validateCategoryCodeUnique(SystemIconCategoryDO category) {
        SystemIconCategoryDO existingCategory = getOne(new LambdaQueryWrapperX<SystemIconCategoryDO>()
                .eq(SystemIconCategoryDO::getCategoryCode, category.getCategoryCode())
                .eq(SystemIconCategoryDO::getTenantId, category.getTenantId())
                .eq(SystemIconCategoryDO::getDomainId, category.getDomainId()));

        if (existingCategory != null && !existingCategory.getId().equals(category.getId())) {
            throw new IconException(IconException.CATEGORY_CODE_DUPLICATE);
        }
    }

    /**
     * 校验分类存在
     */
    private SystemIconCategoryDO validateCategoryExists(Integer id) {
        SystemIconCategoryDO category = getById(id);
        if (category == null) {
            throw new IconException(IconException.CATEGORY_NOT_EXISTS);
        }
        return category;
    }

    /**
     * 检查是否有子分类
     */
    private boolean hasChildCategory(Integer parentId) {
        return count(new LambdaQueryWrapperX<SystemIconCategoryDO>()
                .eq(SystemIconCategoryDO::getParentId, parentId)) > 0;
    }

    /**
     * 检查分类下是否有图标
     */
    private boolean hasIcons(Integer categoryId) {
        List<SystemIconDO> icons = systemIconService.getIconsByCategory(categoryId);
        return !icons.isEmpty();
    }
}
