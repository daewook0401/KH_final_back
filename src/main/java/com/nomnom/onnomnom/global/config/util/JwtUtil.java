package com.nomnom.onnomnom.global.config.util;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;
    private SecretKey key;

    @PostConstruct
    public void init(){
        byte[] keyArr = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyArr);
    }

    public String getAccessToken(String memberId){
        return Jwts.builder()
                    .subject(memberId)
                    .issuedAt(new Date())
                    // .expiration(new Date(System.currentTimeMillis() + (3600000L*1)))
                    .expiration(new Date(System.currentTimeMillis() + (5 * 60_000L)))
                    .signWith(key)
                    .compact();
    }

    public String getRefreshToken(String memberId){
        return Jwts.builder()
                    .subject(memberId) // 사용자이름
                    .issuedAt(new Date()) // 발급일
                    .expiration(new Date(System.currentTimeMillis() + (3600000L*24))) // 만료일
                    .signWith(key) // 서명
                    .compact();
    }
    public Claims parseJwt(String token){
        return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }
    public boolean validateRefreshToken(String token) {
        try {
            // 서명·만료 여부 체크 (parseJwt를 그대로 쓰거나, 별도 메서드로 분리)
            parseJwt(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
