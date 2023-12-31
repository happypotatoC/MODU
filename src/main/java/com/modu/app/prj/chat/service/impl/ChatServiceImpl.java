package com.modu.app.prj.chat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.modu.app.prj.chat.mapper.ChatMapper;
import com.modu.app.prj.chat.service.ChatChmVO;
import com.modu.app.prj.chat.service.ChatDTO;
import com.modu.app.prj.chat.service.ChatService;
import com.modu.app.prj.chat.service.ChatVO;
import com.modu.app.prj.chat.service.ChatrParticiVO;
import com.modu.app.prj.chat.service.ChatrVO;

@Service
public class ChatServiceImpl implements ChatService{
	
	@Autowired
	ChatMapper chatMapper;

	//채팅방만들기
	@Override
	public int makeChatr(ChatrVO chartVO) {
		return chatMapper.makeChatr(chartVO);
	}

	//채팅방참여자INSERT
	@Override
	public int insertChatMemb(ChatrParticiVO chatParticiVO) {
		return chatMapper.insertChatMemb(chatParticiVO);
	}

	//현재참여중인채팅방리스트
	@Override
	public List<ChatrVO> chatRoomList(String particiMembUniNo) {
		return chatMapper.chatRoomList(particiMembUniNo);
	}

	//세션용
	@Override
	public ChatrParticiVO chatSession(ChatrParticiVO cptvo) {
		return chatMapper.chatSession(cptvo);
	}
	
	//채팅메세지insert
	@Override
	public int insertChat(ChatVO chatVO) {
		chatMapper.insertChat(chatVO);
		return chatVO.getChatNo();
	}

	//채팅메세지리스트
	@Override
	public List<ChatDTO> chatMessageList(String chatrNo) {
		return chatMapper.chatMessageList(chatrNo);
	}
	
	//채팅방나가기
	@Override
	public int leaveChatr(String chatParticiMem) {
		return chatMapper.leaveChatr(chatParticiMem);
	}

	//채팅방수정용
	@Override
	public ChatrParticiVO selectOneChatr(String chatParticiMembUniNo) {
		return chatMapper.selectOneChatr(chatParticiMembUniNo);
	}
	
	//채팅방명변경
	@Override
	public int changeChatrNm(ChatrParticiVO chatParticiVO) {
		return chatMapper.changeChatrNm(chatParticiVO);
	}

	//채팅방참여자들리스트
	@Override
	public List<ChatrParticiVO> chatrParticiList(String chatrNo) {
		return chatMapper.chatrParticiList(chatrNo);
	}
	
	//채팅읽음확인용INSERT
	@Override
	public int insertChatChm(ChatChmVO chatChmVO) {
		return chatMapper.insertChatChm(chatChmVO);
	}
	
	//채팅참여자추가용리스트
	@Override
	public List<ChatrParticiVO> addChatrParticiList(ChatrParticiVO chatParticiVO) {
		return chatMapper.addChatrParticiList(chatParticiVO);
	}
	
	//채팅읽음업데이트
	@Override
	public int updateReadChat(ChatChmVO chatChmVO) {
		return chatMapper.updateReadChat(chatChmVO);
	}
	
	//채팅알림ON/OFF
	@Override
	public int setChatArm(ChatrParticiVO chatParticiVO) {
		return chatMapper.setChatArm(chatParticiVO);
	}

}
