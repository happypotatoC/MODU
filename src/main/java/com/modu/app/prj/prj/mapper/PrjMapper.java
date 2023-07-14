package com.modu.app.prj.prj.mapper;

import java.util.List;

import com.modu.app.prj.prj.service.PrjVO;

public interface PrjMapper {
	// 프로젝트 생성
	public int insertPrj(PrjVO prjVO);

	// 프로젝트 리스트 조회
	public List<PrjVO> selectPrjList(String membUniNo);
	
	public PrjVO prjSession(PrjVO vo);
}