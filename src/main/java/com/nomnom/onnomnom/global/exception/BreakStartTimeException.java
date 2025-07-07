package com.nomnom.onnomnom.global.exception;

import com.nomnom.onnomnom.global.enums.ErrorCode;

public class BreakStartTimeException extends RuntimeException {
    private final ErrorCode errorCode;

    public BreakStartTimeException(ErrorCode errorCode){
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }
    public BreakStartTimeException(ErrorCode errorCode, String customMessage){
        super(customMessage);
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
