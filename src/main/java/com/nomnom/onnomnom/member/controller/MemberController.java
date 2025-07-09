package com.nomnom.onnomnom.member.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.member.model.dto.CheckInfoDTO;
import com.nomnom.onnomnom.member.model.dto.MemberDTO;
import com.nomnom.onnomnom.member.model.dto.MemberSelectDTO;
import com.nomnom.onnomnom.member.model.service.MemberService;
import com.nomnom.onnomnom.member.model.vo.MemberInsertVo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/member")
@Slf4j
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

    @PutMapping("/update")
    public ResponseEntity<ObjectResponseWrapper<String>> updatelInfo(@ModelAttribute MemberInsertVo info, @RequestPart(value = "memberProFiles", required = false) List<MultipartFile> memberProFiles){
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.updateInfo(info, memberProFiles));
    }
    @PutMapping("/social-update")
    public ResponseEntity<ObjectResponseWrapper<String>> updateSocialInfo(@ModelAttribute MemberInsertVo socialInfo, @RequestPart(value = "memberProFiles", required = false) List<MultipartFile> memberProFiles){
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.updateSocialInfo(socialInfo, memberProFiles));
    }

    @PostMapping("/mypage-info")
    public ResponseEntity<ObjectResponseWrapper<MemberDTO>> selectMypageInfo(@AuthenticationPrincipal CustomUserDetails userDetail ){
        return ResponseEntity.ok(memberService.selectMypageInfo(userDetail.getMemberNo()));
    }

    @GetMapping("/member-list")
    public ResponseEntity<ListResponseWrapper<MemberDTO>> selectMemberList(
        @RequestParam(required = false) String isActive,
        @RequestParam(required = false) String isStoreOwner,
        @RequestParam(required = false) String search,
        @AuthenticationPrincipal CustomUserDetails useDetails
    ){
        log.info("{}", isActive);
        log.info("{}", isStoreOwner);
        log.info("{}", search);
        MemberSelectDTO member = MemberSelectDTO.builder()
        .isActive(isActive)
        .isStoreOwner(isStoreOwner)
        .search(search)
        .build();
        log.info("{}", member);
        return ResponseEntity.ok(memberService.selectMemberList(member));
    }

    @PostMapping("admin-update")
    public ResponseEntity<ObjectResponseWrapper<String>> adminUpdate(
        @RequestBody MemberSelectDTO member
    ){
        
        return ResponseEntity.ok(memberService.updateAdminMember(member));
    }

    @PostMapping("/find-id")
    public ResponseEntity<ObjectResponseWrapper<MemberDTO>> selectFindMember(
        @RequestBody MemberSelectDTO member
    ){
        return ResponseEntity.ok(memberService.selectFindMember(member));
    }

    
}
