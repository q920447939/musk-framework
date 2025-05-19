package org.example.musk.functions.system.params.controller.systemParamsConfig.vo;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import lombok.*;

@Data
public class SystemParamsConfigRespVO {

    private Integer id;

    private Integer bSystem;

    private String bGroup;

    private String type;

    private String typeDesc;

    private String value1;

    private String value2;

    private String value3;

    private String remark;

    private Integer status;

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private LocalDateTime createTime;

    private String updater;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private LocalDateTime updateTime;

}
