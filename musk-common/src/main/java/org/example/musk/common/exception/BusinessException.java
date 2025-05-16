package org.example.musk.common.exception;



/**
 *业务异常
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * * 错误码
     */
    protected String errorCode;
    /**
     * * 错误信息
     */
    protected String errorMsg;

    public BusinessException() {
        super();
    }

    public BusinessException(IBaseErrorInfo errorInfo) {
        super(errorInfo.getResultCode());
        this.errorCode = errorInfo.getResultCode();
        this.errorMsg = errorInfo.getResultMsg();
    }

    public BusinessException(BusinessPageExceptionEnum businessPageExceptionEnum) {
        super(businessPageExceptionEnum.getResultCode());
        this.errorCode = businessPageExceptionEnum.getResultCode();
        this.errorMsg = businessPageExceptionEnum.getResultMsg();
    }

    public BusinessException(IBaseErrorInfo errorInfo, Throwable cause) {
        super(errorInfo.getResultCode(), cause);
        this.errorCode = errorInfo.getResultCode();
        this.errorMsg = errorInfo.getResultMsg();
    }

    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public BusinessException(String errorCode, String errorMsg) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(String errorCode, String errorMsg, Throwable cause) {
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


}
