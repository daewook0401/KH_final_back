package com.nomnom.onnomnom.global.exception;

import org.springframework.security.core.AuthenticationException;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ResponseWrapperService service;

    private ResponseEntity<ObjectResponseWrapper<String>> makeResponseEntity(RuntimeException e, ErrorCode code){
        return ResponseEntity.ok().body(service.errorCreate(code, e.getMessage()));
    }
    private ResponseEntity<ObjectResponseWrapper<String>> makeResponseEntity(AuthenticationException e, ErrorCode code){
        return ResponseEntity.ok().body(service.errorCreate(code, e.getMessage()));
    }
    
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ObjectResponseWrapper<String>> makeResponseEntity(BaseException e){
        return makeResponseEntity(e, e.getErrorCode());
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<ObjectResponseWrapper<String>> makeResponseEntity(CustomAuthenticationException e){
        return makeResponseEntity(e, e.getErrorCode());
    }
    
    @ExceptionHandler(ExistingOperatingHoursException.class)
    public ResponseEntity<ObjectResponseWrapper<String>> makeResponseEntity(ExistingOperatingHoursException e){
    	return makeResponseEntity(e, e.getErrorCode());
    }
    
    @ExceptionHandler(BreakEndTimeException.class)
    public ResponseEntity<ObjectResponseWrapper<String>> makeResponseEntity(BreakEndTimeException e){
    	return makeResponseEntity(e, e.getErrorCode());
    }
    
    @ExceptionHandler(BreakStartTimeException.class)
    public ResponseEntity<ObjectResponseWrapper<String>> makeResponseEntity(BreakStartTimeException e){
    	return makeResponseEntity(e, e.getErrorCode());
    }
    
    @ExceptionHandler(FailedToEnrollSettingException.class)
    public ResponseEntity<ObjectResponseWrapper<String>> makeResponseEntity(FailedToEnrollSettingException e){
    	return makeResponseEntity(e, e.getErrorCode());
    }
    
    @ExceptionHandler(NotEnrolledOperatingHours.class)
    public ResponseEntity<ObjectResponseWrapper<String>> makeResponseEntity(NotEnrolledOperatingHours e){
    	return makeResponseEntity(e, e.getErrorCode());
    }
}
