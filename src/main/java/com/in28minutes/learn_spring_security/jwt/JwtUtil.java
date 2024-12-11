package com.in28minutes.learn_spring_security.jwt;


import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.in28minutes.learn_spring_security.user.UserEntity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(UserEntity userEntity) {
        return Jwts.builder()
                .setSubject(userEntity.getUsername()) // 주체설정
                .setIssuedAt(new Date()) // 발급시간
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // 만료시간
                .signWith(key) // 서명 키 설정
                .compact(); // 문자열로 반환
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
           return false;
        }
    }


}