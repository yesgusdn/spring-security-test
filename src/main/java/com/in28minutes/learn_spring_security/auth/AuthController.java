package com.in28minutes.learn_spring_security.auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.in28minutes.learn_spring_security.jwt.JwtDto;
import com.in28minutes.learn_spring_security.jwt.JwtUtil;
import com.in28minutes.learn_spring_security.user.CustomUserDetailsService;
import com.in28minutes.learn_spring_security.user.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private JwtUtil jwtUtil;
	private AuthenticationManagerBuilder authenticationManagerBuilder;	
	private CustomUserDetailsService userDetailsService;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	public AuthController(JwtUtil jwtUtil, AuthenticationManagerBuilder authenticationManagerBuilder, CustomUserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.userDetailsService = userDetailsService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> authorize(@RequestBody LoginDto loginDto){
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
		
		try {
			System.out.println("Creating authentication token");
			Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			System.out.println("Authentication successful");
			String jwt = jwtUtil.generateToken(authentication);
			return ResponseEntity.ok(new JwtDto(jwt));
		} catch (Exception e) {
			System.err.println("Authentication failed: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
		}
		
	
		
		
	}
	
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) { // @RequestBody 어노테이션 추가
        try {
            userDetailsService.createUser(user);
            return new ResponseEntity<>("Signup successful", HttpStatus.CREATED); // 201 Created 반환
        } catch (Exception e) {
            return new ResponseEntity<>("Signup failed: " + e.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request 반환
        }
    }
	
}