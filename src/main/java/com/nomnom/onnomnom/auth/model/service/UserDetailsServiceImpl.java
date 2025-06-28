package com.nomnom.onnomnom.auth.model.service;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.member.model.dao.MemberMapper;
import com.nomnom.onnomnom.member.model.dto.MemberDTO;
import com.nomnom.onnomnom.member.model.dto.MemberSelectDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

    private final MemberMapper memberMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberDTO member = memberMapper.selectMemberByInput(MemberSelectDTO.builder().memberId(username).build()).stream().map(MemberDTO::fromEntity).collect(Collectors.toList()).get(0);
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
