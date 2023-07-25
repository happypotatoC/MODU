package com.modu.app.prj.vote.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.modu.app.prj.board.service.BoardService;
import com.modu.app.prj.board.service.BoardVO;
import com.modu.app.prj.user.service.UserVO;
import com.modu.app.prj.vote.service.ListModel;
import com.modu.app.prj.vote.service.MyDataModel;
import com.modu.app.prj.vote.service.VoteDetaVO;
import com.modu.app.prj.vote.service.VoteService;
import com.modu.app.prj.vote.service.VoteVO;

// 김동건 07/20 투표관리
@Controller
public class VoteController {
	
	@Autowired
	VoteService voteService;
	
	@Autowired
	BoardService boardService;
	
	
	//투표 페이지 이동
	@GetMapping("vote")
	public String vote(){
		/*
		 * vo.setPrjUniNo((String) session.getAttribute("prjUniNo"));
		 * vo.setParticiMembUniNo((String) session.getAttribute("particiMembUniNo"));
		 * List<VoteVO> list = voteService.voteList(vo); List<VoteDetaVO> count =
		 * voteService.allCount();
		 * 
		 * ListModel listModel = new ListModel(); listModel.setChatrList(list);
		 * listModel.setVoteDataList(count); System.out.println(list);
		 * model.addAttribute("voteList",listModel);
		 * System.out.println(model.getAttribute("voteList"));
		 */
		return "vote/voteList";
	}
	
	//투표 리스트 출력
	  @GetMapping("voteList")
	  @ResponseBody 
	  public Map<String, Object> voteList(VoteVO vo,HttpServletRequest request) { 
	  HttpSession session = request.getSession();
	  vo.setPrjUniNo((String) session.getAttribute("prjUniNo"));
	  vo.setParticiMembUniNo((String) session.getAttribute("particiMembUniNo"));
	  
	  Map<String, Object> map = new HashMap<>();
	  
	  map.put("list",voteService.voteList(vo));
	  map.put("count",voteService.allCount());
	  map.put("grd", voteService.grdCheck((String) session.getAttribute("particiMembUniNo")));
	  System.out.println(map);
	  return map; 
	  }
	 
	
	//투표 등록페이지 이동
	@GetMapping("voteInsert")
	public String todoInsertForm(HttpSession session,Model model){
		
		//내가 참여하고있는 프로젝트의 내가 참여하고있는 게시판 제목과 채팅방 제목들을 나열하기 위한 구문
		BoardVO brd = new BoardVO();
		brd.setPrjUniNo((String) session.getAttribute("prjUniNo"));
		brd.setParticiMembUniNo((String) session.getAttribute("particiMembUniNo"));
		List<BoardVO> boardList = boardService.BoardList(brd);
		List<VoteVO> chatrList=voteService.chatrNm((String) session.getAttribute("particiMembUniNo"));
		
		//model에 한번에 넣는방법을 몰라서 새로운 객체를 만들어서 그 객체 안에 다 넣기
		MyDataModel myDataModel = new MyDataModel();
		myDataModel.setVoteVO(new VoteVO());
		myDataModel.setChatrList(chatrList);
		myDataModel.setBoardList(boardList);
		
		
		model.addAttribute("vote", myDataModel);
		return "vote/voteInsert";
	}
	
	// 투표 등록처리
	@PostMapping("voteInsert")
	@ResponseBody
	public String voteInsert(HttpSession session,@RequestBody VoteVO vote) {
		vote.setParticiMembUniNo((String) session.getAttribute("particiMembUniNo"));
		vote.setPrjUniNo((String) session.getAttribute("prjUniNo"));
		int result = voteService.voteInsert(vote);
		return "success";
	}
	
	
	// 투표 단건(=투표하는 페이지)
	@GetMapping("voteInfo/{voteNo}")
	public String voteInfo(HttpSession session,Model model,VoteVO vo, @PathVariable String voteNo,VoteDetaVO vdvo){
		
		//투표 단건 조회에 필요한 세가지 데이터 넣어주기
		vo.setPrjUniNo((String) session.getAttribute("prjUniNo"));
		vo.setParticiMembUniNo((String) session.getAttribute("particiMembUniNo"));
		if(voteNo != null) {
			vo.setVoteNo(voteNo);
		}
		System.out.println(vo);
		// 모델에 투표 단건조회와 투표항목들 조회하기
		model.addAttribute("voteInfo",voteService.voteOne(vo));
		model.addAttribute("item",voteService.voteItem(voteNo));
		model.addAttribute("maker",voteService.voteMaker(voteNo));
		//이미 투표한 장소인지 확인하기 위해 필요한 데이터 넣기
		vdvo.setParticiMembUniNo((String) session.getAttribute("particiMembUniNo"));
		vdvo.setVoteNo(voteNo);
		System.out.println(model.getAttribute("voteInfo"));
		System.out.println(vdvo);
		if(voteService.whoVote(vdvo) != null) {
			return "vote/voteResult";
		}else {
			return "vote/voteInfo";
		}
		
	}
	
	//투표 등록
	@PostMapping("voteInfo/{voteNo}")
	@ResponseBody
	public VoteDetaVO voteDO(HttpSession session,@RequestBody VoteDetaVO vo,@PathVariable String voteNo){
		vo.setParticiMembUniNo((String) session.getAttribute("particiMembUniNo"));
		vo.setVoteNo(voteNo);
		System.out.println(vo);
		voteService.voteDo(vo);
		//변경 성공 여부
		return vo;
	}

	
// map 여러가지 넘기기 map 공부
//	Map<String, Object> map = new HashMap<>();
//	if(result > 0) {
//		map.put("retCode", "Success");
//		map.put("data", vo);
//	}else {
//		map.put("retCode", "Fail");
//	}
//	return map;	

	
	// 투표 단건
		@GetMapping("voteResult/{voteNo}")
		public String voteInfo2(HttpSession session,Model model,VoteVO vo, @PathVariable String voteNo){
			vo.setPrjUniNo((String) session.getAttribute("prjUniNo"));
			vo.setParticiMembUniNo((String) session.getAttribute("particiMembUniNo"));
			if(voteNo != null) {
				vo.setVoteNo(voteNo);
			}
			model.addAttribute("voteInfo",voteService.voteOne(vo));
			model.addAttribute("item",voteService.voteItem(voteNo));
			System.out.println(model.getAttribute("item"));;
			return "vote/voteResult";
		}
		
	//
	
	// 투표 등록	
//	@ResponseBody
//	@PostMapping("voteInsert")
//	public String voteInsert(VoteVO vo,HttpServletRequest request) {
//		model.addAttribute("VoteVO",vo);
//		HttpSession session = request.getSession();
//		BoardVO brd = new BoardVO();
//		brd.setPrjUniNo((String) session.getAttribute("prjUniNo"));
//		brd.setParticiMembUniNo((String) session.getAttribute("particiMembUniNo"));
//		boardService.BoardList(brd));
//		return "vote/voteInsert";
//	}
}
