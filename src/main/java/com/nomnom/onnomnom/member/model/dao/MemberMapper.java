package com.nomnom.onnomnom.member.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.nomnom.onnomnom.member.model.dto.MemberDTO;
import com.nomnom.onnomnom.member.model.entity.MemberEntity;
import com.nomnom.onnomnom.member.model.vo.MemberInsertVo;

@Mapper
public interface MemberMapper {
    MemberEntity selectMemberByMemberId(String memberId);
    MemberEntity selectMemberByMemberNickName(String memberNickName);
    int insertMember(MemberInsertVo member);

}
