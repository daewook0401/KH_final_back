package com.nomnom.onnomnom.auth.model.service;


import com.nomnom.onnomnom.auth.model.dto.VerifyCodeDTO;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.member.model.dto.CheckInfoDTO;

public interface EmailService {
    ObjectResponseWrapper<String> insertVerifyCode(String Email);
    ObjectResponseWrapper<String> editProfileVerify(String email);
    ObjectResponseWrapper<String> pwVerify(CheckInfoDTO checkInfo);
    ObjectResponseWrapper<String> selectCheckVerifyCode(VerifyCodeDTO verifyCodeDTO);
}
