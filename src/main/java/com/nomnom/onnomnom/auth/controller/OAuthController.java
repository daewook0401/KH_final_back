package com.nomnom.onnomnom.auth.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.auth.model.dto.KakaoUserInfoDTO;
import com.nomnom.onnomnom.auth.model.dto.LoginResponseDTO;
import com.nomnom.onnomnom.auth.model.service.GoogleService;
import com.nomnom.onnomnom.auth.model.service.KakaoService;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
@Slf4j
public class OAuthController {


    private final KakaoService kakaoService;
    private final GoogleService googleService;

    // 카카오 로그인

    /**
     * 카카오 로그인 URL 생성
     */
    @GetMapping("/kakao-url")
    public ResponseEntity<ObjectResponseWrapper<Map<String, String>>> getKakaoLoginUrl(){
        return ResponseEntity.ok(kakaoService.getKakaoLoginUrl());
    }

    @GetMapping("/kakao/callback")
    public void kakaoCallbacks(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        log.info(code);
        try {
            LoginResponseDTO loginResponse = kakaoService.getKakaoAcessToken(code);
            
            // 프론트엔드로 리다이렉트 (토큰 정보를 쿼리 파라미터로 전달)
            String redirectUrl = "http://localhost:5173/oauth2/kakao/callback" + "?refreshToken=" + URLEncoder.encode(loginResponse.getTokens().getRefreshToken(), "UTF-8");
            response.sendRedirect(redirectUrl);
           
        } catch (Exception e) {
            response.sendRedirect("http://localhost:5173/login?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        
        }
    }
    // @PostMapping("/kakao-login")
    // public ResponseEntity<ObjectResponseWrapper<LoginResponseDTO>> kakaoCallback(@RequestBody String code, HttpServletResponse response) throws IOException{
    //     return ResponseEntity.ok(kakaoService.getKakaoAcessToken(code));
    // }
    
    // 구글 로그인
    @PostMapping("/google-login")
    public ResponseEntity<ObjectResponseWrapper<LoginResponseDTO>> googleLogin(@RequestBody Map<String, String> body){
        return ResponseEntity.ok(googleService.googleLogin(body));
    }
}
