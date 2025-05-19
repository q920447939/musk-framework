package org.example.musk.functions.icon.controller.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新图标分类请求 VO
 *
 * @author musk-functions-icon
 */
@Data
public class SystemIconCategoryUpdateReqVO {

    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空")
    private Integer id;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 100, message = "分类名称长度不能超过100")
    private String categoryName;

    /**
     * 分类编码
     */
    @NotBlank(message = "分类编码不能为空")
    @Size(max = 100, message = "分类编码长度不能超过100")
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
    @NotNull(message = "状态不能为空")
    private Integer status;

    /**
     * 备注
     */
    @Size(max = 255, message = "备注长度不能超过255")
    private String remark;
}
