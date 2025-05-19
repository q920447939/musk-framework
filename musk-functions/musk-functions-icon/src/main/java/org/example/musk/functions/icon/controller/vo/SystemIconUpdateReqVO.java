package org.example.musk.functions.icon.controller.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新图标请求 VO
 *
 * @author musk-functions-icon
 */
@Data
public class SystemIconUpdateReqVO {

    /**
     * 图标ID
     */
    @NotNull(message = "图标ID不能为空")
    private Integer id;

    /**
     * 图标名称
     */
    @NotBlank(message = "图标名称不能为空")
    @Size(max = 100, message = "图标名称长度不能超过100")
    private String iconName;

    /**
     * 图标编码（唯一标识）
     */
    @NotBlank(message = "图标编码不能为空")
    @Size(max = 100, message = "图标编码长度不能超过100")
    private String iconCode;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 图标描述
     */
    @Size(max = 255, message = "图标描述长度不能超过255")
    private String description;

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
