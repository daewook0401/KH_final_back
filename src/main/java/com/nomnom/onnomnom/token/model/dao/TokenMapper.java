package com.nomnom.onnomnom.token.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.nomnom.onnomnom.token.model.dto.RefreshToken;

@Mapper
public interface TokenMapper {
    void saveToken(RefreshToken token);
    RefreshToken selectByToken(String RefreshToken);
}
