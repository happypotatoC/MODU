package com.modu.app.prj.sche.service;

import java.util.List;

public interface ScheService {
	//일정 리스트
	public List<ScheVO> scheList(String prj);
	
	//일정 등록
	public String scheInsert(ScheVO vo);
	
	//일정 참가자 등록
	public int scheInsertPartici(ScheVO vo);
	
	//일정 조회
	public ScheVO scheInfo(String sno);
	
	//참가자 조회
	public List<ScheVO> schePartici(String sno);
	
	//일정 삭제
	public int scheDelete(String sno);
	
	//제외 참가자목록
	public List<ScheVO> yetPartici(ScheVO vo);
	
	//일정 업데이트
	public int scheUpdate(ScheVO vo);
	
	//참가자 테이블 비우기
	public int scheParticiDelete(String sno);
}
