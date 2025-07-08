package com.nomnom.onnomnom.global.response;

import com.nomnom.onnomnom.global.enums.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Header {
    private String code;
    private String message;

    public Header(ErrorCode errorCode, String message){
        this.code = errorCode.getCode();
        this.message = message;
    }
}
