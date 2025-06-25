package com.nomnom.onnomnom.member.model.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.FileService;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.member.model.dao.MemberMapper;
import com.nomnom.onnomnom.member.model.dto.MemberDTO;
import com.nomnom.onnomnom.member.model.vo.MemberInsertVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final ResponseWrapperService responseWrapperService;
    private final MemberMapper memberMapper;
    private final FileService fileService;
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
    public ObjectResponseWrapper<String> insertMember(MemberDTO member, List<MultipartFile> memberSelfie){
        List<String> url = fileService.imageUpLoad(memberSelfie);
        MemberInsertVo memberValue = MemberInsertVo.builder()
                                                    .memberId(member.getMemberId())
                                                    .memberPw(passwordEncoder.encode(member.getMemberPw()))
                                                    .memberEmail(member.getMemberEmail())
                                                    .memberName(member.getMemberName())
                                                    .memberNickName(member.getMemberNickName())
                                                    .memberPh(member.getMemberPh())
                                                    .memberRole(member.getMemberRole())
                                                    .memberSelfie(url.get(0))
                                                    .build();
        memberMapper.insertMember(memberValue);
        return responseWrapperService.wrapperCreate("S100","회원가입 성공");
    }
}
