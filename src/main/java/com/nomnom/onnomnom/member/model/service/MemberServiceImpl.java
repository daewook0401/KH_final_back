package com.nomnom.onnomnom.member.model.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;
import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.FileService;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.global.service.S3Service;
import com.nomnom.onnomnom.member.model.dao.MemberMapper;
import com.nomnom.onnomnom.member.model.dto.MemberDTO;
import com.nomnom.onnomnom.member.model.dto.MemberSelectDTO;
import com.nomnom.onnomnom.member.model.entity.MemberEntity;
import com.nomnom.onnomnom.member.model.vo.MemberInsertVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final ResponseWrapperService responseWrapperService;
    private final MemberMapper memberMapper;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    @Override
    public List<MemberDTO> selectMemberByInput(MemberSelectDTO member) {
        List<MemberEntity> memberResult = memberMapper.selectMemberByInput(member);
        if(memberResult.size() <= 0){
            return null;
        }
        return memberResult.stream().map(MemberDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public MemberDTO selectMemberByNo(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().memberNo(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        if (memberResult == null){
            return null;
        }
        return memberResult.get(0);
    }
    @Override
    public MemberDTO selectMemberById(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().memberId(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        if (memberResult == null){
            return null;
        }
        return memberResult.get(0);
    }
    @Override
    public MemberDTO selectMemberByEmail(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().memberEmail(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        if (memberResult == null){
            return null;
        }
        return memberResult.get(0);
    }
    @Override
    public MemberDTO selectMemberByName(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().memberName(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        if (memberResult == null){
            return null;
        }
        return memberResult.get(0);
    }
    @Override
    public MemberDTO selectMemberByNickName(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().memberNickName(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        if (memberResult == null){
            return null;
        }
        return memberResult.get(0);
    }
    @Override
    public MemberDTO selectMemberByPh(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().memberPh(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        if (memberResult == null){
            return null;
        }
        return memberResult.get(0);
    }
    @Override
    public List<MemberDTO> selectMemberByRole(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().memberRole(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        if (memberResult == null){
            return null;
        }
        return memberResult;
    }
    @Override
    public List<MemberDTO> selectMemberByIsActive(String memberInput) {
        MemberSelectDTO member = MemberSelectDTO.builder().isActive(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        if (memberResult == null){
            return null;
        }
        return memberResult;
    }
    @Override
    public List<MemberDTO> selectMemberByIsStoreOwner(String memberInput){
        MemberSelectDTO member = MemberSelectDTO.builder().isStoreOwner(memberInput).build();
        List<MemberDTO> memberResult = selectMemberByInput(member);
        if (memberResult == null){
            return null;
        }
        return memberResult;
    }
    
    @Override
    public ObjectResponseWrapper<String> selectCheckId(String memberId){
        if(selectMemberById(memberId) != null){
            throw new BaseException(ErrorCode.DUPLICATE_MEMBER_ID);
        }
        log.info("{}",memberId);
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
        
        String url = "";
        if (memberSelfie == null){
            url = "NULL";
        } else if (memberSelfie.size()==1){
            url = fileService.imageUpLoad(memberSelfie).get(0);
        } else if(memberSelfie.size()>1) {
            throw new BaseException(ErrorCode.FILE_SIZE_EXCEEDED, "이미지 개수가 초과 되었습니다.");
        }

        
        MemberInsertVo memberValue = MemberInsertVo.builder()
                                                    .memberId(member.getMemberId())
                                                    .memberPw(passwordEncoder.encode(member.getMemberPw()))
                                                    .memberEmail(member.getMemberEmail())
                                                    .memberName(member.getMemberName())
                                                    .memberNickName(member.getMemberNickName())
                                                    .memberPh(member.getMemberPh())
                                                    .memberRole("ROLE_COMMON")
                                                    .memberSelfie(url)
                                                    .build();
        memberMapper.insertMember(memberValue);
        return responseWrapperService.wrapperCreate("S106", "계정 생성 성공");
    }
    @Override
    public ObjectResponseWrapper<String> insertMember(MemberInsertVo member, List<MultipartFile> memberSelfie){
        
        String url = "";
        if (memberSelfie == null){
            url = "NULL";
        } else if (memberSelfie.size()==1){
            url = fileService.imageUpLoad(memberSelfie).get(0);
        } else if(memberSelfie.size()>1) {
            throw new BaseException(ErrorCode.FILE_SIZE_EXCEEDED, "이미지 개수가 초과 되었습니다.");
        }

        
        MemberInsertVo memberValue = MemberInsertVo.builder()
                                                    .memberId(member.getMemberId())
                                                    .memberPw(passwordEncoder.encode(member.getMemberPw()))
                                                    .memberEmail(member.getMemberEmail())
                                                    .memberName(member.getMemberName())
                                                    .memberNickName(member.getMemberNickName())
                                                    .memberPh(member.getMemberPh())
                                                    .memberRole("ROLE_COMMON")
                                                    .memberSelfie(url)
                                                    .build();
        memberMapper.insertMember(memberValue);
        return responseWrapperService.wrapperCreate("S106", "계정 생성 성공");
    }

    @Override
    public ObjectResponseWrapper<String> updateInfo(MemberInsertVo info,
            List<MultipartFile> memberSelfie) {
        
        String url = "";
        if (memberSelfie == null){
            url = "NULL";
        } else if (memberSelfie.size()==1){
            url = fileService.imageUpLoad(memberSelfie).get(0);
        } else if(memberSelfie.size()>1) {
            throw new BaseException(ErrorCode.FILE_SIZE_EXCEEDED, "이미지 개수가 초과 되었습니다.");
        }
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();
        MemberInsertVo memberValue;
        String oldSelfie = selectMemberById(userDetails.getUsername()).getMemberSelfie();
        if (oldSelfie != "NULL"){
            s3Service.deleteFile(oldSelfie);
        }
        if (info.getMemberPw() != null && !info.getMemberPw().isBlank()) {
            memberValue = MemberInsertVo.builder()
                                            .memberId(userDetails.getUsername())
                                            .memberNickName(info.getMemberNickName())
                                            .memberPw(passwordEncoder.encode(info.getMemberPw()))
                                            .memberSelfie(url)
                                            .build();
        } else {
            memberValue = MemberInsertVo.builder()
                                .memberId(userDetails.getUsername())
                                .memberNickName(info.getMemberNickName())
                                .memberSelfie(url)
                                .build();
        }
        memberMapper.updateInfo(memberValue);

        return responseWrapperService.wrapperCreate("S106", "계정 정보 변경");
    }
    @Override
    public ObjectResponseWrapper<String> updateSocialInfo(MemberInsertVo socialInfo,
            List<MultipartFile> memberSelfie) {
        
        String url = "";
        if (memberSelfie == null){
            url = "NULL";
        } else if (memberSelfie.size()==1){
            url = fileService.imageUpLoad(memberSelfie).get(0);
        } else if(memberSelfie.size()>1) {
            throw new BaseException(ErrorCode.FILE_SIZE_EXCEEDED, "이미지 개수가 초과 되었습니다.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                                .getAuthentication()
                                .getPrincipal();
        MemberInsertVo memberValue = MemberInsertVo.builder()
                                                    .memberId(userDetails.getUsername())
                                                    .memberName(socialInfo.getMemberName())
                                                    .memberNickName(socialInfo.getMemberNickName())
                                                    .memberSelfie(url)
                                                    .build();
        memberMapper.updateSocialInfo(memberValue);

        return responseWrapperService.wrapperCreate("S106", "소셜 계정 생성 성공");
    }

    @Override
    public ObjectResponseWrapper<MemberDTO> selectMypageInfo(String memberNo){
        return responseWrapperService.wrapperCreate("S100", "마이페이지 정보 조회 성공", selectMemberByNo(memberNo));
    }

    @Override
    public ListResponseWrapper<MemberDTO> selectMemberList(MemberSelectDTO member) {
        List<MemberEntity> memberResult = memberMapper.selectMemberList(member);
        if(memberResult.size() <= 0){
            throw new BaseException(ErrorCode.MEMBER_NOT_FOUND, "조회 멤버가 없습니다.");
        }
        return responseWrapperService.wrapperCreate("S100", "멤버 조회 성공", memberResult.stream().map(MemberDTO::fromEntity).collect(Collectors.toList()));
    }

    @Override
    public ObjectResponseWrapper<String> updateAdminMember(MemberSelectDTO member){
        MemberInsertVo memberValue = MemberInsertVo.builder()
                                            .memberId(member.getMemberId())
                                            .isActive(member.getIsActive())
                                            .isStoreOwner(member.getIsStoreOwner())
                                            .build();
        memberMapper.updateAdminMember(memberValue);
        return responseWrapperService.wrapperCreate("S100", "수정 성공");
    }
}
