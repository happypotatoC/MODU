package com.modu.app.prj.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.util.HtmlUtils;

import com.modu.app.prj.chat.service.ChatVO;


@Controller
public class ChatController {
	
	@Autowired
	private WebSocketHandler webSocketHandler;

	
	@GetMapping("/chatPage") 
	public String chatPage() {
		return "chat/chat";
	}

	@MessageMapping("/chat") 
	@SendTo("/sub/chat")
	public ChatVO greeting(ChatVO chatVO) throws Exception {
		Thread.sleep(1000); // simulated delay
		//return new ChatVO("Hello, " + HtmlUtils.htmlEscape(message.getCntn()) + "!");
		System.out.println(chatVO);
		return new ChatVO(HtmlUtils.htmlEscape(chatVO.getCntn()));
		
	}

}
