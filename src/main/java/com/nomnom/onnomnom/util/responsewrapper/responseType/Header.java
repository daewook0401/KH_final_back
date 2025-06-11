package com.nomnom.onnomnom.util.responsewrapper.responseType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Header {
    private int code;
    private String message;
}
