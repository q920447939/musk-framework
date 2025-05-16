package org.example.musk.functions.system.params.controller.appParamsConfig.vo;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class AppParamsConfigUpdateReqVO {

    @NotNull(message = "编号不能为空")
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

}