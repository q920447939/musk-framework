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

    /**
     * 构造函数 - 支持参数化错误信息
     *
     * @param code 错误码
     * @param args 参数（用于格式化错误信息）
     */
    public BusinessException(String code, Object... args) {
        super(code);
        this.errorCode = code;
        // 如果有参数，可以用于格式化错误信息，这里简单处理为拼接
        if (args != null && args.length > 0) {
            StringBuilder sb = new StringBuilder(code);
            for (Object arg : args) {
                sb.append(" ").append(arg);
            }
            this.errorMsg = sb.toString();
        } else {
            this.errorMsg = code;
        }
    }

    /**
     * 构造函数 - 支持错误码和异常原因
     *
     * @param code 错误码
     * @param cause 异常原因
     */
    public BusinessException(String code, Throwable cause) {
        super(code, cause);
        this.errorCode = code;
        this.errorMsg = code;
    }

    /**
     * 构造函数 - 支持错误码、异常原因和参数
     *
     * @param code 错误码
     * @param cause 异常原因
     * @param args 参数（用于格式化错误信息）
     */
    public BusinessException(String code, Throwable cause, Object... args) {
        super(code, cause);
        this.errorCode = code;
        // 如果有参数，可以用于格式化错误信息，这里简单处理为拼接
        if (args != null && args.length > 0) {
            StringBuilder sb = new StringBuilder(code);
            for (Object arg : args) {
                sb.append(" ").append(arg);
            }
            this.errorMsg = sb.toString();
        } else {
            this.errorMsg = code;
        }
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
