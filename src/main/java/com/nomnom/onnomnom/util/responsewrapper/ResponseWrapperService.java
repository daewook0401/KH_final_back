package com.nomnom.onnomnom.util.responsewrapper;

import java.util.List;

import com.nomnom.onnomnom.util.responsewrapper.responseType.ResponseWrapper;


public interface ResponseWrapperService {
    ResponseWrapper<String> wrapperCreate(int code, String message);
    <T> ResponseWrapper<T> wrapperCreate(int code, String message, List<T> item);
    ResponseWrapper<String> errorCreate(int code, String message);
}
