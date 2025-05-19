package org.example.musk.functions.navigation.controller.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 导航配置响应 VO
 *
 * @author musk-functions-navigation
 */
@Data
public class NavigationConfigRespVO {

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
     * 备注
     */
    private String remark;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;
    
    /**
     * 平台类型（1:APP 2:WEB）
     */
    private Integer platformType;
    
    /**
     * 导航层级(0表示首层)
     */
    private Integer navigatorLevel;
    
    /**
     * 父导航ID(如果层级为0,那么父导航ID为null)
     */
    private Integer parentNavigatorId;
    
    /**
     * 所属域(1:APP)
     */
    private Integer domainId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
