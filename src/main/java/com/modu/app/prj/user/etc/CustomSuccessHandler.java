package com.modu.app.prj.user.etc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.modu.app.prj.prj.service.PrjService;
import com.modu.app.prj.prj.service.PrjVO;
import com.modu.app.prj.user.service.PrincipalDetails;
import com.modu.app.prj.user.service.UserVO;

public class CustomSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	PrjService prjService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		Object principal = authentication.getPrincipal();

		if (principal instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) principal;

			UserVO vo;

			if (userDetails instanceof PrincipalDetails) {
				vo = ((PrincipalDetails) userDetails).getUserVO();
				System.out.println("회원 로그인 : " + vo);
			} else {
				System.out.println("로그인 실패");
				return;
			}
			// 세션에 사용자 정보 저장
			HttpSession session = request.getSession();
			session.setAttribute("user", vo);
			
			// 초대토큰 있는지 확인
			String inviteTk = (String) session.getAttribute("inviteTk");
			if (inviteTk != null) {
				// 초대토큰 있을때
				PrjVO invite = prjService.selectInvite(inviteTk);
				invite.setMembUniNo(vo.getMembUniNo());
				invite.setNnm(vo.getNm());
				invite.setPrjPubcId(vo.getId());
				int result = prjService.insertPartiMemb(invite);
				session.setAttribute("inviterst", result);

				// session token 삭제
				session.removeAttribute("inviteTk");
			}
			
			// 아이디 저장 체크 여부 확인
			boolean rememberId = request.getParameter("rememberId") != null;

			if (rememberId) {
				// 아이디 저장을 위한 쿠키 생성
				Cookie cookie = new Cookie("savedUsername", vo.getId());
				cookie.setMaxAge(30 * 24 * 60 * 60); // 쿠키 수명 설정 (일단 30일)
				cookie.setPath("/");
				response.addCookie(cookie);
			} else {
				// 쿠키 삭제
				Cookie cookie = new Cookie("savedUsername", null);
				cookie.setMaxAge(0); // 쿠키 수명 삭제
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}

		response.sendRedirect("/modu/prjList");
	}
}