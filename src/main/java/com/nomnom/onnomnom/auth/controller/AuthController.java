package com.nomnom.onnomnom.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        return ResponseEntity.ok(authService.tokens(memberLoginInfo));
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<ObjectResponseWrapper<LoginResponseDTO>> refreshTokens(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        String refreshToken = authorizationHeader.replaceFirst("Bearer ", "");
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.refreshAccessToken(refreshToken));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ObjectResponseWrapper<String>> logout(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.ok(authService.logout(userDetails));
    }
    
}
