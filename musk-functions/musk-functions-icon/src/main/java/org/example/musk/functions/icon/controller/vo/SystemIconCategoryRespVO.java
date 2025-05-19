package org.example.musk.functions.icon.controller.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图标分类响应 VO
 *
 * @author musk-functions-icon
 */
@Data
public class SystemIconCategoryRespVO {

    /**
     * 分类ID
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类编码
     */
    private String categoryCode;

    /**
     * 父分类ID
     */
    private Integer parentId;

    /**
     * 显示顺序
     */
    private Integer displayOrder;

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
