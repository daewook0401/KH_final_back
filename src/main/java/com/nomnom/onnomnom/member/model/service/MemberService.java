package com.nomnom.onnomnom.member.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.member.model.dto.MemberDTO;

public interface MemberService {
    ObjectResponseWrapper<String> selectCheckId(MemberDTO member);
    ObjectResponseWrapper<String> selectCheckNickName(MemberDTO member);
    ObjectResponseWrapper<String> insertMember(MemberDTO member, List<MultipartFile> memberSelfie);
}
