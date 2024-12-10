package org.example.musk.plugin.web.upload.entity.uploadFileMemberTotalStatistics.bo;

import java.time.LocalDateTime;
import lombok.*;
import org.example.musk.common.pojo.db.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UploadFileMemberTotalStatisticsPageReqBO extends PageParam {

    private Integer memberId;

    private Integer fileType;

    private Long totalFileCount;

    private Double totalFileSize;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] createTime;

}
