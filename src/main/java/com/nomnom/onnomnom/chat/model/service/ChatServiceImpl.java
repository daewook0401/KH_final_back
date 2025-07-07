package com.nomnom.onnomnom.chat.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nomnom.onnomnom.auth.model.service.AuthService;
import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.chat.model.dao.ChatMapper;
import com.nomnom.onnomnom.chat.model.dto.ChattingList;
import com.nomnom.onnomnom.chat.model.dto.ChattingRoom;
import com.nomnom.onnomnom.chat.model.vo.ChattingRoomVo;
import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {
	
	private final AuthService authService;
	private final ChatMapper chatMapper;
	private final ResponseWrapperService responseWrapperService;

	@Override
	public ObjectResponseWrapper<ChattingRoom> selectRoomoNo() {
		CustomUserDetails memeber = authService.getUserDetails();
		String memberNo = memeber.getMemberNo();
		
		ChattingRoom chatRoom = chatMapper.selectRoomNo(memberNo);
		if(chatRoom == null) {
			ChattingRoomVo chatRoomVo = ChattingRoomVo.builder()
											.memberNo(memberNo)
											.build();
			int result = chatMapper.insertRoomNo(chatRoomVo);
			ChattingRoom reponseChatRoom = new ChattingRoom();
			reponseChatRoom.setMemberNo(chatRoomVo.getMemberNo());
			reponseChatRoom.setMemberNo(chatRoomVo.getRoomNo());
			log.info("reponseChatRoom:{}",reponseChatRoom);
			return responseWrapperService.wrapperCreate("S101", "roomNo 조회 성공",reponseChatRoom);
		}
		
		return responseWrapperService.wrapperCreate("S101", "roomNo 조회 성공",chatRoom);
	}

	@Override
	public ListResponseWrapper<ChattingList> selectChatList(String roomNo) {
		
		CustomUserDetails memeber = authService.getUserDetails();
		String memberNo = memeber.getMemberNo();
		
		int result = chatMapper.selectCheckChatting(roomNo);
		
		log.info("result : {}",result);
		List<ChattingList> chatList = chatMapper.selectChatList(roomNo);
	    for (ChattingList chat : chatList) {
	        if (memberNo.equals(chat.getMemberNo())) {
	            chat.setMine(true);
	        } else {
	            chat.setMine(false);
	        }
	    }
		return responseWrapperService.wrapperCreate("S101", "채팅내역 조회 성공",chatList);
	}

	@Override
	public ListResponseWrapper<ChattingList> selectAdminChatList() {
		List<ChattingList> chatList = chatMapper.selectAdminChatList();
		return responseWrapperService.wrapperCreate("S101", "관리자 전체 채팅내역 조회 성공",chatList);
	}

	
}
