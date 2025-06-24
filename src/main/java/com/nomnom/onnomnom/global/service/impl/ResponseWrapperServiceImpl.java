package com.nomnom.onnomnom.global.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.response.Header;
import com.nomnom.onnomnom.global.response.ListBody;
import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.global.response.ObjectBody;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;

@Service
public class ResponseWrapperServiceImpl implements ResponseWrapperService{
    
    private Header makeHeader(ErrorCode code, String message) {
        return new Header(code, message);
    }
    private Header makeHeader(String code, String message) {
        return new Header(code, message);
    }
    @Override
    public ObjectResponseWrapper<String> wrapperCreate(String code, String message){
        return new ObjectResponseWrapper<String>(makeHeader(code, message), null);
    }
    @Override
    public ObjectResponseWrapper<String> wrapperCreate(ErrorCode code, String message) {
        return new ObjectResponseWrapper<String>(makeHeader(code, message), null);
    }

    @Override
    public ObjectResponseWrapper<String> errorCreate(ErrorCode code, String message){
        return wrapperCreate(code, message);
    }

    @Override
    public <U> ListResponseWrapper<U> wrapperCreate(ErrorCode code, String message, List<U> items) {
        List<U> safeList = (items != null) ? items : new ArrayList<U>();
        ListBody<U> body = new ListBody<U>(safeList, safeList.size());
        return new ListResponseWrapper<U>(makeHeader(code, message), body);
    }
    
    @Override
    public <U> ObjectResponseWrapper<U> wrapperCreate(ErrorCode code, String message, U items) {
        ObjectBody<U> body = new ObjectBody<U>(items, 1);
        return new ObjectResponseWrapper<U>(makeHeader(code, message), body);
    }
}
