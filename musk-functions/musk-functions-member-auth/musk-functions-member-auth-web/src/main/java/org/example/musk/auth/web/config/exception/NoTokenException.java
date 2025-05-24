package org.example.musk.auth.web.config.exception;


import org.example.musk.common.exception.IBaseErrorInfo;

/**
 * * 自定义的业务异常
 *
 * @author 王国初
 * @ClassName: BusinessException
 * @Desciption TODO
 * @date 2020年3月9日
 * @Version V1.0
 */
public class NoTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * * 错误码
     */
    protected String errorCode;
    /**
     * * 错误信息
     */
    protected String errorMsg;

    public NoTokenException() {
        super();
    }

    public NoTokenException(IBaseErrorInfo errorInfo) {
        super(errorInfo.getResultCode());
        this.errorCode = errorInfo.getResultCode();
        this.errorMsg = errorInfo.getResultMsg();
    }

    public NoTokenException(IBaseErrorInfo errorInfo, Throwable cause) {
        super(errorInfo.getResultCode(), cause);
        this.errorCode = errorInfo.getResultCode();
        this.errorMsg = errorInfo.getResultMsg();
    }

    public NoTokenException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public NoTokenException(String errorCode, String errorMsg) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public NoTokenException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getMessage() {
        return errorMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
