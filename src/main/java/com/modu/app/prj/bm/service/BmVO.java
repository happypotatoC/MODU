package com.modu.app.prj.bm.service;


import java.util.Date;

import lombok.Data;

@Data
public class BmVO {
	

//	BM_UNI_NO           NOT NULL VARCHAR2(10) 
//	REG_DT              NOT NULL DATE         
//	PARTICI_MEMB_UNI_NO NOT NULL VARCHAR2(10) 
//	POST_UNI_NO                  VARCHAR2(10) 
//	ATT_NO                       VARCHAR2(10) 
//	CHAT_NO                      NUMBER  
	
	//즐찾번호
	private String bmUniNo;
	
	//즐찾등록날짜
	private Date regDt;
	
	//참여자 버노
	private String particiMembUniNo;
	
	//메세지 버노
	private String postUniNo;
	
	//파일 버노
	private String attNo;
	
	//채팅 버노
	private String chatNo;
	
	
	
//	BM                  NOT NULL CHAR(1)      
//	ARM_YN              NOT NULL CHAR(1)      
//	BRD_ARM_UNI_NO      NOT NULL VARCHAR2(10) 
//	BRD_UNI_NO                   VARCHAR2(10) 
//	PARTICI_MEMB_UNI_NO          VARCHAR2(10) 
	
	// 즐겨찾기
	private String bm;
	
	// 알림 여부
	private String armYn;
	
	//참여자 버노
	private String brdArmUniNo;
	
	//asdasd
	private String brdUniNo;
	
}
