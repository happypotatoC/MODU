package com.modu.app.prj.chat.mapper;

import java.util.List;

import com.modu.app.prj.chat.service.ChatChmVO;
import com.modu.app.prj.chat.service.ChatDTO;
import com.modu.app.prj.chat.service.ChatVO;
import com.modu.app.prj.chat.service.ChatrParticiVO;
import com.modu.app.prj.chat.service.ChatrVO;

public interface ChatMapper {
	
	//세션용
	public ChatrParticiVO chatSession(ChatrParticiVO cptvo);
	
	//채팅방만들기
	public int makeChatr (ChatrVO chartVO);
	
	//채팅참여자insert
	public int insertChatMemb(ChatrParticiVO chatParticiVO);
	
	//참여해있는채팅방리스트
	public List<ChatrVO> chatRoomList(String particiMembUniNo);
	
	//채팅메세지insert
	public int insertChat(ChatVO chatVO);
	
	//채팅메세지전체리스트
	public List<ChatDTO> chatMessageList(String chatrNo);
	
	//채팅방나가기
	public int leaveChatr(String chatParticiMembUniNo);
	
	//채팅방조회(채팅방명변경용)
	public ChatrParticiVO selectOneChatr(String chatParticiMembUniNo);
	
	//채팅방명변경
	public int changeChatrNm(ChatrParticiVO chatParticiVO);
	
	//채팅방참여자들리스트
	public List<ChatrParticiVO> chatrParticiList(String chatrNo);
	
	//채팅읽음확인용
	public int insertChatChm(ChatChmVO chatChmVO);
	
	//채팅참여자추가용리스트
	public List<ChatrParticiVO> addChatrParticiList(ChatrParticiVO chatParticiVO);
	
	//채팅읽음업데이트
	public int updateReadChat(ChatChmVO chatChmVO);
	
	//채팅알림ON/OFF
	public int setChatArm(ChatrParticiVO chatParticiVO);

}
