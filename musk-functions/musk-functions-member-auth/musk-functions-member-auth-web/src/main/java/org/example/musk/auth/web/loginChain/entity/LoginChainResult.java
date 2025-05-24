package org.example.musk.auth.web.loginChain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.example.musk.common.exception.BusinessPageExceptionEnum;
import org.example.musk.common.exception.IBaseErrorInfo;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.auth.web.utils.CommonResultUtils;

/**
 * 登录链结果
 * ClassName: LoginChainResult
 *
 * @author
 * @Description:
 * @date 2023年07月10日
 */
@Data
@Accessors(chain = true)
@Builder
@Getter
public class LoginChainResult<T> {
    /**
     * 是否处理成功
     */
    private boolean success;
    /**
     * 处理结果
     */
    private CommonResult<T> result;

    /**
     * 成功对象
     */
    private static final LoginChainResult SUCCESS = LoginChainResult.<Object>builder().success(true).build();


    /**
     * 构建失败结果
     *
     * @param errorInfo 错误信息
     * @return {@link LoginChainResult}
     */
    public static LoginChainResult fail(IBaseErrorInfo errorInfo) {
        return LoginChainResult.builder().result(CommonResultUtils.fail(errorInfo)).success(false).build();
    }




    /**
     * 构建失败结果
     *
     * @param errorInfo 错误信息
     * @param message   消息
     * @return {@link LoginChainResult}
     */
    public static LoginChainResult fail(IBaseErrorInfo errorInfo,String message) {
        return LoginChainResult.builder().result(CommonResultUtils.fail(errorInfo,message)).success(false).build();
    }


    /**
     * 构建失败结果
     *
     * @param errorInfo 错误信息
     * @return {@link LoginChainResult}
     */
    public static LoginChainResult fail(IBaseErrorInfo errorInfo,String message,String data) {
        return LoginChainResult.builder().result(CommonResultUtils.failWithData(errorInfo,message,data)).success(false).build();
    }


    /**
     * 构建成功结果
     *
     * @return {@link LoginChainResult}
     */
    public static LoginChainResult succ() {
        return SUCCESS;
    }

    /**
     * 构建成功结果
     *
     * @return {@link LoginChainResult}
     */
    public static <T> LoginChainResult<T> succWithData(T t) {
        CommonResult<T> successed = CommonResult.success(t);
        return (LoginChainResult<T>) LoginChainResult.builder().result((CommonResult<Object>) successed).success(true).build();
    }

}
