package com.nomnom.onnomnom.global.service;

import java.util.List;

import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;


public interface ResponseWrapperService {
    ObjectResponseWrapper<String> wrapperCreate(ErrorCode code, String message);
    ObjectResponseWrapper<String> wrapperCreate(String code, String message);
    ObjectResponseWrapper<String> errorCreate(ErrorCode code, String message);
    <T> ListResponseWrapper<T> wrapperCreate(String code, String message, List<T> item);
    <T> ObjectResponseWrapper<T> wrapperCreate(String code, String message, T item);
}
