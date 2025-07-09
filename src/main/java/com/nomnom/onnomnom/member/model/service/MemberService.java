package com.nomnom.onnomnom.member.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.member.model.dto.MemberDTO;
import com.nomnom.onnomnom.member.model.dto.MemberSelectDTO;
import com.nomnom.onnomnom.member.model.vo.MemberInsertVo;

public interface MemberService {
    List<MemberDTO> selectMemberByInput(MemberSelectDTO member);
    MemberDTO selectMemberByNo(String memberNo);
    MemberDTO selectMemberById(String memberId);
    MemberDTO selectMemberByEmail(String memberEmail);
    MemberDTO selectMemberByName(String memberName);
    MemberDTO selectMemberByNickName(String memberNickName);
    MemberDTO selectMemberByPh(String memberPh);
    List<MemberDTO> selectMemberByRole(String memberRole);
    List<MemberDTO> selectMemberByIsActive(String isActive);
    List<MemberDTO> selectMemberByIsStoreOwner(String memberInput);
    ObjectResponseWrapper<String> selectCheckId(String memberId);
    ObjectResponseWrapper<String> selectCheckNickName(String memberNickName);
    ObjectResponseWrapper<String> insertMember(MemberDTO member, List<MultipartFile> memberSelfie);
    ObjectResponseWrapper<String> insertMember(MemberInsertVo member, List<MultipartFile> memberSelfie);
    ObjectResponseWrapper<String> updateSocialInfo(MemberInsertVo socialInfo, List<MultipartFile> memberProFiles);
    ObjectResponseWrapper<MemberDTO> selectMypageInfo(String memberNo);
    ListResponseWrapper<MemberDTO> selectMemberList(MemberSelectDTO member);
    ObjectResponseWrapper<String> updateAdminMember(MemberSelectDTO member);
    ObjectResponseWrapper<String> updateInfo(MemberInsertVo info, List<MultipartFile> memberSelfie);
    ObjectResponseWrapper<MemberDTO> selectFindMember(MemberSelectDTO member);
}
