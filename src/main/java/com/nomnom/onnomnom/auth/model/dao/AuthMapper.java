package com.nomnom.onnomnom.auth.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.nomnom.onnomnom.auth.model.dto.VerifyCodeDTO;

@Mapper
public interface AuthMapper {
    void sendVerifyCode(VerifyCodeDTO verifyCode);
}
