package com.modu.app.prj.board.mapper;

import java.util.List;

import com.modu.app.prj.board.service.BoardVO;

public interface BoardMapper {

	// 게시판 리스트
	public List<BoardVO> BoardList(BoardVO vo);

	// 게시판 추가
	public int InsertBoard(BoardVO vo);

	// 게시판 삭제
	public String DeleteBoard(String brdUniNo);

	// 게시판 수정
	public int BrdUpdate(BoardVO vo);
	
	//게시판 이름으로 리스트 찾기 - PostControl에 쓰일 List
	public List<BoardVO> brdNm(BoardVO vo);
	
	//무료 게시판이면 Count 해서 게시판 4개까지 생성 가능
	public int BrdCount(String prjNo);
	
	// 게시글 알림 : 현재 참여한 프로젝트 내에 회원에게 알람 발송
	public List<BoardVO> prjList(BoardVO vo);
	
	//채팅 알림 : 현재 참여한 프로젝트 내 채팅방 참여 회원에게 알림 발송
	public List<BoardVO> chatParticiMemb(BoardVO vo);
	
	//비공개 게시판에 참여 돼 있는 회원 제외 결과
	public List<BoardVO> particiBrd(BoardVO vo);
	
	//비공개 게시판 초대
	public int newJeans(BoardVO vo);
	
	//게시글 리스트
	public List<BoardVO> POSTLIST(BoardVO vo);
	
	//비공개 게시판 초대된 회원 리스트
	public List<BoardVO> superShy(BoardVO vo);
	
	//전체 알람 여부
	public BoardVO armYn(String particiMembUniNo);
	
	//알람 Y , N 업데이트
	public int particiArm(BoardVO vo);
	

}
