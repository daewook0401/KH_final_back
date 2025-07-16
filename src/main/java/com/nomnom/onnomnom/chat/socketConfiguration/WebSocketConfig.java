package com.nomnom.onnomnom.chat.socketConfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.nomnom.onnomnom.chat.handShakeInterceptor.HandShakeInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer{

	private final WebSocketHandler chatHandler;
	private final HandShakeInterceptor  handShakeInterceptor;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(chatHandler, "/ws/chat/{roomId}")
				.addInterceptors(handShakeInterceptor) 
				.setAllowedOrigins("https://onnomnom.shop");
				//.setAllowedOrigins("http://localhost:5173");
		
	}

	
}
