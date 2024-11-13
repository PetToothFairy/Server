package com.example.server.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import javax.crypto.SecretKey;

@Component
public class JwtProvider {
    @Value("${key.jwt-secret-key}")
    private String secretKey;



    private Key getSigningKey() {
        byte[] keyBytes = this.secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 토큰에서 모든 클레임 추출
    private Claims getAllClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 문자열을 JWT로 암호화.
    public String createJWTToken(String payload) {
        return Jwts.builder()
                .setSubject("JWT") // 예시로 "JWT" 사용
                .claim("data", payload)
                .signWith(getSigningKey())
                .compact();
    }

    // JWT를 다시 문자열로 비암호화
    public String decodeJWTToken(String jwtToken) {
        Claims claims;
        try {
            claims = getAllClaimsFromToken(jwtToken);
        } catch (Exception e) {
            return "";
        }
        return claims.get("data", String.class);
    }

    // JWT 토큰을 검증하고 Kakao토큰 추출.
    public String validateAndGetPayload(String token) {
        Claims claims;

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }

        return claims.get("data", String.class);
    }
}