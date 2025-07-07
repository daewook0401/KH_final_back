package com.nomnom.onnomnom.auth.model.service;

import java.util.Map;

import com.nomnom.onnomnom.auth.model.dto.LoginResponseDTO;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;

public interface GoogleService {
    ObjectResponseWrapper<LoginResponseDTO> googleLogin(Map<String, String> body);
}
