package org.example.musk.plugin.web.upload.controller.uploadFileMemberByDayStatistics.vo;


import lombok.*;

import java.time.LocalDate;

@Data
public class UploadFileMemberByDayStatisticsAddReqVO {

    private Integer memberId;

    private LocalDate statisticsDate;

    private Integer fileType;

    private Long totalFileCount;

    private Double totalFileSize;

}
