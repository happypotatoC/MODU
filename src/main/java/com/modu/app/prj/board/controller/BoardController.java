package com.modu.app.prj.board.controller;

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
import com.modu.app.prj.prj.service.PrjService;
import com.modu.app.prj.prj.service.PrjVO;
import com.modu.app.prj.user.service.UserVO;

@Controller
public class BoardController {

	@Autowired
	BoardService boardService;

	@Autowired
	PrjService prjService;

	// 사이드바 게시판 리스트
	@GetMapping("/main")
	public String BoardList(Model model, PrjVO prjVO, HttpServletRequest request) {
		HttpSession session = request.getSession();
		prjVO.setMembUniNo(((UserVO) session.getAttribute("user")).getMembUniNo());
		PrjVO vo = prjService.prjSession(prjVO);
		session.setAttribute("prjUniNo", vo.getPrjUniNo());
		session.setAttribute("particiMembUniNo", vo.getParticiMembUniNo());
		BoardVO brd = new BoardVO();
		brd.setParticiMembUniNo(vo.getParticiMembUniNo());
		brd.setPrjUniNo(vo.getPrjUniNo());
		model.addAttribute("Brd", boardService.BoardList(brd));
		return "index";
	}

	@PostMapping("InsertBoard")
	@ResponseBody
	public BoardVO InsertBoard(BoardVO vo) {
		String check = vo.getPubcYn();
		if (check.equals("on")) {
			vo.setPubcYn("Y");
		} else {
			vo.setPubcYn("N");
		}
		boardService.InsertBoard(vo);
		return vo;
	}
}
