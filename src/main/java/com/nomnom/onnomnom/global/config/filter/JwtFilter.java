package com.nomnom.onnomnom.global.config.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.global.config.util.JwtUtil;
import com.nomnom.onnomnom.token.model.dto.TokenDTO;
import com.nomnom.onnomnom.token.model.service.TokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter{

    private final JwtUtil util;
    private final TokenService tokenService;
    private final UserDetailsService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = null;
        if (authorization != null && authorization.startsWith("Bearer ")){
            token = authorization.substring(7);
        }

        try {
            if (token != null) {
                Claims claims = util.parseJwt(token);   // Access Token 검증
                String username = claims.getSubject();

                CustomUserDetails user = (CustomUserDetails) userService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            log.info("Access Token 만료: {}", e.getMessage());

            // 1) Refresh Token 꺼내기 (예: "Refresh-Token" 헤더 or 쿠키)
            String refreshToken = extractRefreshToken(request);
            if (refreshToken != null && util.validateRefreshToken(refreshToken) ) {
                // 2) 리프레시 토큰에서 사용자 정보 추출
                String username = util.parseJwt(refreshToken).getSubject();
                CustomUserDetails user = (CustomUserDetails) userService.loadUserByUsername(username);

                // 3) 새 Access Token 발급
                TokenDTO tokens = tokenService.refreshToken(refreshToken);

                // 4) 응답 헤더에 담아주기
                response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken());
                // response.setHeader("Refresh-Token", newRefreshToken);

                // 5) SecurityContext 업데이트
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {
                // Refresh Token도 없거나 유효하지 않으면 에러 응답
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Refresh Token이 유효하지 않습니다.");
                return;
            }
        } catch (JwtException e) {
            log.warn("잘못된 토큰: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("유효하지 않은 토큰입니다.");
            return;
        }
        filterChain.doFilter(request, response);
    }
    private String extractRefreshToken(HttpServletRequest request) {
    // 1) 헤더 우선
        String refreshToken = request.getHeader("Refresh-Token");
        if (StringUtils.hasText(refreshToken)) {
            return refreshToken;
        }
    // 2) 쿠키에서 찾기
    if (request.getCookies() != null) {
        for (Cookie cookie : request.getCookies()) {
            if ("Refresh-Token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
    }
    return null;
}
}
