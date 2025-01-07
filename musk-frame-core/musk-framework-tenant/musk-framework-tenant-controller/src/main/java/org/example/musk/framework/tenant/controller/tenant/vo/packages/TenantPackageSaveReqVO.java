package org.example.musk.framework.tenant.controller.tenant.vo.packages;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.musk.common.enums.tenant.CommonStatusEnum;

import java.util.Set;

@Data
public class TenantPackageSaveReqVO {

    private Long id;

    @NotEmpty(message = "套餐名不能为空")
    private String name;

    @NotNull(message = "状态不能为空")
    private Integer status;

    private String remark;

    @NotNull(message = "关联的菜单编号不能为空")
    private Set<Long> menuIds;

}
