package org.example.musk.functions.resource.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 资源分类树VO
 *
 * @author musk-functions-resource
 */
@Data
public class ResourceCategoryTreeVO {

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
     * 状态（0正常 1禁用）
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
     * 子分类列表
     */
    private List<ResourceCategoryTreeVO> children = new ArrayList<>();
}
