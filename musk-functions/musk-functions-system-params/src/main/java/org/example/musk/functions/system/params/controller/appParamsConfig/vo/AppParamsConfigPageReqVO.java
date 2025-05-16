package org.example.musk.functions.system.params.controller.appParamsConfig.vo;


import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;
import org.example.musk.common.pojo.db.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import static org.example.musk.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppParamsConfigPageReqVO extends PageParam {

    private Integer bSystem;

    private String bGroup;

    private String type;

    private String typeDesc;

    private String value1;

    private String value2;

    private String value3;

    private String remark;

    private Integer status;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}