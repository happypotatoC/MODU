package com.modu.app.prj.vote.mapper;

import java.util.Date;
import java.util.List;

import com.modu.app.prj.vote.service.VoteDetaVO;
import com.modu.app.prj.vote.service.VoteVO;

// 2023/07/21 김동건
public interface VoteMapper {
	
	
	
	 
	//투표 생성하기 
	public int voteInsert(VoteVO vo);
	
	//투표 리스트
	public List<VoteVO>voteList(VoteVO vo);
	
	//투표 단건 조회(투표 하기폼)
	public VoteVO oneVote(VoteVO vo);
	
	//투표 항목 나열
	public List<VoteDetaVO> voteItem(String vno);
	
	//투표하기 
	public int voteDo(VoteDetaVO vo);
	
	//재투표방지
	public VoteDetaVO whoVote(VoteDetaVO vo);
	
	//투표 제작자
	public VoteVO voteMaker(String vid);
	
	//투표 날짜 확인
	public Date toDtCheck(String vid);
	
	//투표 기간 수정
	public int voteDate(VoteVO voteVo);	
	
	//투표결과
	public List<VoteVO> voteResult();
	
	//등급조회
	public VoteDetaVO grdCheck(String partici);
	
	//모든 투표 참여자
	public List<VoteDetaVO> allCount(String partici);
	
	//투표 참여자수
	public VoteDetaVO voteCount(String no);
	
	//투표번호 채팅방 이름찾기
	public VoteVO voteChatr(String voteNo);
	
	//투표번호 게시판 이름 찾기
	public VoteVO voteBrdNm(String voteNo);
	
	//채팅방 제목
	public List<VoteVO> chatrNm(String no);
	
	//게시판 제목
	public List<VoteVO> brdNm(String no);
	
	//투표 삭제
	public int voteDelete(String vno);
	
	
}
