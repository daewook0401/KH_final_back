package com.nomnom.onnomnom.member.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.nomnom.onnomnom.member.model.dto.MemberSelectDTO;
import com.nomnom.onnomnom.member.model.entity.MemberEntity;
import com.nomnom.onnomnom.member.model.vo.MemberInsertVo;

@Mapper
public interface MemberMapper {
    List<MemberEntity> selectMemberByInput(MemberSelectDTO memberInput);
    void insertMember(MemberInsertVo member);
    void updateSocialInfo(MemberInsertVo member);
    List<MemberEntity> selectMemberList(MemberSelectDTO memberInput);
    void updateAdminMember(MemberInsertVo member);
}