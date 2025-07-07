package com.nomnom.onnomnom.chat.handShakeInterceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import com.nomnom.onnomnom.global.config.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HandShakeInterceptor implements HandshakeInterceptor{
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attrs) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        /* 1) HTTP 헤더로 인증이 안돼 있으면 쿼리스트링 토큰 파싱 */
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
        	System.out.println("여기 안들어옴???");
            String token = UriComponentsBuilder
                    .fromUri(request.getURI())
                    .build()
                    .getQueryParams()
                    .getFirst("token");                // ws://…?token=JWT

            if (token == null) {
                log.warn("WebSocket connect rejected : token missing");
                return false;                         // 핸드셰이크 거절
            }

            try {
                Claims claims = jwtUtil.parseJwt(token);      // 서명·만료 검증
                String userId = claims.getSubject();

                UserDetails user = userDetailsService.loadUserByUsername(userId);

                auth = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (JwtException | IllegalArgumentException e) {
                log.warn("Invalid WS JWT : {}", e.getMessage());
                return false;
            }
        }
        /* 2) 인증객체를 WebSocketSession.attributes 에 복사 */
        attrs.put("principal", auth.getPrincipal());   // NwUserDetails
        attrs.put("userId",   auth.getName());         // 편의용
        return true;   // 핸드셰이크 계속
    }

    @Override
    public void afterHandshake(ServerHttpRequest req, ServerHttpResponse res,
                               WebSocketHandler wsHandler, Exception ex) {
        /* nothing */
    }
}
