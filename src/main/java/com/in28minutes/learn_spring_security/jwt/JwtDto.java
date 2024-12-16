package com.in28minutes.learn_spring_security.jwt;

import lombok.Getter;

@Getter
public class JwtDto {
	private String token;
	public JwtDto(String token) {
		this.token = token;
	}
}
