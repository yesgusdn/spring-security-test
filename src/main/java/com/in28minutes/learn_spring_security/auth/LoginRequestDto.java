package com.in28minutes.learn_spring_security.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {
	private String username;
	private String password;
}
