package org.example.musk.functions.resource.service;

import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.resource.entity.SystemResourceDO;
import org.example.musk.functions.resource.vo.ResourcePageReqVO;

import java.io.InputStream;
import java.util.List;

/**
 * 资源查询服务接口
 *
 * @author musk-functions-resource
 */
public interface ResourceQueryService {

    /**
     * 获取资源信息
     *
     * @param id 资源ID
     * @return 资源信息
     */
    SystemResourceDO getResource(Integer id);

    /**
     * 获取资源内容
     *
     * @param id 资源ID
     * @return 资源内容
     */
    InputStream getResourceContent(Integer id);

    /**
     * 获取资源访问URL
     *
     * @param id            资源ID
     * @param expireSeconds 过期时间（秒）
     * @return 访问URL
     */
    String getResourceAccessUrl(Integer id, Integer expireSeconds);

    /**
     * 根据分类获取资源列表
     *
     * @param categoryId 分类ID
     * @return 资源列表
     */
    List<SystemResourceDO> getResourcesByCategory(Integer categoryId);

    /**
     * 根据标签获取资源列表
     *
     * @param tag 标签
     * @return 资源列表
     */
    List<SystemResourceDO> getResourcesByTag(String tag);

    /**
     * 根据资源类型获取资源列表
     *
     * @param resourceType 资源类型
     * @return 资源列表
     */
    List<SystemResourceDO> getResourcesByType(Integer resourceType);

    /**
     * 分页查询资源
     *
     * @param pageReqVO 查询条件
     * @return 资源分页结果
     */
    PageResult<SystemResourceDO> pageResources(ResourcePageReqVO pageReqVO);

    /**
     * 搜索资源
     *
     * @param keyword 关键字
     * @return 资源列表
     */
    List<SystemResourceDO> searchResources(String keyword);
}
