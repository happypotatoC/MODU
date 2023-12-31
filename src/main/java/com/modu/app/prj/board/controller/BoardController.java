package com.modu.app.prj.board.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.modu.app.prj.board.service.BoardService;
import com.modu.app.prj.board.service.BoardVO;
import com.modu.app.prj.post.service.PostService;
import com.modu.app.prj.prj.service.PrjService;
import com.modu.app.prj.prj.service.PrjVO;
import com.modu.app.prj.user.service.UserVO;

// 2023-07-20 김성현 
// 게시판 관리
@Controller
public class BoardController {

	@Autowired
	BoardService boardService;

	@Autowired
	PrjService prjService;
	
	@Autowired
	PostService postService;

	// 프로젝트 메인페이지
	@GetMapping("/main")
	public String BoardList(Model model, PrjVO prjVO, HttpServletRequest request) {
		// 프로젝트 참여번호를 세션에 저장
		HttpSession session = request.getSession();
		prjVO.setMembUniNo(((UserVO) session.getAttribute("user")).getMembUniNo());
		PrjVO vo = prjService.getMemInfo(prjVO);
		session.setAttribute("prjUniNo", vo.getPrjUniNo());
		session.setAttribute("particiMembUniNo", vo.getParticiMembUniNo());
		session.setAttribute("grd", vo.getGrd());
		session.setAttribute("armYn", boardService.armYn(vo.getParticiMembUniNo()).getArmYn());
		session.setAttribute("prjMembInfo", vo);
		session.setAttribute("prjName", (prjService.getPrjInfo(vo.getPrjUniNo())).getPrjNm());
		return "redirect:scheList";
	}

	// 게시판 등록
	@PostMapping("InsertBoardBm")
	@ResponseBody
	public BoardVO InsertBoard(BoardVO vo, HttpSession session) {
		String check = vo.getPubcYn();
		if (check.equals("on")) {
			vo.setPubcYn("Y");
		} else {
			vo.setPubcYn("N");
		}
		String prjNo = (String) session.getAttribute("prjUniNo");
		String particiMembUniNo = (String) session.getAttribute("particiMembUniNo");

		if ((boardService.BrdCount(prjNo) < 4 && prjService.getPrjInfo(prjNo).getExdt() == null) || prjService.getPrjInfo(prjNo).getExdt() != null) {
			vo.setParticiMembUniNo(particiMembUniNo);
			vo.setPrjUniNo(prjNo);
			boardService.InsertBoard(vo);
		}else {
			vo.setPrjUniNo(null);
		}
		return vo;
	}

	// 게시판 리스트
	@GetMapping("boardList")
	public String BoardList(Model model, HttpSession session) {
		// 사이드바 게시판 리스트
		BoardVO brd = new BoardVO();
		brd.setParticiMembUniNo((String) session.getAttribute("particiMembUniNo"));
		brd.setPrjUniNo((String) session.getAttribute("prjUniNo"));
		model.addAttribute("Brd", boardService.BoardList(brd));
		return "boardLIst/boardList";
	}

	// 게시판 삭제
	@PostMapping("BoardDelete")
	@ResponseBody
	public String boardDelete(BoardVO vo) {
		boardService.DeleteBoard(vo.getBrdUniNo());
		return vo.getBrdUniNo();
	}

	// 게시판 업데이트
	@PostMapping("BrdUpdate")
	@ResponseBody
	public BoardVO BrdUpdate(BoardVO vo) {
		String check1 = vo.getPubcYn();
		if (check1.equals("on")) {
			vo.setPubcYn("Y");
		} else {
			vo.setPubcYn("N");
		}
		boardService.BrdUpdate(vo);
		return vo;
	}

	// 해당 프로젝트 참여자 리스트 조회 후 참여된 회원에게 알람 발송을 위한 컨트롤
	@GetMapping("prjParticiMembList")
	@ResponseBody
	public List<BoardVO> prjList(BoardVO vo, HttpSession session) {
		String prjUniNo = (String) session.getAttribute("prjUniNo");
		vo.setPrjUniNo(prjUniNo);
		boardService.prjList(vo);
		return boardService.prjList(vo);
	}

	@PostMapping("newJeans")
	@ResponseBody
	public int newJeans(@RequestBody BoardVO vo, HttpSession session) {
		int result = 0;
		if (vo.getParticiMembUniNos() != null) {
			List<String> membList = vo.getParticiMembUniNos();
			for (String memb : membList) {
				vo.setParticiMembUniNo(memb);
				result = boardService.newJeans(vo);
			}
		}
		return result;
	}
	
	@PostMapping("POSTLIST")
	@ResponseBody
	public List<BoardVO> POSTLIST(@RequestBody BoardVO vo, HttpSession session){
		vo.setParticiMembUniNo((String) session.getAttribute("particiMembUniNo"));
		return boardService.POSTLIST(vo);
	}
	
	@GetMapping("superShy")
	public String superShy(BoardVO vo, Model model){
		List<BoardVO> list = boardService.superShy(vo);
		model.addAttribute("superShy",list);
		return "/post/postList";
	}
	
	// 게시판 업데이트
		@PostMapping("armYn")
		@ResponseBody
		public BoardVO armYn(@RequestBody BoardVO vo, HttpSession session) {
			vo.setParticiMembUniNo((String) session.getAttribute("particiMembUniNo"));
			String yn = vo.getArmYn();
			if (yn.equals("Y")) {
				vo.setPubcYn("N");
			} else {
				vo.setPubcYn("N");
			}
			boardService.particiArm(vo);
			return vo;
		}
}
