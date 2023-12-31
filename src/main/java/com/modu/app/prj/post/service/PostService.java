package com.modu.app.prj.post.service;

import java.util.List;


public interface PostService {
	
	//전체조회
	public List<PostVO> getAllPostList(String brdUniNo);
	
	//단건조회
	public PostVO getOnePost(String postUniNo);
	
	//게시글폼게시글등록, 수정폼용 게시판조회
	public PostVO selectOneBoard(String brdUniNo);
	//등록
	public int insertPost(PostVO postVO);
	
	//수정
	public int updatePost(PostVO postVO);
	
	//삭제
	public int deletePost(String postUniNo);
	
	//댓글알림on/off
	public int replyOnOff(PostVO postVO);
	
	//공지등록on/off
	public int notiOnOff(PostVO postVO);
		
	//상단공지리스트
	public List<PostVO> selectAllNotiPost(String brdUniNo);
	
	//멤버호출용리스트
	public List<MembDTO> selectCallMembPub(String prjUniNo);
	public List<MembDTO> selectCallMembNonPub(String brdUniNo);
	
	
}
