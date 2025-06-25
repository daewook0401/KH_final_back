package com.nomnom.onnomnom.member.model.service;

import java.util.List;
import java.util.stream.Collectors;

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
import com.nomnom.onnomnom.member.model.dto.MemberSelectDTO;
import com.nomnom.onnomnom.member.model.entity.MemberEntity;
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
    public List<MemberDTO> selectMemberByInput(MemberSelectDTO member) {
        List<MemberEntity> memberResult = memberMapper.selectMemberByInput(member);

        return memberResult.stream().map(MemberDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public MemberDTO selectMemberByNo(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().memberNo(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        
        return memberResult.get(0);
    }
    @Override
    public MemberDTO selectMemberById(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().memberId(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        return memberResult.get(0);
    }
    @Override
    public MemberDTO selectMemberByEmail(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().memberEmail(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        return memberResult.get(0);
    }
    @Override
    public MemberDTO selectMemberByName(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().memberName(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        return memberResult.get(0);
    }
    @Override
    public MemberDTO selectMemberByNickName(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().memberNickName(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        return memberResult.get(0);
    }
    @Override
    public MemberDTO selectMemberByPh(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().memberPh(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        return memberResult.get(0);
    }
    @Override
    public List<MemberDTO> selectMemberByRole(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().memberRole(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        return memberResult;
    }
    @Override
    public List<MemberDTO> selectMemberByIsActive(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().isActive(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        return memberResult;
    }
    
    @Override
    public ObjectResponseWrapper<String> selectCheckId(String memberId){
        if(selectMemberById(memberId) != null){
            throw new BaseException(ErrorCode.DUPLICATE_MEMBER_ID);
        }
        return responseWrapperService.wrapperCreate("S104", "사용할 수 있는 아이디");
    }
    @Override
    public ObjectResponseWrapper<String> selectCheckNickName(String memberNickName){
        if(selectMemberByNickName(memberNickName) != null){
            throw new BaseException(ErrorCode.DUPLICATE_NICKNAME);
        }
        return responseWrapperService.wrapperCreate("S104", "사용할 수 있는 닉네임");
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
        return responseWrapperService.wrapperCreate("S106", "계정 생성 성공");
    }
}
