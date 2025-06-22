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
    
    @Override
    public ObjectResponseWrapper<String> wrapperCreate(ErrorCode code, String message) {
        Header header = new Header(code, message);
        return new ObjectResponseWrapper<>(header, null);
    }

    @Override
    public ObjectResponseWrapper<String> errorCreate(ErrorCode code, String message){
        return wrapperCreate(code, message);
    }

    @Override
    public <U> ListResponseWrapper<U> wrapperCreate(ErrorCode code, String message, List<U> item) {
        Header header = new Header(code, message);
        List<U> safeList = (item != null) ? item : new ArrayList<>();
        ListBody<U> body = new ListBody<>(safeList, safeList.size());
        return new ListResponseWrapper<>(header, body);
    }
    
    @Override
    public <U> ObjectResponseWrapper<U> wrapperCreate(ErrorCode code, String message, U item) {
        Header header = new Header(code, message);
        ObjectBody<U> body = new ObjectBody<>(item, 1);
        return new ObjectResponseWrapper<>(header, body);
    }
}
