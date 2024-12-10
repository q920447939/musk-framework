package org.example.musk.plugin.web.upload.controller.uploadFileTenantTotalStatistics.vo;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class UploadFileTenantTotalStatisticsUpdateReqVO {

    @NotNull(message = "编号不能为空")
    private Integer id;

    private Integer fileType;

    private Long totalFileCount;

    private Double totalFileSize;

}
