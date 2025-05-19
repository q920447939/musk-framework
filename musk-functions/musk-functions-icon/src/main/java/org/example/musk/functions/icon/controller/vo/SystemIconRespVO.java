package org.example.musk.functions.icon.controller.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图标响应 VO
 *
 * @author musk-functions-icon
 */
@Data
public class SystemIconRespVO {

    /**
     * 图标ID
     */
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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
