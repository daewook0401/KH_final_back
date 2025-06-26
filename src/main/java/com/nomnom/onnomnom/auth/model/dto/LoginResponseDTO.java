package com.nomnom.onnomnom.auth.model.dto;

import com.nomnom.onnomnom.token.model.dto.TokenDTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginResponseDTO {
    private LoginInfo loginInfo;
    private TokenDTO tokens;
}
