package com.nomnom.onnomnom.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.auth.model.dto.VerifyCodeDTO;
import com.nomnom.onnomnom.auth.model.service.EmailService;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.member.model.dto.CheckInfoDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    @PostMapping("/verify-email")
    public ResponseEntity<ObjectResponseWrapper<String>> verifyEmail(@Valid @RequestBody CheckInfoDTO checkInfo){
        return ResponseEntity.status(HttpStatus.CREATED).body(emailService.insertVerifyCode(checkInfo.getMemberEmail()));
    }
    @PostMapping("/editprofile-verify")
    public ResponseEntity<ObjectResponseWrapper<String>> verifyEditProfile(@Valid @RequestBody CheckInfoDTO checkInfo){
        return ResponseEntity.status(HttpStatus.CREATED).body(emailService.editProfileVerify(checkInfo.getMemberEmail()));
    }
    @PostMapping("/check-verifycode")
    public ResponseEntity<ObjectResponseWrapper<String>> checkVerifyCode(@RequestBody VerifyCodeDTO verifyCodeDTO){
        return ResponseEntity.ok(emailService.selectCheckVerifyCode(verifyCodeDTO));
    }
    @PostMapping("/pw-verify")
    public ResponseEntity<ObjectResponseWrapper<String>> checkVerifyCodePw(@Valid @RequestBody CheckInfoDTO checkInfo){
        return ResponseEntity.ok(emailService.pwVerify(checkInfo));
    }
}
