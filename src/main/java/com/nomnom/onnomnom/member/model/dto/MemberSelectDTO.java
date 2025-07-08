package com.nomnom.onnomnom.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MemberSelectDTO {
    private String memberNo;
    private String memberId;
    private String memberEmail;
    private String memberName;
    private String memberNickName;
    private String memberPh;
    private String memberRole;
    private String isActive;
    private String isStoreOwner;
    private String search;
}
