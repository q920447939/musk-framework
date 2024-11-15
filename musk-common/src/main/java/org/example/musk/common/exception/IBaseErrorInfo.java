package org.example.musk.common.exception;

/**
 * * 基础错误信息接口
 *
 * @author 王国初
 * @ClassName: IBaseErrorInfo
 * @Desciption TODO
 * @date 2020年3月9日
 * @Version V1.0
 */
public interface IBaseErrorInfo {
    /**
     * * 获取错误码
     *
     * @return
     */
    String getResultCode();

    /**
     * * 获取错误描述
     *
     * @return
     */
    String getResultMsg();
}
