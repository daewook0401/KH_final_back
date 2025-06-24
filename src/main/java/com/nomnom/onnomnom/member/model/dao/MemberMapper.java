package com.nomnom.onnomnom.member.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.nomnom.onnomnom.member.model.dto.MemberDTO;
import com.nomnom.onnomnom.member.model.vo.Member;

@Mapper
public interface MemberMapper {
    MemberDTO selectMemberByMemberId(String memberId);
    MemberDTO selectMemberByMemberNickName(String memberNickName);
    MemberDTO insertMember(Member member);

}
