package org.example.musk.functions.navigation.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.musk.common.pojo.db.DomainBaseDO;

/**
 * 导航配置表 DO
 *
 * @author musk-functions-navigation
 */
@TableName("app_navigation_config")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NavigationConfigDO extends DomainBaseDO {

    /**
     * ID
     */
    @TableId
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
}
