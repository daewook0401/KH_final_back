package com.nomnom.onnomnom.member.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MemberInsertVo {
    private String memberId;
    private String memberPw;
    private String memberEmail;
    private String memberName;
    private String memberNickName;
    private String memberPh;
    private String memberRole;
    private String memberSelfie;
    private String isActive;
    private String isStoreOwner;
}
