package com.nomnom.onnomnom.auth.model.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.nomnom.onnomnom.auth.model.dto.GoogleTokenResponse;
import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;

@Service
public class GoogleOAuthService {
    private static final String TOKEN_URI = "https://oauth2.googleapis.com/token";

    // application.yml 또는 환경변수에서 주입
    @Value("${google.client-id}")
    private String clientId;
    @Value("${google.client-secret}")
    private String clientSecret;
    @Value("${google.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    public GoogleTokenResponse exchangeCodeForTokens(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // form data 세팅
        MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("redirect_uri", redirectUri);
        form.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<GoogleTokenResponse> response = restTemplate.postForEntity(
            TOKEN_URI, request, GoogleTokenResponse.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new BaseException(ErrorCode.INVALID_TOKEN, "구글 토큰 교환 실패");
        }
        return response.getBody();
    }
}
