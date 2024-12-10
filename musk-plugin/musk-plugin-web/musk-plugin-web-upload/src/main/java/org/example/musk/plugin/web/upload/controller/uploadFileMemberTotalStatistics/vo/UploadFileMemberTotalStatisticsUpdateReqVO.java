package org.example.musk.plugin.web.upload.controller.uploadFileMemberTotalStatistics.vo;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class UploadFileMemberTotalStatisticsUpdateReqVO {

    @NotNull(message = "编号不能为空")
    private Integer id;

    private Integer memberId;

    private Integer fileType;

    private Long totalFileCount;

    private Double totalFileSize;

}
