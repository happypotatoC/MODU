package com.modu.app.prj.board.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.modu.app.prj.board.mapper.BoardMapper;
import com.modu.app.prj.board.service.BoardService;
import com.modu.app.prj.board.service.BoardVO;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardMapper boardMapper;
	
}
