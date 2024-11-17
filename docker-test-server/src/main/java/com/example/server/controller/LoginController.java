package com.example.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.error.CException;
import com.example.server.service.LoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginController {
    private final LoginService loginService;

    // 로그인시 재가입 여부를 판단하는 API
    // Input : AccessToken, RefreshToken, ExpiresIn, RefreshTokenExpiresIn
    // Output(403) : 이미 가입한 사용자 -> 데이터 Update -> 홈페이지
    // Output(401) : 유효하지 않은 토큰
    // Output(200) : 재가입 No -> 회원가입 페이지
    @GetMapping("")
    public ResponseEntity<?> login(
                                @RequestHeader("AccessToken") String AccessToken,
                                @RequestHeader("ExpiresIn") Integer ExpiresIn,
                                @RequestHeader("RefreshToken") String RefreshToken,
                                @RequestHeader("RefreshTokenExpiresIn") Integer RefreshTokenExpiresIn
                                ){
        try {
            loginService.isUserRejoin(AccessToken, ExpiresIn, RefreshToken, RefreshTokenExpiresIn);
        } catch (CException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.toString());
        }
        
        return ResponseEntity.status(200).body(".");
    }
}
