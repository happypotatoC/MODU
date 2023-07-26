package com.modu.app.prj.user.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.modu.app.prj.user.mapper.UserMapper;
import com.modu.app.prj.user.service.UserService;
import com.modu.app.prj.user.service.UserVO;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	@Autowired
	UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserVO userVO = userMapper.loginCheck(username);

		if (userVO == null) {
			System.out.println("유저정보 없음");
			throw new UsernameNotFoundException("no user");
		}
		return userVO;
	}

	@Override
	public String generateRandomToken() {
		String token = UUID.randomUUID().toString();
		return token;
	}

	@Override
	public int signup(UserVO userVO) {
		return userMapper.signup(userVO);
	}

	@Override
	public String idSearch(UserVO userVO) {
		UserVO result = userMapper.idSearch(userVO);
		if (result == null) {
			return null;
		} else {
			return result.getId();
		}
	}

	@Override
	public int pwdSearch(UserVO userVO) {
		return userMapper.pwdSearch(userVO);
	}

	@Override
	public int idVaild(String id) {
		return userMapper.idVaild(id);
	}

	@Override
	public UserVO emailAuth(String id) {
		return userMapper.emailAuth(id);
	}

	@Override
	public int phNoVaild(String phNo) {
		return userMapper.phNoVaild(phNo);
	}

	@Override
	public String updateEmailAuthStatus(String token) {
		int result = userMapper.updateEmailAuthStatus(token);

		if (result > 0) {
			return "인증완료";
		} else {
			return "오류 또는 실패";
		}
	}

	@Override
	public String updateNm(Map<String, String> params) {
		int result = userMapper.updateNm(params);
		if (result > 0) {
			return "이름 변경 성공";
		} else {
			return "이름 변경 실패";
		}
	}

	@Override
	public String updatePwd(Map<String, String> params) {
		int result = userMapper.updatePwd(params);
		if (result > 0) {
			return "비밀번호 변경 성공";
		} else {
			return "비밀번호 변경 실패";
		}
	}

	@Override
	public String updateId(Map<String, String> params) {
		int result = userMapper.updateId(params);
		if (result > 0) {
			return "아이디 변경 성공";
		} else {
			return "아이디 변경 실패";
		}
	}

}
