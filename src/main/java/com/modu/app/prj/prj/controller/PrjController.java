package com.modu.app.prj.prj.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.modu.app.prj.pay.service.PayService;
import com.modu.app.prj.prj.service.PrjService;
import com.modu.app.prj.prj.service.PrjVO;
import com.modu.app.prj.user.controller.SendEmail;
import com.modu.app.prj.user.service.UserService;
import com.modu.app.prj.user.service.UserVO;

// 2023-07-20 하수민 프로젝트 관리
@Controller
public class PrjController {
	
	@Autowired
	PrjService prjService;
	@Autowired
	PayService payService;
	@Autowired
	UserService userService;
	
	//프로젝트 생성 페이지 이동
	@GetMapping("prjInsert")
	public String prjInsertForm(Model model) {
		model.addAttribute("prjVO", new PrjVO());
		return "prj/prjInsert";
	}
	
	// 프로젝트 생성
	@PostMapping("prjInsert")
	public String prjInsert(PrjVO prjVO, HttpSession session) {
		UserVO vo = (UserVO) session.getAttribute("user");
		prjVO.setMembUniNo(vo.getMembUniNo());
		prjService.insertPrj(prjVO);
		return "redirect:prjList";
	}
	
	// 프로젝트 리스트 페이지
	@GetMapping("prjList")
	public String prjList(Model model, HttpSession session) {
		UserVO vo = (UserVO) session.getAttribute("user");
		model.addAttribute("prjList",prjService.getPrjList(vo.getMembUniNo()));
	    Integer inviterst = (Integer) session.getAttribute("inviterst");
	    if (session.getAttribute("inviterst") != null) {
	        // inviterst 값을 사용하여 원하는 동작 수행
	        model.addAttribute("inviterst", inviterst);
	        session.removeAttribute("inviterst");
	    }

		return "prj/prjList";
	}

	// 프로젝트 관리(프로젝트 참여회원)
	@GetMapping("prjManage")
	public String prjList(Model model, PrjVO prjVO, HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVO user = (UserVO) session.getAttribute("user");
		
		//로그인한 사람의 프로젝트 내 등급 확인(프로젝트  관리페이지는 관리자와 생성자만)
		PrjVO info = new PrjVO();
		info.setMembUniNo(user.getMembUniNo());
		info.setPrjUniNo(prjVO.getPrjUniNo());
		info = prjService.getMemInfo(info);
		// 프로젝트 정보 담기
		prjVO = prjService.getPrjInfo(prjVO.getPrjUniNo());
		if(info == null) {
			return "redirect:prjList";
		}else {
			if(info.getCd().equals("나무") || info.getCd().equals("농부")) {
				model.addAttribute("prjNo", prjVO.getPrjUniNo());
				model.addAttribute("prjNm", prjVO.getPrjNm());
				return "prj/prjManage";			
			}else {
				return "redirect:prjList";
			}			
		}
	}
	
	//프로젝트관리페이지-팀원관리
	@GetMapping("prjManage2")
	public String prjList2(PrjVO prjVO, Model model) {
		model.addAttribute("prjNo", prjVO.getPrjUniNo());
		return "prj/prjMembManage";			
	}
	
	//프로젝트관리페이지-프로젝트 관리
	@GetMapping("prjUpdate")
	public String prjUpdate(Model model, PrjVO prjVO) {		
		return "prj/prjUpdate";
	}
	
	//프로젝트관리페이지-결제관리
	@GetMapping("prjPay")
	public String prjPay(PrjVO prjVO, Model model) {	
		// 프로젝트 관련 정보 prjVO에 담기
		prjVO = prjService.getPrjInfo(prjVO.getPrjUniNo());
		model.addAttribute("prj", prjVO);
		
		// 프로젝트 결제 내역
		model.addAttribute("payList", payService.prjPayList(prjVO.getPrjUniNo()));
		return "prj/prjPay";
	}
	
	// 프로젝트 관리 - 대시보드
	@GetMapping("prjDash")
	public String prjDash(String prjUniNo, Model model) {
		model.addAttribute("memCnt", prjService.getPrjMemCnt(prjUniNo));
		model.addAttribute("brdCnt", prjService.getBrdCnt(prjUniNo));
		model.addAttribute("chatrCnt", prjService.getChatrCnt(prjUniNo));
		model.addAttribute("prjNo", prjUniNo);
		return "prj/prjDash";
	}
	
	// 프로젝트 초대
	@PostMapping("invitePrj")
	@ResponseBody
	public String invite(@RequestBody PrjVO prj, HttpSession session, HttpServletRequest request) {
		prj.setPrjUniNo((String) session.getAttribute("prjUniNo"));
		// 토큰 생성
		String token = userService.generateRandomToken();
		prj.setTk(token);
		
		// 초대테이블 insert
		String id = prjService.insertInvite(prj);
		prj.setId(id);
		// 이메일 보내기
		String siteURL = request.getRequestURL().toString().replace(request.getServletPath(), "");
		SendEmail.inviteSend(prj, siteURL);		
		return "0";
	}
	
	// 초대링크로 들어왔을때
	@GetMapping("/invite")
	public String getInviteTk(@RequestParam("token") String token, 
			HttpSession session, Model model) {
		PrjVO invite = prjService.selectInvite(token);
		UserVO user = (UserVO)session.getAttribute("user");
		
		if(invite == null) {
			model.addAttribute("msg", "유효하지 않는 초대링크");
			return "prj/invite";
		}else if("Y".equals(invite.getCk())) {
			model.addAttribute("msg", "이미 사용한 링크");
			return "prj/invite";
		}else {
			// 유효한 링크
			// 유효하면 그 프로젝트에 지금 몇명인지
			session.setAttribute("inviteTk", token);
			
			if(user == null) {
				// 로그인이 안되어있을때 로그인페이지로
				return "redirect:/login";
			}else {
				// 로그인 돼있을때
				// 프로젝트에 insert
				invite.setMembUniNo(user.getMembUniNo());
				invite.setNnm(user.getNm());
				invite.setPrjPubcId(user.getId());
				int result = prjService.insertPartiMemb(invite);
				session.setAttribute("inviterst", result);

				// session token 삭제
				session.removeAttribute("inviteTk");
				// 프로젝트리스트로
				return "redirect:prjList";
			}
		}
	}
	
	@GetMapping("subscribe")
	public String subscribe() {
		return "prj/feeInfo";
	}
	
	// 프로젝트 참여 회원 리스트
	@GetMapping("prjMembList")
	public String prjMembList(HttpSession session, Model model){
		PrjVO prjVO = new PrjVO();
		prjVO.setPrjUniNo((String) session.getAttribute("prjUniNo") );
		model.addAttribute("membList", prjService.getPrjPartiList(prjVO));
		return "prj/prjMembList";
	}
}
