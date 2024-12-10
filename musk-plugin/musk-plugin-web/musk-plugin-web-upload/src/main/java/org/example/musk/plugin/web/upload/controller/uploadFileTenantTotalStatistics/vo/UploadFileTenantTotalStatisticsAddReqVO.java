package org.example.musk.plugin.web.upload.controller.uploadFileTenantTotalStatistics.vo;


import lombok.*;

@Data
public class UploadFileTenantTotalStatisticsAddReqVO {

    private Integer fileType;

    private Long totalFileCount;

    private Double totalFileSize;

}
