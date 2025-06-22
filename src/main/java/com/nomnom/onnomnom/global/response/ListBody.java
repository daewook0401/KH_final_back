package com.nomnom.onnomnom.global.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListBody<T> {
    private List<T> items;
    private int totalCount;
}
