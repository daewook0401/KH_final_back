package com.nomnom.onnomnom.global.exception;

import com.nomnom.onnomnom.global.enums.ErrorCode;

public class NonExistingTimeException extends RuntimeException {
    private final ErrorCode errorCode;

    public NonExistingTimeException(ErrorCode errorCode){
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }
    public NonExistingTimeException(ErrorCode errorCode, String customMessage){
        super(customMessage);
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
