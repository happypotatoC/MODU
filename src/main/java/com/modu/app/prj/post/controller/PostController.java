package com.modu.app.prj.post.controller;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.modu.app.prj.bm.service.BmService;
import com.modu.app.prj.bm.service.BmVO;
import com.modu.app.prj.board.service.BoardService;
import com.modu.app.prj.board.service.BoardVO;
import com.modu.app.prj.file.service.FileService;
import com.modu.app.prj.file.service.FileVO;
import com.modu.app.prj.post.service.PostService;
import com.modu.app.prj.post.service.PostVO;
// 230707 김자영 post

@Controller
public class PostController {

	@Autowired
	PostService postService;

	@Autowired
	FileService fileService; //첨부파일용
	
	@Autowired
	BmService bmService;

	@Autowired
	BoardService boardService;
	
	@Autowired
	SimpMessagingTemplate messagingTemplate;
	
	// 전체조회페이지이동
	@GetMapping("postList")
	public String postList(Model model, String brdUniNo, HttpSession session) {
		model.addAttribute("brdUniNo", brdUniNo);
		model.addAttribute("postList", postService.getAllPostList(brdUniNo));
		
		BmVO vo = new BmVO();
		vo.setParticiMembUniNo((String) session.getAttribute("particiMembUniNo"));
		model.addAttribute("postbm", bmService.PostBmList(vo));
		
		BoardVO vo1 = new BoardVO();
		vo1.setPrjUniNo((String) session.getAttribute("prjUniNo"));
		vo1.setBrdUniNo(brdUniNo);
		List<BoardVO> list = boardService.superShy(vo1);
		List<BoardVO> list2 = boardService.BoardList(vo1);
		model.addAttribute("superShy",list);
		model.addAttribute("aaa",list2);
		model.addAttribute("checkNM", boardService.brdNm(vo1));
		model.addAttribute("particiBrd",boardService.particiBrd(vo1));
		return "post/postList";
	}

	// 단건조회
	@GetMapping("postSelect")
	public String onePost(Model model, String postUniNo) {
		model.addAttribute("post", postService.getOnePost(postUniNo));
		return "post/postInsert";
	}

	// 등록페이지
	@GetMapping("postInsert")
	public String postInsertForm(Model model, String brdUniNo, HttpSession session) {
		
		//게시판정보조회
		PostVO postvo = postService.selectOneBoard(brdUniNo);
		model.addAttribute("post", postvo);

		// 게시판공개여부에 따라 멤버조회
		char isPub = postvo.getPubcYn();
		if (isPub == 'Y') {
			String prjUniNo = (String) session.getAttribute("prjUniNo");
			model.addAttribute("membList", postService.selectCallMembPub(prjUniNo));
		} else if (isPub == 'N') {
			model.addAttribute("membList", postService.selectCallMembNonPub(brdUniNo));
		}

		return "post/postInsert";
	}

	// 등록처리 
	@PostMapping("postInsert")
	public String postInsert(PostVO postVO, HttpSession session, MultipartFile[] file) {
		
		String particiMembUniNo = (String) session.getAttribute("particiMembUniNo");
		postVO.setParticiMembUniNo(particiMembUniNo);
		System.out.println(postVO);
		
		String tagArm="";
		for(String temp:postVO.getPostTagArmList()) {
			tagArm+=temp+",";
			String memno= temp.substring(0, temp.indexOf("-")-1);
			messagingTemplate.convertAndSend("/subArm/tag/" + memno, "ㄻㅇㄴㄹ");
		}
		postVO.setPostTagArm(tagArm);
		//게시글등록
		postService.insertPost(postVO);
		//첨부파일등록
		FileVO fileVO = new FileVO();
		fileVO.setPostUniNo(postVO.getPostUniNo());
		fileVO.setParticiMembUniNo(particiMembUniNo);
		fileService.insertFile(file, fileVO);
			
		return "redirect:/postList?brdUniNo=" + postVO.getBrdUniNo();
	}
	

	// 수정페이지
	@GetMapping("postUpdate")
	public String postUpdateForm(Model model, String postUniNo) {
		PostVO post = postService.getOnePost(postUniNo);
		FileVO file = new FileVO();
		file.setPostUniNo(postUniNo);
		
		model.addAttribute("post", post); //게시글단건조회
		model.addAttribute("attList", fileService.fileList(file)); //게시글번호로첨부파일리스트
		return "post/postUpdate";
	}

	// 수정처리
	@PostMapping("postUpdate")
	public String postUpdate(PostVO postVO, MultipartFile[] file) {
		
		postService.updatePost(postVO);
		
		PostVO upPost = postService.getOnePost(postVO.getPostUniNo());//업데이트 된 게시글 단건조회
	
		//첨부파일추가등록
		FileVO fileVO = new FileVO();
		fileVO.setPostUniNo(upPost.getPostUniNo());
		fileVO.setParticiMembUniNo(upPost.getParticiMembUniNo());
		
		fileService.insertFile(file, fileVO); 
		
		return "redirect:/postList?brdUniNo=" + postVO.getBrdUniNo();
	}
}
