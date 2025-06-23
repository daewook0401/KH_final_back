package com.nomnom.onnomnom.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ObjectBody<T> {
    private T items;
    private int totalCount;
}
