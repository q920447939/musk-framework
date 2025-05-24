package org.example.musk.functions.resource.vo;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 资源分类创建请求VO
 *
 * @author musk-functions-resource
 */
@Data
public class ResourceCategoryCreateReqVO {

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
