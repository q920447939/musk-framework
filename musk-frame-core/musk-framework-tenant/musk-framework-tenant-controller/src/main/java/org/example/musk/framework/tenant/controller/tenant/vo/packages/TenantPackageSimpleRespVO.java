package org.example.musk.framework.tenant.controller.tenant.vo.packages;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class TenantPackageSimpleRespVO {

    @NotNull(message = "套餐编号不能为空")
    private Long id;

    @NotNull(message = "套餐名不能为空")
    private String name;

}
