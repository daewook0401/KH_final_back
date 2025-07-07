package com.nomnom.onnomnom.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.chat.model.dto.ChattingList;
import com.nomnom.onnomnom.chat.model.dto.ChattingRoom;
import com.nomnom.onnomnom.chat.model.service.ChatService;
import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/chatting")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
	
	private final ChatService chatService;
	
	@GetMapping("/roomNo")
	public ResponseEntity<ObjectResponseWrapper<ChattingRoom>> selectRoomoNo() {
		return ResponseEntity.status(HttpStatus.CREATED).body(chatService.selectRoomoNo());
	}
	
	@GetMapping("{roomNo}")
	public ResponseEntity<ListResponseWrapper<ChattingList>> selectChatList(@PathVariable("roomNo") String roomNo) {
		log.info("roomNo:{}",roomNo);
		return ResponseEntity.status(HttpStatus.CREATED).body(chatService.selectChatList(roomNo));
	}
	
	@GetMapping("/admin/list")
	public ResponseEntity<ListResponseWrapper<ChattingList>> selectAdminChatList() {
		return ResponseEntity.status(HttpStatus.CREATED).body(chatService.selectAdminChatList());
	}

}
