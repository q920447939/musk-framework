package org.example.musk.functions.resource.vo;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 资源分类更新请求VO
 *
 * @author musk-functions-resource
 */
@Data
public class ResourceCategoryUpdateReqVO {

    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空")
    private Integer id;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
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
}
