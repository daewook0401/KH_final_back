package com.nomnom.onnomnom.auth.model.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.member.model.dto.MemberDTO;
import com.nomnom.onnomnom.member.model.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

    private final MemberService memberService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberDTO member = memberService.selectMemberById(username);
        if(member == null){
            throw new UsernameNotFoundException("존재하지 않는 사용자 입니다.");
        }

        return CustomUserDetails.builder()
                .memberNo(member.getMemberNo())
                .username(member.getMemberId())
                .password(member.getMemberPw())
                .isActive(member.getIsActive())
                .isStoreOwner(member.getIsStoreOwner())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(member.getMemberRole())))
                .build();
    }
}
