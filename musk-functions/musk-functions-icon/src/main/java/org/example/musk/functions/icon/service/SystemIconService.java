package org.example.musk.functions.icon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.musk.functions.icon.entity.SystemIconDO;

import java.util.List;

/**
 * 图标 Service 接口
 *
 * @author musk-functions-icon
 */
public interface SystemIconService extends IService<SystemIconDO> {

    /**
     * 创建图标
     *
     * @param icon 图标信息
     * @return 图标ID
     */
    Integer createIcon(SystemIconDO icon);

    /**
     * 更新图标
     *
     * @param icon 图标信息
     */
    void updateIcon(SystemIconDO icon);

    /**
     * 删除图标
     *
     * @param id 图标ID
     */
    void deleteIcon(Integer id);

    /**
     * 获取图标
     *
     * @param id 图标ID
     * @return 图标信息
     */
    SystemIconDO getIcon(Integer id);

    /**
     * 根据编码获取图标
     *
     * @param iconCode 图标编码
     * @return 图标信息
     */
    SystemIconDO getIconByCode(String iconCode);

    /**
     * 获取分类下的图标列表
     *
     * @param categoryId 分类ID
     * @return 图标列表
     */
    List<SystemIconDO> getIconsByCategory(Integer categoryId);

    /**
     * 搜索图标
     *
     * @param keyword 关键词
     * @return 图标列表
     */
    List<SystemIconDO> searchIcons(String keyword);
}
