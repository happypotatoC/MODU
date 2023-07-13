package com.modu.app.prj.prj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.modu.app.prj.prj.service.PrjService;
import com.modu.app.prj.prj.service.PrjVO;

@Controller
public class PrjController {
	@Autowired
	PrjService prjService;
	
	@GetMapping("prjInsert")
	public String prjInsertForm(Model model) {
		model.addAttribute("prjVO", new PrjVO());
		return "prj/prjInsert";
	}
	
	@PostMapping("prjInsert")
	public String prjInsert(PrjVO prjVO) {
		prjService.insertPrj(prjVO);
		return "prj/prjList";
	}
	
	// 프로젝트 리스트 페이지
	@GetMapping("prjList")
	public String prjList(String membUniNo, Model model) {
		model.addAttribute("prjList",prjService.getPrjList(membUniNo));
		return "prj/prjList";
	}
}
