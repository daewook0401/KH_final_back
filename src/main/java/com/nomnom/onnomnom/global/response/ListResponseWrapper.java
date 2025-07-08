package com.nomnom.onnomnom.global.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListResponseWrapper<T> {
    private Header header;
    private ListBody<T> body;
}
