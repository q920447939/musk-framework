package org.example.musk.functions.icon.entity;

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
 * 图标资源表 DO
 *
 * @author musk-functions-icon
 */
@TableName("system_icon_resource")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemIconResourceDO extends DomainBaseDO {

    /**
     * 资源ID
     */
    @TableId
    private Integer id;

    /**
     * 图标ID
     */
    private Integer iconId;



    /**
     * 资源类型（1:URL 2:Base64 3:字体图标）
     */
    private Integer resourceType;

    /**
     * 资源URL
     */
    private String resourceUrl;


    /**
     * 宽度（像素）
     */
    private Integer width;

    /**
     * 高度（像素）
     */
    private Integer height;

    /**
     * 版本号
     */
    private String version;

    /**
     * 是否默认资源
     */
    private Boolean isDefault;
}
