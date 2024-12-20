package com.in28minutes.learn_spring_security.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.in28minutes.learn_spring_security.jwt.JwtAuthenticationFilter;
import com.in28minutes.learn_spring_security.jwt.JwtUtil;
import com.in28minutes.learn_spring_security.user.CustomUserDetailsService;

@Configuration
public class SecurityConfig {
    
	private CustomUserDetailsService  userDetailsService;	
	private JwtUtil jwtUtil;
	
	public SecurityConfig(@Lazy CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrfConfig -> 
					csrfConfig.disable()
			)
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/api/auth/**", "/auth/register","/h2-console/**").permitAll()
					.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
					.anyRequest().authenticated()
				)
			.headers(headers -> headers.disable())
			.formLogin(formLogin -> 
					formLogin.disable()
			)
			.logout(logout -> logout.permitAll())
			.httpBasic(httpBasic ->
					httpBasic.disable()
			)
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    

		
}
