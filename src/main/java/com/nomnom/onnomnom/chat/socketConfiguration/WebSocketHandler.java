package com.nomnom.onnomnom.chat.socketConfiguration;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.chat.model.dao.ChatMapper;
import com.nomnom.onnomnom.chat.model.dto.ChattingList;
import com.nomnom.onnomnom.chat.model.dto.ResponseChattingList;
import com.nomnom.onnomnom.chat.model.vo.ChattingListVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

	private final Map<String, Map<String, WebSocketSession>> rooms = new ConcurrentHashMap<>();
	private final ChatMapper chatMapper;
	ObjectMapper objectMapper = new ObjectMapper();
	
	/*
	 * 연결될 때마다 이 메서드 호출
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("전화받음요");
		System.out.println(session);
		
		String roomNo = getRoomNo(session);
		CustomUserDetails loginUser = (CustomUserDetails) session.getAttributes().get("principal");
	    String memberNo = loginUser.getMemberNo();
	    log.info("memberNo : {}",memberNo);

	    Map<String, WebSocketSession> userSessions = rooms.computeIfAbsent(roomNo, r -> new ConcurrentHashMap<>());
	    WebSocketSession oldSession = userSessions.put(memberNo, session);
		
	}


	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		System.out.println("메시지 송신자 : " + session);
		System.out.println("수신된 메시지 : " + message);
		String roomNo = getRoomNo(session);
		if(roomNo == null) return;
		
		Map<String, WebSocketSession> userSessions = rooms.get(roomNo);
	    if (userSessions == null) return;
	    
	    CustomUserDetails loginUser = (CustomUserDetails) session.getAttributes().get("principal");
	    String memberNo = loginUser.getMemberNo();
	    
		//JSON형태로 들어온 걸 ChattingList객체형태로 바꿈
		ChattingList chatMessage = objectMapper.readValue(message.getPayload(), ChattingList.class);
		
		ChattingListVo chatListVo = ChattingListVo.builder()
										.roomNo(roomNo)
										.memberNo(memberNo)
										.content(chatMessage.getContent())
										.build();
		log.info("chatListVo : {} ",chatListVo);
		// 메세지 저장
		chatMapper.insertChatting(chatListVo);
		
		// 나와 같은방인 친구들에게만 뿌리기 (브로드캐스트)
		//Set<WebSocketSession> currentRoom = rooms.get(roomId);
		for (WebSocketSession userSess : userSessions.values()) {
	        if (!userSess.isOpen()) continue;

	        CustomUserDetails receiver = (CustomUserDetails) userSess.getAttributes().get("principal");
	        String receiverMemberNo = receiver.getMemberNo();
	        boolean isMineForThisClient = (receiverMemberNo == memberNo);  // 변경된 부분

	        ResponseChattingList responseList = new ResponseChattingList();
	        responseList.setRoomNo(roomNo);
	        responseList.setMemberNo(memberNo);
	        responseList.setContent(chatMessage.getContent());
	        responseList.setMine(isMineForThisClient);
	

	        // 6) 해당 세션에만 보내기
	        userSess.sendMessage(new TextMessage(objectMapper.writeValueAsString(responseList)));  // 변경된 부분
	        userSess.sendMessage(new TextMessage(objectMapper.writeValueAsString(responseList)));  // 변경된 부분
	    }
//		// Message 객체를 JSON 형태로 바꿔서 뿌리기 
//		TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage));
//		
//		for(WebSocketSession user: rooms.getOrDefault(roomNo, Collections.emptySet())) {
//			if(user.isOpen()) {
//				user.sendMessage(textMessage);
//			}
//		}
//		
//		for(WebSocketSession user: rooms.getOrDefault("adminRoom", Collections.emptySet())) {
//			if(user.isOpen()) {
//				user.sendMessage(textMessage);
//			}
			
		}
		
	
	
	/*
	 * 연결 종료 될 때마다 이 메서드 홏룰
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("전화 끊었습니다.");
		String roomNo = getRoomNo(session);
	    if (roomNo == null || roomNo.isEmpty()) return;
	    
	    Map<String, WebSocketSession> userSessions = rooms.get(roomNo);
	    if (userSessions == null) return;

        String memberNo = ((CustomUserDetails) session.getAttributes().get("principal")).getMemberNo();
        WebSocketSession mapped = userSessions.get(memberNo);
		
        if (mapped != null && mapped.getId().equals(session.getId())) {
            userSessions.remove(memberNo);
            if (userSessions.isEmpty()) {
                rooms.remove(roomNo);
            }
        }
        
	}

	// 접속한 사용자의 uri에서 접속한 방번호를 추출
	private String getRoomNo(WebSocketSession session) {
		String path = session.getUri().getPath();
		String[] paths = path.split("/");
		String roomNo = "";
		if(paths.length >=4) {
		   return roomNo = paths[3];
		}
		return "";
	}
}
