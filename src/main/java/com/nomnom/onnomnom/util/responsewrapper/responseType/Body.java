package com.nomnom.onnomnom.util.responsewrapper.responseType;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Body<T> {
    private List<T> items;
    private int totalCount;
}
