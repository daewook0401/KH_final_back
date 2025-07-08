package com.nomnom.onnomnom.global.exception;

import org.springframework.security.core.AuthenticationException;

import com.nomnom.onnomnom.global.enums.ErrorCode;

public class CustomAuthenticationException extends AuthenticationException {
    private final ErrorCode errorCode;

    public CustomAuthenticationException(ErrorCode errorCode){
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }
    public CustomAuthenticationException(ErrorCode errorCode, String customMessage){
        super(customMessage);
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
