package com.nomnom.onnomnom.auth.model.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nomnom.onnomnom.auth.model.dto.LoginInfo;
import com.nomnom.onnomnom.auth.model.dto.LoginResponseDTO;
import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.member.model.dto.MemberDTO;
import com.nomnom.onnomnom.member.model.service.MemberService;
import com.nomnom.onnomnom.member.model.vo.MemberInsertVo;
import com.nomnom.onnomnom.token.model.dto.TokenDTO;
import com.nomnom.onnomnom.token.model.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoServiceImpl implements KakaoService {

    private static final String baseUrl = "https://kauth.kakao.com/oauth/authorize";
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final TokenService tokenService;

    @Value("${oauth2.kakao.client-id}")
    private String clientId;
    
    @Value("${oauth2.kakao.client-secret}")
    private String clientSecret;

    @Value("${oauth2.kakao.redirect-uri}")
    private String redirectUri;

    private final ResponseWrapperService responseWrapperService;
    @Override
    public ObjectResponseWrapper<Map<String, String>> getKakaoLoginUrl() {
        try {
            // https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&prompt=login
            String kakaoLoginUrl = baseUrl + "?response_type=code" + "&client_id=" + clientId + "&redirect_uri=" + redirectUri + "&prompt=login";
            
            return responseWrapperService.wrapperCreate("S100", "카카오 URL 생성", Map.of("loginUrl", kakaoLoginUrl));
        } catch (Exception e) {
            throw new BaseException(ErrorCode.REQUEST_RESULT_FAILURE);
        }
    }
    @Override
    public LoginResponseDTO getKakaoAcessToken(String code) {
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        // 요청 바디 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        String accessToken;
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            log.info("emailㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ {}", response);
            if (response.getStatusCode() == HttpStatus.OK){
                try {
                    JsonNode jsonNode = objectMapper.readTree(response.getBody());
                    accessToken = jsonNode.get("access_token").asText();
                } catch (JsonProcessingException e) {
                    throw new BaseException(ErrorCode.INVALID_TOKEN, "카카오 토큰 파싱 오류");
                }
            } else {
                throw new BaseException(ErrorCode.INVALID_TOKEN, "카카오 토큰 요청 실패");
            }
        } catch (Exception e){
            throw new BaseException(ErrorCode.INVALID_TOKEN, "카카오 토큰 요청 중 오류 발생");
        }
        return insertKakaoUser(accessToken);

    }

    private LoginResponseDTO insertKakaoUser(String accessToken){
        log.info("emailㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ {}", accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );
            log.info("emailㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ {}", response);
            // 응답 처리
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                
                String id = jsonNode.get("id").asText();
                log.info("tqtqtqtqtqtqtqtqtqtqq id가 널이야? {}", id);
                MemberDTO member = memberService.selectMemberById(id);
                if (member == null){
                    MemberInsertVo memberValue = MemberInsertVo
                                                .builder()
                                                .memberId(id)
                                                .memberPw(passwordEncoder.encode(id))
                                                .memberEmail(id)
                                                .memberName(id)
                                                .memberNickName(id)
                                                .memberRole("ROLE_COMMON")
                                                .build();
                    memberService.insertMember(memberValue, null);
                    member = memberService.selectMemberById(id);
                }
                return kakaoLoginSection(member);
            } else {
                throw new BaseException(ErrorCode.REQUEST_RESULT_FAILURE, "카카오 사용자 정보 요청 실패");
            }
        } catch (JsonProcessingException e) {
            throw new BaseException(ErrorCode.REQUEST_RESULT_FAILURE,"카카오 사용자 정보 파싱 오류: ");
        } catch (Exception e) {
            throw new BaseException(ErrorCode.REQUEST_RESULT_FAILURE,"카카오 사용자 정보 요청 중 오류 발생: ");
        }
    }
    private LoginResponseDTO kakaoLoginSection(MemberDTO member){
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
                                    .isActive(loginMember.getIsActive())
                                    .build())
                .tokens(tokens)
                .build();
        return loginResponse;
    }
}
