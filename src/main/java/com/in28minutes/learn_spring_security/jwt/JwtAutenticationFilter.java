package com.in28minutes.learn_spring_security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.in28minutes.learn_spring_security.auth.LoginRequestDto;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAutenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private final JwtUtil jwtUtil;

	public JwtAutenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
		setFilterProcessesUrl("/api/user/login"); // 원하는 url에만 적용
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse reponse)
			throws AuthenticationException {
		try {
			LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword(), null)
					);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	
}
