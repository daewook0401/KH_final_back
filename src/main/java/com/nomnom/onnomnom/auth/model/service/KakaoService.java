package com.nomnom.onnomnom.auth.model.service;
import java.util.Map;

import com.nomnom.onnomnom.auth.model.dto.LoginResponseDTO;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;

public interface KakaoService {
    ObjectResponseWrapper<Map<String, String>> getKakaoLoginUrl();
    LoginResponseDTO getKakaoAcessToken(String code);
}
