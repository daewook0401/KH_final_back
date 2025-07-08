package com.nomnom.onnomnom.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nomnom.onnomnom.token.model.dto.TokenDTO;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class LoginResponseDTO {
    private LoginInfo loginInfo;
    private TokenDTO tokens;
}
