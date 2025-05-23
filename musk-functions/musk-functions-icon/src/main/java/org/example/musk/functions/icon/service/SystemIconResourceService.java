package org.example.musk.functions.icon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.musk.functions.icon.entity.SystemIconResourceDO;

import java.util.List;

/**
 * 图标资源 Service 接口
 *
 * @author musk-functions-icon
 */
public interface SystemIconResourceService extends IService<SystemIconResourceDO> {

    /**
     * 创建图标资源
     *
     * @param resource 图标资源信息
     * @return 资源ID
     */
    Integer createIconResource(SystemIconResourceDO resource);

    /**
     * 更新图标资源
     *
     * @param resource 图标资源信息
     */
    void updateIconResource(SystemIconResourceDO resource);

    /**
     * 删除图标资源
     *
     * @param id 资源ID
     */
    void deleteIconResource(Integer id);

    /**
     * 获取图标资源
     *
     * @param id 资源ID
     * @return 图标资源信息
     */
    SystemIconResourceDO getIconResource(Integer id);

    /**
     * 获取图标的所有资源
     *
     * @param iconId 图标ID
     * @return 图标资源列表
     */
    List<SystemIconResourceDO> getResourcesByIconId(Integer iconId);

    /**
     * 获取图标的默认资源
     *
     * @param iconId 图标ID
     * @return 默认图标资源
     */
    SystemIconResourceDO getDefaultResource(Integer iconId);
}
