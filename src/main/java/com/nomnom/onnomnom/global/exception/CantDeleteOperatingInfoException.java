package com.nomnom.onnomnom.global.exception;

import com.nomnom.onnomnom.global.enums.ErrorCode;

public class CantDeleteOperatingInfoException extends RuntimeException {
    private final ErrorCode errorCode;

    public CantDeleteOperatingInfoException(ErrorCode errorCode){
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }
    public CantDeleteOperatingInfoException(ErrorCode errorCode, String customMessage){
        super(customMessage);
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}