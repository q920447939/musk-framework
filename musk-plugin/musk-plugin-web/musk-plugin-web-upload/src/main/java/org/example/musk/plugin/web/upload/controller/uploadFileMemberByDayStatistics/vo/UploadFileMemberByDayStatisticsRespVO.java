package org.example.musk.plugin.web.upload.controller.uploadFileMemberByDayStatistics.vo;


import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Data
public class UploadFileMemberByDayStatisticsRespVO {

    private Integer id;

    private Integer memberId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private LocalDate statisticsDate;

    private Integer fileType;

    private Long totalFileCount;

    private Double totalFileSize;

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private LocalDateTime createTime;

    private String updater;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private LocalDateTime updateTime;

}
