package org.example.musk.plugin.web.upload.controller.uploadFileMemberTotalStatistics.vo;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Data
public class UploadFileMemberTotalStatisticsRespVO {

    private Integer id;

    private Integer memberId;

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
