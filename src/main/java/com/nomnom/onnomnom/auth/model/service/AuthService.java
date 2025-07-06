package com.nomnom.onnomnom.auth.model.service;


import com.nomnom.onnomnom.auth.model.dto.LoginResponseDTO;
import com.nomnom.onnomnom.auth.model.dto.MemberLoginDTO;
import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;

public interface AuthService {
    ObjectResponseWrapper<LoginResponseDTO> tokens(MemberLoginDTO memberLoginInfo);
    ObjectResponseWrapper<LoginResponseDTO> refreshAccessToken (String refreshToken);
    CustomUserDetails getUserDetails();
    ObjectResponseWrapper<String> logout(CustomUserDetails userDetails);
}
