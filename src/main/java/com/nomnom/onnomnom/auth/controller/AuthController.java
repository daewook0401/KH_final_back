package com.nomnom.onnomnom.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.auth.model.dto.LoginResponseDTO;
import com.nomnom.onnomnom.auth.model.dto.MemberLoginDTO;
import com.nomnom.onnomnom.auth.model.service.AuthService;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/tokens")
    public ResponseEntity<ObjectResponseWrapper<LoginResponseDTO>> tokens(@Valid @RequestBody MemberLoginDTO memberLoginInfo) {
        return ResponseEntity.ok(authService.tokens(memberLoginInfo));
    }
    

}
