package com.nomnom.onnomnom.member.model.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "TB_MEMBER")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEntity {
    @Id private Long memberNo;
    private String memberId;
    private String memberPw;
    private String memberEmail;
    private String memberName;
    private String memberNickName;
    private String memberPh;
    private Date memberEnrollDate;
    private Date memberModifiedDate;
    private String memberRole;
    private String isActive;
    private String memberSelfie;
}
