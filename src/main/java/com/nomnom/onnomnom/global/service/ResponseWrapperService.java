package com.nomnom.onnomnom.global.service;

import java.util.List;

import com.nomnom.onnomnom.global.response.ResponseWrapper;


public interface ResponseWrapperService {
    ResponseWrapper<String> wrapperCreate(int code, String message);
    <T> ResponseWrapper<T> wrapperCreate(int code, String message, List<T> item);
    ResponseWrapper<String> errorCreate(int code, String message);
}
