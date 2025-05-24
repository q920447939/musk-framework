package org.example.musk.auth.web.config.exception;

import lombok.Data;

@Data
public class NotTenantException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * * 参数
     */
    protected String param;

    private NotTenantException() {
    }

    public NotTenantException(String param) {
        super(param);
        this.param = param;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
