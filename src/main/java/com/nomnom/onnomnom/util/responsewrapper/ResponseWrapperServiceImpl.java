package com.nomnom.onnomnom.util.responsewrapper;

import java.util.ArrayList;
import java.util.List;

import com.nomnom.onnomnom.util.responsewrapper.responseType.Body;
import com.nomnom.onnomnom.util.responsewrapper.responseType.Header;
import com.nomnom.onnomnom.util.responsewrapper.responseType.ResponseWrapper;


public class ResponseWrapperServiceImpl<T> implements ResponseWrapperService{

    @Override
    public ResponseWrapper<String> wrapperCreate(int code, String message) {
        Header header = new Header(code, message);
        List<String> list = new ArrayList<>();
        Body<String> body = new Body<String>(list, 0);
        return new ResponseWrapper<>(header, body);
    }

    @Override
    public <T> ResponseWrapper<T> wrapperCreate(int code, String message, List<T> item) {
        Header header = new Header(code, message);
        List<T> safeList = (item != null) ? item : new ArrayList<>();
        Body<T> body = new Body<>(safeList, safeList.size());
        return new ResponseWrapper<>(header, body);
    }

    @Override
    public ResponseWrapper<String> errorCreate(int code, String message){
        return wrapperCreate(code, message);
    }

}
