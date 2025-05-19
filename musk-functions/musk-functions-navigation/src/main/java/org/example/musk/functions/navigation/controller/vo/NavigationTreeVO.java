package org.example.musk.functions.navigation.controller.vo;

import lombok.Data;

import java.util.List;

/**
 * 导航树 VO
 *
 * @author musk-functions-navigation
 */
@Data
public class NavigationTreeVO {

    /**
     * ID
     */
    private Integer id;

    /**
     * 导航名称
     */
    private String navigatorName;

    /**
     * 导航图标ID
     */
    private Integer navigatorIconId;

    /**
     * 显示顺序
     */
    private Short displayIndex;

    /**
     * 目标地址
     */
    private String targetUri;

    /**
     * 地址参数
     */
    private String uriParams;

    /**
     * 平台类型
     */
    private Integer platformType;

    /**
     * 导航层级
     */
    private Integer navigatorLevel;

    /**
     * 父导航ID
     */
    private Integer parentNavigatorId;

    /**
     * 子导航
     */
    private List<NavigationTreeVO> children;
}
