package com.nomnom.onnomnom.auth.model.service;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.nomnom.onnomnom.auth.model.dao.AuthMapper;
import com.nomnom.onnomnom.auth.model.dto.LoginInfo;
import com.nomnom.onnomnom.auth.model.dto.LoginResponseDTO;
import com.nomnom.onnomnom.auth.model.dto.MemberLoginDTO;
import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.global.config.util.JwtUtil;
import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;
import com.nomnom.onnomnom.global.exception.CustomAuthenticationException;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.member.model.dto.MemberDTO;
import com.nomnom.onnomnom.member.model.service.MemberService;
import com.nomnom.onnomnom.member.model.vo.MemberInsertVo;
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
    private final PasswordEncoder passwordEncoder;
    private final GoogleOAuthService googleService;
    private static final String TOKEN_URI = "https://oauth2.googleapis.com/token";

     // application.yml 또는 환경변수에서 주입
    @Value("${google.client-id}")
    private String clientId;
    @Value("${google.client-secret}")
    private String clientSecret;
    @Value("${google.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ObjectResponseWrapper<LoginResponseDTO> tokens(MemberLoginDTO memberLoginInfo) {
        Authentication authentication = null;
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
        TokenDTO tokens = tokenService.generateToken(loginMember.getUsername(), loginMember.getMemberNo(), memberLoginInfo.getAuthLogin());
        LoginResponseDTO loginResponse = LoginResponseDTO
                .builder()
                .loginInfo(LoginInfo.builder()
                                    .memberNo(loginMember.getMemberNo())
                                    .username(loginMember.getUsername())
                                    .memberRole(loginMember.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("ROLE_USER"))
                                    .isStoreOwner(loginMember.getIsStoreOwner())
                                    .isModify("Y")
                                    .build())
                .tokens(tokens)
                .build();

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
                                    .isModify("Y")
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

    @Override
    public ObjectResponseWrapper<LoginResponseDTO> googleLogin(Map<String, String> body){
        String code = body.get("code");
        GoogleTokenResponse tokens = googleService.exchangeCodeForTokens(code);
        // String token = body.get("token");
        // log.info("{}", body);
        // log.info("{}", token);
        // GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
        //     new NetHttpTransport(),
        //     GsonFactory.getDefaultInstance()
        // )
        // .setAudience(Collections.singletonList("1070671526490-nke8ohh1a4dc3kg5hesdof5t6cdq8v2j.apps.googleusercontent.com"))
        // .build();
        // log.info("{}", verifier);
        // try{
        //     log.info("트라이 안");
        //     GoogleIdToken idToken = verifier.verify(token);
        //     log.info("{}", idToken);
        //     if (idToken == null){
        //         throw new BaseException(ErrorCode.INVALID_TOKEN,"유효하지 않은 Google 토큰입니다.");
        //     }

        //     Payload payload = idToken.getPayload();

        //     String email = payload.getEmail();
        //     String name = (String) payload.get("name");
        //     String googleSub = payload.getSubject();
        //     log.info("{}", email);
        //     // 기존 회원 확인
        //     MemberDTO member = memberService.selectMemberByEmail(email);
        //     if (member == null){
        //         MemberInsertVo memberValue = MemberInsertVo.builder()
        //             .memberId(email)
        //             .memberPw(passwordEncoder.encode(googleSub))
        //             .memberEmail(email)
        //             .memberName(name)
        //             .memberNickName(name)
        //             .memberRole("ROLE_COMMON")
        //             .build();
        //         memberService.insertMember(memberValue, null);
        //         member = memberService.selectMemberByEmail(email);
        //     }
        //     return responseWrapperService.wrapperCreate("S100", "구글 로그인 성공", googleLoginSection(member));
        // } catch(Exception e) {
        //     throw new CustomAuthenticationException(ErrorCode.DB_INSERT_FAILURE, "구글 로그인 처리 실패");
        // }
    }

    private LoginResponseDTO googleLoginSection(MemberDTO member){
        CustomUserDetails loginMember = CustomUserDetails.builder()
                .memberNo(member.getMemberNo())
                .username(member.getMemberId())
                .password(member.getMemberPw())   // 실제 검사하진 않음
                .isActive(member.getIsActive())
                .isStoreOwner(member.getIsStoreOwner())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(member.getMemberRole())))
                .build();
        // SecurityContext에 직접 인증 정보 등록
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                loginMember,
                null,
                loginMember.getAuthorities()
            );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String isModify = "";
        if (member.getMemberModifiedDate() == null){
            isModify = "N";
        } else {
            isModify = "Y";
        }
        TokenDTO tokens = tokenService.generateToken(loginMember.getUsername(), loginMember.getMemberNo(), "N");
        LoginResponseDTO loginResponse = LoginResponseDTO
                .builder()
                .loginInfo(LoginInfo.builder()
                                    .memberNo(loginMember.getMemberNo())
                                    .username(loginMember.getUsername())
                                    .memberRole(loginMember.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("ROLE_USER"))
                                    .isStoreOwner(loginMember.getIsStoreOwner())
                                    .isModify(isModify)
                                    .build())
                .tokens(tokens)
                .build();
        return loginResponse;
    }
}
