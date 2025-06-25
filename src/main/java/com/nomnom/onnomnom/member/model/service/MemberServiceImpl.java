package com.nomnom.onnomnom.member.model.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.member.model.dao.MemberMapper;
import com.nomnom.onnomnom.member.model.dto.MemberDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final ResponseWrapperService responseWrapperService;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ObjectResponseWrapper<String> selectCheckId(MemberDTO member) {
        if(memberMapper.selectMemberByMemberId(member.getMemberId()) != null){
            throw new BaseException(ErrorCode.DUPLICATE_MEMBER_ID);
        }
        return responseWrapperService.wrapperCreate("S104","아이디 비중복 조회 성공");
    }

    @Override
    public ObjectResponseWrapper<String> selectCheckNickName(MemberDTO member) {
        if(memberMapper.selectMemberByMemberNickName(member.getMemberNickName()) != null){
            throw new BaseException(ErrorCode.DUPLICATE_NICKNAME);
        }
        return responseWrapperService.wrapperCreate("S104", "닉네임 비중복 조회 성공");
    }

    @Override
    public ObjectResponseWrapper<String> insertMember(MemberDTO member, MultipartFile memberSelfie){
        memberMapper.insertMember(member);
        return responseWrapperService.wrapperCreate("S100","회원가입 성공");
    }
}
