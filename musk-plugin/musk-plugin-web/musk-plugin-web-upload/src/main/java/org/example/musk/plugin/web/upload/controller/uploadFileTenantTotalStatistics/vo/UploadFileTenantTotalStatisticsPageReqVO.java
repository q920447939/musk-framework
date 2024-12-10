package org.example.musk.plugin.web.upload.controller.uploadFileTenantTotalStatistics.vo;


import java.time.LocalDateTime;
import lombok.*;
import org.example.musk.common.pojo.db.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static org.example.musk.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UploadFileTenantTotalStatisticsPageReqVO extends PageParam {

    private Integer fileType;

    private Long totalFileCount;

    private Double totalFileSize;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
