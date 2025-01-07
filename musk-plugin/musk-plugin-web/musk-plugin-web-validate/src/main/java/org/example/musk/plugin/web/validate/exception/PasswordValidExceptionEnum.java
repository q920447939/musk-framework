package org.example.musk.plugin.web.validate.exception;/**
 * @Project:
 * @Author:
 * @Date: 2021年09月14日
 */

import cn.hutool.core.lang.Assert;
import lombok.Getter;
import org.example.musk.common.exception.IBaseErrorInfo;

import java.util.Arrays;
import java.util.Optional;

/**
 * ClassName: BusinessPageExceptionEnum
 *
 * @author
 * @Description:  页面业务异常，可在页面上展示的异常
 * 开始编码 4100000
 * 结束编码 4200000
 * @date 2021年09月14日
 */
@Getter
public enum PasswordValidExceptionEnum implements IBaseErrorInfo {

    PASSWORD_IS_EMPTY_ERROR("4111100", "密码不能为空"),
    PASSWORD_IS_SIMPLE_WORD_ERROR("4111103", "密码不能为弱密码"),
    PASSWORD_IS_CONTAIN_SPECIAL_CHAR_ERROR("4111104", "密码不能包含特殊字符"),

    END("4200000", "");
    private String exCode;
    private String exDesc;

    PasswordValidExceptionEnum(String exCode, String exDesc) {
        this.exCode = exCode;
        this.exDesc = exDesc;
    }


    @Override
    public String getResultCode() {
        return this.exCode;
    }

    @Override
    public String getResultMsg() {
        return this.getExDesc();
    }

    public static PasswordValidExceptionEnum getBusinessPageExceptionEnumByCode(String code){
        Assert.notNull(code);
        return Optional.ofNullable(Arrays.stream(PasswordValidExceptionEnum.values()).filter(k->k.getExCode().equals(code)).findFirst().get()).orElse(null);
    }
}
