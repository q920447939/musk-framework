package org.example.musk.plugin.web.upload.controller.uploadFileMemberByDayStatistics.vo;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
public class UploadFileMemberByDayStatisticsUpdateReqVO {

    @NotNull(message = "编号不能为空")
    private Integer id;

    private Integer memberId;

    private LocalDate statisticsDate;

    private Integer fileType;

    private Long totalFileCount;

    private Double totalFileSize;

}
