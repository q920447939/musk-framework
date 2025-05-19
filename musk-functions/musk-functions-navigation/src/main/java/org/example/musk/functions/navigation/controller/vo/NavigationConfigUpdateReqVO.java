package org.example.musk.functions.navigation.controller.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 导航配置更新请求 VO
 *
 * @author musk-functions-navigation
 */
@Data
public class NavigationConfigUpdateReqVO {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空")
    private Integer id;

    /**
     * 导航名称
     */
    @NotBlank(message = "导航名称不能为空")
    private String navigatorName;

    /**
     * 导航图标ID
     */
    private Integer navigatorIconId;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    private Short displayIndex;

    /**
     * 目标地址
     */
    @NotBlank(message = "目标地址不能为空")
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
    @NotNull(message = "状态不能为空")
    private Integer status;
    
    /**
     * 平台类型（1:APP 2:WEB）
     */
    @NotNull(message = "平台类型不能为空")
    private Integer platformType;
    
    /**
     * 导航层级(0表示首层)
     */
    @NotNull(message = "导航层级不能为空")
    private Integer navigatorLevel;
    
    /**
     * 父导航ID(如果层级为0,那么父导航ID为null)
     */
    private Integer parentNavigatorId;
    
    /**
     * 所属域(1:APP)
     */
    @NotNull(message = "所属域不能为空")
    private Integer domainId;
}
