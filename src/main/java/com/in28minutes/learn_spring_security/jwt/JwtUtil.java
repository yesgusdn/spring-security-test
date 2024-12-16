package com.in28minutes.learn_spring_security.jwt;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    
    // 토큰 생성
    public String generateToken (Authentication authentication) {
    	return Jwts.builder()
    			.setSubject(authentication.getName())
    			.setIssuedAt(new Date())
    			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
    			.signWith(key)
    			.compact();
    }
    
    // Header에서 토큰 추출
    public String extractToken(HttpServletRequest request) {
    	String header = request.getHeader("Authorization");
    	if(header != null && header.startsWith("Bearer ")) {
    		return header.substring(7);
    	}
    	
    	return null;
    }
    
    // 토큰 유효성 검증
    public boolean validateToken(String token) {
    	try {
    		Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    		return true;
    	} catch(Exception e) {
    		Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    		logger.error(e.getMessage());
    		return false;
    	}
    	
    }
    
    // 토큰에 해당하는 user 가져오기
    public String getUsernameFromToken(String token) {
    	Claims claims = extractAllClaims(token);
    	return claims.getSubject();
    }
    
    public Claims extractAllClaims(String token) {
    	return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}