package com.nomnom.onnomnom.token.model.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import com.nomnom.onnomnom.global.config.util.JwtUtil;
import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;
import com.nomnom.onnomnom.token.model.dao.TokenMapper;
import com.nomnom.onnomnom.token.model.dto.RefreshToken;
import com.nomnom.onnomnom.token.model.dto.TokenDTO;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtUtil tokenUtil;
    private final TokenMapper tokenMapper;

    @Override
    public TokenDTO generateToken(String memberId, String memberNo, String authLogin) {
        String accessToken = tokenUtil.getAccessToken(memberId);
        String refreshToken;
        if (authLogin.equals("Y")){
            refreshToken = tokenUtil.getRefreshToken(memberId, authLogin);
            saveToken(refreshToken, memberNo, authLogin);
        } else {
            refreshToken = tokenUtil.getRefreshToken(memberId);
            saveToken(refreshToken, memberNo);
        }
        
        return TokenDTO.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    private void saveToken(String refreshToken, String memberNo){
        long expiryMillis = System.currentTimeMillis() + (3600000L * 24);
        Instant expiryInstant = Instant.ofEpochMilli(expiryMillis);
        LocalDateTime expiryDateTime = expiryInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        RefreshToken token = RefreshToken.builder().memberNo(memberNo).refreshToken(refreshToken).expiredDate(expiryDateTime).build();
        tokenMapper.saveToken(token);
    }
    private void saveToken(String refreshToken, String memberNo, String authLogin){
        long expiryMillis = System.currentTimeMillis() + (3600000L * 24 * 30);
        Instant expiryInstant = Instant.ofEpochMilli(expiryMillis);
        LocalDateTime expiryDateTime = expiryInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        RefreshToken token = RefreshToken.builder().memberNo(memberNo).refreshToken(refreshToken).expiredDate(expiryDateTime).build();
        tokenMapper.saveToken(token);
    }
    @Override
    public TokenDTO refreshToken(String refreshToken) {
        RefreshToken responseToken = tokenMapper.selectByToken(refreshToken);

        if(responseToken == null || responseToken.getExpiredDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < System.currentTimeMillis()){
            throw new BaseException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String memberId = getIdByToken(refreshToken);
        return TokenDTO.builder().accessToken(tokenUtil.getAccessToken(memberId)).refreshToken(refreshToken).build();
    }

    private String getIdByToken(String refreshToken){
        Claims claims = tokenUtil.parseJwt(refreshToken);
        return claims.getSubject();
    }

    @Override
    public void deleteRefreshToken(String memberNo) {
        tokenMapper.deleteRefreshToken(memberNo);
    }
}