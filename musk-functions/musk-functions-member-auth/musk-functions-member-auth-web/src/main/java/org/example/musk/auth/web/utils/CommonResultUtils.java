package org.example.musk.auth.web.utils;

import cn.hutool.core.lang.Assert;
import org.example.musk.common.exception.IBaseErrorInfo;
import org.example.musk.common.pojo.CommonResult;

public class CommonResultUtils {
    public static <T> CommonResult<T> fail(IBaseErrorInfo errorInfo) {
        Assert.notNull(errorInfo, "错误信息不能为空!");
        CommonResult<T> rb = new CommonResult<>();
        rb.setCode(Integer.parseInt(errorInfo.getResultCode()));
        rb.setMsg(errorInfo.getResultMsg());
        rb.setData(null);
        return rb;
    }

    public static <T> CommonResult<T> fail(IBaseErrorInfo errorInfo,String message) {
        Assert.notNull(errorInfo, "错误信息不能为空!");
        CommonResult<T> rb = new CommonResult<>();
        rb.setCode(Integer.parseInt(errorInfo.getResultCode()));
        rb.setMsg(message);
        rb.setData(null);
        return rb;
    }


    public static <T> CommonResult<T> failWithData(IBaseErrorInfo errorInfo,String message,T data) {
        Assert.notNull(errorInfo, "错误信息不能为空!");
        CommonResult<T> rb = new CommonResult<>();
        rb.setCode(Integer.parseInt(errorInfo.getResultCode()));
        rb.setMsg(message);
        rb.setData(data);
        return rb;
    }
}
