package com.nomnom.onnomnom.auth.model.service;

import com.nomnom.onnomnom.auth.model.dto.LoginResponseDTO;
import com.nomnom.onnomnom.auth.model.dto.MemberLoginDTO;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;

public interface AuthService {
    ObjectResponseWrapper<LoginResponseDTO> tokens(MemberLoginDTO memberLoginInfo);
    ObjectResponseWrapper<String> selectCheckEmail(String email);
}
