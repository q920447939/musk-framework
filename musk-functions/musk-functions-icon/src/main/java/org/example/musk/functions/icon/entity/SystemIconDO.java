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
 * 图标主表 DO
 *
 * @author musk-functions-icon
 */
@TableName("system_icon")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemIconDO extends DomainBaseDO {

    /**
     * 图标ID
     */
    @TableId
    private Integer id;

    /**
     * 图标名称
     */
    private String iconName;

    /**
     * 图标编码（唯一标识）
     */
    private String iconCode;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 图标描述
     */
    private String description;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
