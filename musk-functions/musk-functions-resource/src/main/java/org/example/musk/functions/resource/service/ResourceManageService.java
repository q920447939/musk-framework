package org.example.musk.functions.resource.service;

import org.example.musk.functions.resource.entity.SystemResourceDO;

/**
 * 资源管理服务接口
 *
 * @author musk-functions-resource
 */
public interface ResourceManageService {

    /**
     * 更新资源信息
     *
     * @param resource 资源信息
     */
    void updateResource(SystemResourceDO resource);

    /**
     * 删除资源
     *
     * @param id 资源ID
     */
    void deleteResource(Integer id);

    /**
     * 批量删除资源
     *
     * @param ids 资源ID数组
     */
    void batchDeleteResources(Integer[] ids);

    /**
     * 更新资源状态
     *
     * @param id     资源ID
     * @param status 状态
     */
    void updateResourceStatus(Integer id, Integer status);

    /**
     * 更新资源分类
     *
     * @param id         资源ID
     * @param categoryId 分类ID
     */
    void updateResourceCategory(Integer id, Integer categoryId);

    /**
     * 更新资源标签
     *
     * @param id   资源ID
     * @param tags 标签
     */
    void updateResourceTags(Integer id, String tags);
}
