package com.nomnom.onnomnom.chat.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.nomnom.onnomnom.chat.model.dto.ChattingList;
import com.nomnom.onnomnom.chat.model.dto.ChattingRoom;
import com.nomnom.onnomnom.chat.model.vo.ChattingListVo;
import com.nomnom.onnomnom.chat.model.vo.ChattingRoomVo;

@Mapper
public interface ChatMapper {

	ChattingRoom selectRoomNo(String memberNo);
	
	int insertRoomNo(ChattingRoomVo chatRoomVo);
	
	
	int insertChatting(ChattingListVo chatListVo);

	int selectCheckChatting(String roomNo);
	List<ChattingList> selectChatList(String roomNo);

	List<ChattingList> selectAdminChatList();


}
