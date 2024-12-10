package org.example.musk.plugin.web.upload.entity.uploadFileMemberByDayStatistics.bo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import org.example.musk.common.pojo.db.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UploadFileMemberByDayStatisticsPageReqBO extends PageParam {

    private Integer memberId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate[] statisticsDate;

    private Integer fileType;

    private Long totalFileCount;

    private Double totalFileSize;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] createTime;

}
