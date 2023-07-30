package com.modu.app.prj.sche.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.modu.app.prj.prj.service.PrjService;
import com.modu.app.prj.prj.service.PrjVO;
import com.modu.app.prj.sche.service.ScheService;
import com.modu.app.prj.sche.service.ScheVO;
import com.modu.app.prj.vote.service.VoteVO;

@Controller
public class ScheController {
	
	@Autowired
	ScheService scheService;
	
	@Autowired
	PrjService prjService;
	
	@GetMapping("scheList")
	public String ScheList(Model model,HttpSession session) {
		// 리스트 뽑기 , 리스트에 필요한 세션 값가져와서 바로 집어넣음.
		System.out.println("111111111111");
		PrjVO vo = new PrjVO();
		vo.setPrjUniNo((String) session.getAttribute("prjUniNo"));
		model.addAttribute("scheList",scheService.scheList((String) session.getAttribute("prjUniNo")));
		model.addAttribute("membList", prjService.getPrjPartiList(vo));
		System.out.println("2222222222222222");
		return "sche/scheList";
	}
	
	//단건조회
	@GetMapping("scheInfo/{scheUniNo}")
	@ResponseBody
	public Map<String, Object> scheInfo(@PathVariable String scheUniNo,Model model,HttpSession session) {
		Map<String, Object> map = new HashMap<>();
		map.put("scheInfo",scheService.scheInfo(scheUniNo));
		model.addAttribute("scheInfo",scheService.scheInfo(scheUniNo));
		map.put("partici", scheService.schePartici(scheUniNo));
		System.out.println(map);
		return map;
	}
	
	//투표 등록
	@PostMapping("scheInsert")
	@ResponseBody
	public String voteInsert(HttpSession session,@RequestBody ScheVO vo) {
		vo.setParticiMembUniNo((String) session.getAttribute("particiMembUniNo"));
		vo.setPrjUniNo((String) session.getAttribute("prjUniNo"));
		
		scheService.scheInsert(vo);
		return "success"; 
		
	}
	
	//삭제
	@PostMapping("scheDelte")
	public int deleteSche(@RequestBody ScheVO vo) {
		if(scheService.scheDelete(vo.getScheUniNo()) >0) {
			return 1;
		}else {
			return 2;
		}	
	}
}