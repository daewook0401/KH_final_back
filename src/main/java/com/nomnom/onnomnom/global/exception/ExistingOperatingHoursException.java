package com.nomnom.onnomnom.global.exception;

import com.nomnom.onnomnom.global.enums.ErrorCode;

public class ExistingOperatingHoursException extends RuntimeException {
    private final ErrorCode errorCode;

    public ExistingOperatingHoursException(ErrorCode errorCode){
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }
    public ExistingOperatingHoursException(ErrorCode errorCode, String customMessage){
        super(customMessage);
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
