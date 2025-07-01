package com.nomnom.onnomnom.global.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GlobalController {
    private final ResponseWrapperService responseWrapperService;
    @GetMapping
    public ResponseEntity<ObjectResponseWrapper<String>> global(){
        return ResponseEntity.ok().body(responseWrapperService.wrapperCreate("S100", "글로벌컨트롤러 통신 성공"));
    }

    @PostMapping("/test")
    public ResponseEntity<ObjectResponseWrapper<String>> postMethodName() {
        return ResponseEntity.ok().body(responseWrapperService.wrapperCreate("S100", "글로벌컨트롤러 통신 성공"));
    }
    
}
