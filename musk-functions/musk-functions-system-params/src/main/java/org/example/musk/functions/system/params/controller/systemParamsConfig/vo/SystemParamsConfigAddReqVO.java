package org.example.musk.functions.system.params.controller.systemParamsConfig.vo;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class SystemParamsConfigAddReqVO {

    @NotNull(message = "系统(1:web)不能为空")
    private Integer bSystem;

    @NotBlank(message = "业务所属组编码不能为空")
    private String bGroup;

    @NotBlank(message = "参数类型编码不能为空")
    private String type;

    private String typeDesc;

    private String value1;

    private String value2;

    private String value3;

    private String remark;

    @NotNull(message = "状态(0:失效，1:生效)不能为空")
    private Integer status;

}
