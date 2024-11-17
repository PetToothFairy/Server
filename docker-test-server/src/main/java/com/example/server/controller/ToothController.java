package com.example.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.error.CException;
import com.example.server.service.InvalidTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tooth")
public class ToothController {
    // 양치질이 끝나면 또는 주기적으로 Front에서 데이터를 보내면
    // TimeStream에 저장해주는 역할 API.

    private final InvalidTokenService invalidTokenService;

    // 사용자가 애완견 이빨을 찍으면
    @GetMapping("") 
    public ResponseEntity<?> tooth(@RequestHeader("AccessToken") String AccessToken) {
        try {
            invalidTokenService.isTokenInvalid(AccessToken);
        } catch (CException e) {
            ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
        } catch (Exception e) {
            ResponseEntity.status(500).body(e.toString());
        }

        return ResponseEntity.status(200).body("null");
    }
}
