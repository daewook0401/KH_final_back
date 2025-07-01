package com.nomnom.onnomnom.member.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.member.model.dto.CheckInfoDTO;
import com.nomnom.onnomnom.member.model.dto.MemberDTO;
import com.nomnom.onnomnom.member.model.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ObjectResponseWrapper<String>> insertMember(@ModelAttribute MemberDTO member, @RequestPart(value = "memberProFiles", required = false) List<MultipartFile> memberProFiles) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.insertMember(member, memberProFiles));
    }

    @PostMapping("/check-id")
    public ResponseEntity<ObjectResponseWrapper<String>> selectCheckId(@Valid @RequestBody CheckInfoDTO checkInfo){
        return ResponseEntity.ok(memberService.selectCheckId(checkInfo.getMemberId()));
    }
    @PostMapping("/check-nickname")
    public ResponseEntity<ObjectResponseWrapper<String>> selectCheckNickName(@Valid @RequestBody CheckInfoDTO checkInfo){
        return ResponseEntity.ok(memberService.selectCheckNickName(checkInfo.getMemberNickName()));
    }

    
}
