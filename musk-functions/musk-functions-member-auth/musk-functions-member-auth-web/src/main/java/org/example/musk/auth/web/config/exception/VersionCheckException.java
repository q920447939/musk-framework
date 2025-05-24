package org.example.musk.auth.web.config.exception;


import org.example.musk.common.exception.IBaseErrorInfo;

/**
 * *
 *版本检查
 */
public class VersionCheckException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * * 错误码
     */
    protected String errorCode;
    /**
     * * 错误信息
     */
    protected String errorMsg;

    public VersionCheckException() {
        super();
    }

    public VersionCheckException(IBaseErrorInfo errorInfo) {
        super(errorInfo.getResultCode());
        this.errorCode = errorInfo.getResultCode();
        this.errorMsg = errorInfo.getResultMsg();
    }

    public VersionCheckException(IBaseErrorInfo errorInfo, Throwable cause) {
        super(errorInfo.getResultCode(), cause);
        this.errorCode = errorInfo.getResultCode();
        this.errorMsg = errorInfo.getResultMsg();
    }

    public VersionCheckException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public VersionCheckException(String errorCode, String errorMsg) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public VersionCheckException(String errorCode, String errorMsg, Throwable cause) {
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
