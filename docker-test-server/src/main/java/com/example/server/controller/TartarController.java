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
@RequestMapping("/api/tartar")
public class TartarController {
    private final InvalidTokenService invalidTokenService;

    // Default :
    // 1. Token이 유효한지 확인.
    
    // 사용자가 애완견 이빨을 찍으면 
    @GetMapping("")
    public ResponseEntity<?> tartar(@RequestHeader("AccessToken") String AccessToken) {
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
