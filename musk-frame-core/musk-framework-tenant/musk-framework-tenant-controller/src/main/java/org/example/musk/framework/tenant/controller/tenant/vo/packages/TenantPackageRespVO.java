package org.example.musk.framework.tenant.controller.tenant.vo.packages;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class TenantPackageRespVO {

    private Long id;

    private String name;

    private Integer status;

    private String remark;


    private LocalDateTime createTime;

}
