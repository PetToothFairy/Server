package com.example.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.Response.BaseResponse;
import com.example.server.Response.CException;
import com.example.server.Response.ErrorBase;
import com.example.server.Response.SuccessBase;
import com.example.server.jwt.JwtTokenService;
import com.example.server.model.Token;
import com.example.server.service.LoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginController {
    private final LoginService loginService;
    private final JwtTokenService jwtTokenService;

    // 로그인시 회원가입, 재로그인을 판단하는 API
    // Input : AccessToken(Kakao Access Token)
    // Output(403) : 존재하지 않는 유저(회원가입)   : Message "존재하지 않는 유저"
    // Output(401) : 유효하지 않은 토큰(에러)       : Message "유효하지 않은 '소셜' 액세스 토큰"
    // Output(200) : 가입 유저(재로그인)
    @GetMapping("")
    public ResponseEntity<?> login (@RequestHeader("AccessToken") String AccessToken) {
        Long userId = Long.valueOf(0);
        // 1. KAKAO OPEN API로 userId 확인.
        // => Error : 유효하지 않는 토큰(에러)
        try {
            userId = loginService.getUserIdByKakaoOpenApi(AccessToken);
        } catch(Exception e) {
            throw new CException(ErrorBase.KAKAO_INVALID_TOKEN);
        }
        // 2. userId로 데이터베이스 확인
        // => 데이터베이스 false : 존재하지 않는 유저(회원가입)
        try {
            System.out.println(loginService.getUserIdByDatabase(userId));
            if(loginService.getUserIdByDatabase(userId) == false) {
                throw new CException(ErrorBase.GHOST_USER);
            }
        } catch(Exception e) {
            System.out.println("Here");
            throw new CException(ErrorBase.GHOST_USER);
        }

        // 3. JWT AccessToken, RefreshToken 토큰 발급
        Token token = new Token();
        String randomId = jwtTokenService.generateRandomId();
        try {
            String accessToken = jwtTokenService.createAccessToken(userId);
            String refreshToken = jwtTokenService.createRefreshToken(userId, randomId);
            token.setAccessToken(accessToken);
            token.setRefreshToken(refreshToken);
        } catch(Exception e) {
            throw new CException(ErrorBase.INTERNAL_SERVER_ERROR);
        }

        // 4. RefreshToken RandomID Database에 저장
        try {
            loginService.updateRandomIdByUserId(userId, randomId);
        } catch(Exception e) {
            throw new CException(ErrorBase.KAKAO_INVALID_TOKEN);
        }

        return ResponseEntity
            .status(SuccessBase.SUCCESS.getStatus())
            .body(BaseResponse.success(SuccessBase.SUCCESS, token));
    }
}
