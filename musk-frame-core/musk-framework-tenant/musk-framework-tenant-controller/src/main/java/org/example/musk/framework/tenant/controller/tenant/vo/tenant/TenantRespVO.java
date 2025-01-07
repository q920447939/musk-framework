package org.example.musk.framework.tenant.controller.tenant.vo.tenant;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TenantRespVO {

    private Long id;

    private String name;

    private String contactName;

    private String contactMobile;

    private Integer status;

    private String website;

    private Long packageId;

    private LocalDateTime expireTime;

    private Integer accountCount;

    private LocalDateTime createTime;

}
