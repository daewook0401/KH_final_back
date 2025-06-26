package com.nomnom.onnomnom.token.model.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RefreshToken {
    private String memberNo;
    private String refreshToken;
    private LocalDateTime expiredDate;
}
