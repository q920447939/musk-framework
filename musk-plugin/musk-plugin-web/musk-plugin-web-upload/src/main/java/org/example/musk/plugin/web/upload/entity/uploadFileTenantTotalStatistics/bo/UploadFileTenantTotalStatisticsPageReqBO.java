package org.example.musk.plugin.web.upload.entity.uploadFileTenantTotalStatistics.bo;

import java.time.LocalDateTime;
import lombok.*;
import org.example.musk.common.pojo.db.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UploadFileTenantTotalStatisticsPageReqBO extends PageParam {

    private Integer fileType;

    private Long totalFileCount;

    private Double totalFileSize;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] createTime;

}
