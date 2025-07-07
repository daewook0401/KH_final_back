package com.nomnom.onnomnom.chat.model.service;

import com.nomnom.onnomnom.chat.model.dto.ChattingList;
import com.nomnom.onnomnom.chat.model.dto.ChattingRoom;
import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;

public interface ChatService {

	ObjectResponseWrapper<ChattingRoom> selectRoomoNo();
	
	ListResponseWrapper<ChattingList> selectChatList(String roomNo);

	ListResponseWrapper<ChattingList> selectAdminChatList();


	
}
