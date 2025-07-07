package com.nomnom.onnomnom.global.exception;

import com.nomnom.onnomnom.global.enums.ErrorCode;

public class BreakEndTimeException extends RuntimeException {
    private final ErrorCode errorCode;

    public BreakEndTimeException(ErrorCode errorCode){
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }
    public BreakEndTimeException(ErrorCode errorCode, String customMessage){
        super(customMessage);
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
