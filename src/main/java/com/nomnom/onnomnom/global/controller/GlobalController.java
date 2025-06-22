package com.nomnom.onnomnom.global.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GlobalController {
    
    @GetMapping
    public ResponseEntity<String> global(){
        return ResponseEntity.ok().body("성공");
    }
}
