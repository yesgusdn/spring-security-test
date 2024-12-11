package com.in28minutes.learn_spring_security.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.in28minutes.learn_spring_security.jwt.JwtUtil;
import com.in28minutes.learn_spring_security.user.CustomUserDetailService;


@Configuration
public class SecurityConfig {
	
	private final CustomUserDetailService customUserDetailService;
	private final JwtUtil jwtUtil;
	
    public SecurityConfig(CustomUserDetailService customUserDetailService, JwtUtil jwtUtil) {
        this.customUserDetailService = customUserDetailService;
        this.jwtUtil = jwtUtil;
    }	

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf((csrfConfig) -> 
					csrfConfig.disable()
			)
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/auth/login", "/auth/register","/h2-console/**").permitAll()
					.anyRequest().authenticated()
				)
			.headers(headers -> headers.disable())
			.formLogin(formLogin -> 
					formLogin.disable()
			)
			.httpBasic(httpBasic ->
					httpBasic.disable()
			);
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

    // AuthenticationManager 빈 생성
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    	AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    	authenticationManagerBuilder.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
    	
        return authenticationManagerBuilder.build();
    }
    
    // UserDetailsService 빈 등록
    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailService;
    }	
}
