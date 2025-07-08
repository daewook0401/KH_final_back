package com.nomnom.onnomnom.auth.controller;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.auth.model.dto.LoginResponseDTO;
import com.nomnom.onnomnom.auth.model.dto.MemberLoginDTO;
import com.nomnom.onnomnom.auth.model.service.AuthService;
import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/tokens")
    public ResponseEntity<ObjectResponseWrapper<LoginResponseDTO>> tokens(@Valid @RequestBody MemberLoginDTO memberLoginInfo) {
        ObjectResponseWrapper<LoginResponseDTO> response = authService.tokens(memberLoginInfo);
        if (memberLoginInfo.getAuthLogin().equals("Y")){
            LoginResponseDTO body = response.getBody().getItems();
            ResponseCookie cookie = ResponseCookie
                .from("Refresh-Token", body.getTokens().getRefreshToken())
                .domain("localhost")
                .httpOnly(true)
                .secure(false) // https 환경일 때 true로 변경
                .path("/")
                .maxAge(Duration.ofDays(30))
                .sameSite("Lax")
                .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
        }
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<ObjectResponseWrapper<LoginResponseDTO>> refreshTokens(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
        @CookieValue(value = "Refresh-Token", required = false) String refreshTokenCookie
    ) {
        String refreshToken;
        log.info("{}",authorizationHeader);
        log.info("{}", refreshTokenCookie);
        if (refreshTokenCookie != null){
            refreshToken = refreshTokenCookie;
        } else {
            refreshToken = authorizationHeader.replaceFirst("Bearer ", "");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.refreshAccessToken(refreshToken));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ObjectResponseWrapper<String>> logout(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        ResponseCookie deleteCookie = ResponseCookie
                .from("Refresh-Token", "")
                .domain("localhost")
                .httpOnly(true)
                .secure(false) // https 환경일 때 true로 변경
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookie.toString()).body(authService.logout(userDetails));
    }

    @PostMapping("password-confirm")
    public ResponseEntity<ObjectResponseWrapper<String>> passwordConfirm(@RequestBody Map<String, String> password){
        return ResponseEntity.ok(authService.passwordConfirm(password.get("password")));
    }

}
