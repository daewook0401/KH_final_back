package com.nomnom.onnomnom.member.model.vo;

import java.util.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Member {
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberNickName;
    private String memberPh;
    private Date memberEnrollDate;
    private Date memberModifiedDate;
    private String isActive;
    private String memberSelfie;
}
