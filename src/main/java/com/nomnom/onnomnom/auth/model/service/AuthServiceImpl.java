package com.nomnom.onnomnom.auth.model.service;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nomnom.onnomnom.auth.model.dao.AuthMapper;
import com.nomnom.onnomnom.auth.model.dto.LoginInfo;
import com.nomnom.onnomnom.auth.model.dto.LoginResponseDTO;
import com.nomnom.onnomnom.auth.model.dto.MemberLoginDTO;
import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.global.config.util.JwtUtil;
import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.CustomAuthenticationException;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.member.model.dto.MemberDTO;
import com.nomnom.onnomnom.member.model.service.MemberService;
import com.nomnom.onnomnom.token.model.dto.TokenDTO;
import com.nomnom.onnomnom.token.model.service.TokenService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final ResponseWrapperService responseWrapperService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final AuthMapper authMapper;

    @Override
    public ObjectResponseWrapper<LoginResponseDTO> tokens(MemberLoginDTO memberLoginInfo) {
        Authentication authentication = null;
        log.debug("여기에요 여기 Attempt login for id={} / pw={}", memberLoginInfo.getMemberId(), memberLoginInfo.getMemberPw());
        try{
            authentication =
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    memberLoginInfo.getMemberId(), 
                    memberLoginInfo.getMemberPw()));
        } catch(AuthenticationException e) { 
            throw new CustomAuthenticationException(ErrorCode.ID_PASSWORD_MISMATCH);
        }
        CustomUserDetails loginMember = (CustomUserDetails)authentication.getPrincipal();

        LoginResponseDTO loginResponse = LoginResponseDTO
                .builder()
                .loginInfo(LoginInfo.builder()
                                    .memberNo(loginMember.getMemberNo())
                                    .username(loginMember.getUsername())
                                    .memberRole(loginMember.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("ROLE_USER"))
                                    .isStoreOwner(loginMember.getIsStoreOwner())
                                    .build())
                .tokens(tokenService.generateToken(loginMember.getUsername(), loginMember.getMemberNo()))
                .build();
        log.debug("여기에요 여기 Attempt login for id={} / pw={}", memberLoginInfo.getMemberId(), memberLoginInfo.getMemberPw());
        return responseWrapperService.wrapperCreate("S100", "로그인 성공", loginResponse);
    }

    @Override
    public ObjectResponseWrapper<LoginResponseDTO> refreshAccessToken(String refreshToken) {
        String refresh = refreshToken.replaceFirst("Bearer ", "");
        TokenDTO newTokens = tokenService.refreshToken(refresh);
        String user = getIdByToken(refreshToken);
        MemberDTO member = memberService.selectMemberById(user);
        CustomUserDetails loginMember = CustomUserDetails.builder()
                .memberNo(member.getMemberNo())
                .username(member.getMemberId())
                .password(member.getMemberPw())
                .isActive(member.getIsActive())
                .isStoreOwner(member.getIsStoreOwner())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(member.getMemberRole())))
                .build();
        LoginResponseDTO loginResponse = LoginResponseDTO
                .builder()
                .loginInfo(LoginInfo.builder()
                                    .memberNo(loginMember.getMemberNo())
                                    .username(loginMember.getUsername())
                                    .memberRole(loginMember.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("ROLE_USER"))
                                    .isStoreOwner(loginMember.getIsStoreOwner())
                                    .build())
                .tokens(newTokens)
                .build();
        return responseWrapperService.wrapperCreate("S108", "토큰 생성 성공", loginResponse);
    }
    private String getIdByToken(String refreshToken){
        Claims claims = jwtUtil.parseJwt(refreshToken);
        return claims.getSubject();
    }

    @Override
    public CustomUserDetails getUserDetails() {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext()
	                                .getAuthentication()
	                                .getPrincipal();
        return user;
    }

    @Override
    public ObjectResponseWrapper<String> logout(CustomUserDetails userDetails) {
        tokenService.deleteRefreshToken(userDetails.getMemberNo());
        return responseWrapperService.wrapperCreate("S109", "로그아웃 성공");
    }
}
