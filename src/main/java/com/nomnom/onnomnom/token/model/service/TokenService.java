package com.nomnom.onnomnom.token.model.service;

import com.nomnom.onnomnom.token.model.dto.TokenDTO;

public interface TokenService {
    TokenDTO generateToken(String memberId, String memberNo);

    TokenDTO refreshToken(String refreshToken);

    void deleteRefreshToken(String memberNo);
}
