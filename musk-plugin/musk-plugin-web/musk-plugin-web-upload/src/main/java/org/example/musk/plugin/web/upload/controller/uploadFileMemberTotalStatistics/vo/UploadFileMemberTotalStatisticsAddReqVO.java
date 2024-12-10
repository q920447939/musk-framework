package org.example.musk.plugin.web.upload.controller.uploadFileMemberTotalStatistics.vo;


import lombok.*;

@Data
public class UploadFileMemberTotalStatisticsAddReqVO {

    private Integer memberId;

    private Integer fileType;

    private Long totalFileCount;

    private Double totalFileSize;

}
