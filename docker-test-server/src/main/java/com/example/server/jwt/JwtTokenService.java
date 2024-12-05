package com.example.server.jwt;

import java.security.SecureRandom;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtTokenService {
    
    private String secret = System.getenv("JWT_SECRET_KEY");
    
    private int accessTokenExpMinutes = 600 * 1000;        // 10분

    private int refreshTokenExpMinutes = 1209600 * 1000;   // 2주

    private final Algorithm algorithm = Algorithm.HMAC256(secret);
    
    public String generateRandomId() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(64);
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        
        for (int i = 0; i < 64; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        
        return sb.toString();
    }

    // AccessToken 생성
    public String createAccessToken(Long id) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenExpMinutes);

        String createdToken = JWT.create()
                .withSubject(String.valueOf(id))
                .withExpiresAt(validity)
                .sign(algorithm);

        return createdToken;
    }

    // RefreshToken 생성
    public String createRefreshToken(Long id, String randomId) {
        String ramdomId = randomId;
        
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenExpMinutes);

        String createdToken = JWT.create()
                .withSubject(String.valueOf(id))
                .withClaim("RandomId", ramdomId)  // Kakao AccessToken
                .withExpiresAt(validity)
                .sign(algorithm);

        return createdToken;
    }

    // AccessToken Validation
    public boolean validateAccessToken(String token) throws Exception {
        // 1. Verify AccessToken
        DecodedJWT jwt = JWT.require(algorithm)
            .build()
            .verify(token);

        // 2. Check Expired AccessToken
        Date expiresAt = jwt.getExpiresAt();
        System.out.println((new Date()));
        System.out.println(expiresAt);
        if (expiresAt != null && expiresAt.before(new Date())) {
            log.error("만료된 토큰입니다.");
            return false;
        }
        return true;
    }

    // RefreshToken Validation
    public boolean validateRefreshToken(String token) throws Exception {
         // 1. Verify RefreshToken
         DecodedJWT jwt = JWT.require(algorithm)
         .build()
         .verify(token);

        // 2. Check Expired RefreshToken
        Date expiresAt = jwt.getExpiresAt();
        if (expiresAt != null && expiresAt.before(new Date())) {
            log.error("만료된 토큰입니다.");
            return false;
        }
        return true;
    }

    // RefreshToken에서 id 추출
    public Long extractIdFromRefreshToken(String token) throws Exception {
        DecodedJWT jwt = JWT.require(algorithm)
            .build()
            .verify(token);
            
        String idString = jwt.getSubject();
        return Long.parseLong(idString);
    }

    // RefreshToken에서 randomId 추출
    public String extractRandomIdFromRefreshToken(String token) throws Exception {
        DecodedJWT jwt = JWT.require(algorithm)
            .build()
            .verify(token);
            
        return jwt.getClaim("RandomId").asString();
    }

    // AccessToken에서 id 추출
    public Long extractIdFromAccessToken(String token) throws Exception {
        DecodedJWT jwt = JWT.require(algorithm)
            .build()
            .verify(token);
            
        String idString = jwt.getSubject();
        return Long.parseLong(idString);
    }
}