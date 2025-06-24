// package com.nomnom.onnomnom.auth.controller;

// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
// import com.nomnom.onnomnom.member.model.dto.MemberDTO;

// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;


// @RestController
// @RequestMapping("/api/auth")
// @RequiredArgsConstructor
// public class AuthController {

//     @PostMapping("/tokens")
//     public ResponseEntity<ObjectResponseWrapper> tokens(@Valid @RequestBody MemberDTO member) {
//         ObjectResponseWrapper loginResponse = authService.tokens(member);
//         return ResponseEntity.ok();
//     }
    
// }
