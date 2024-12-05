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
import com.example.server.service.InvalidTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tooth")
public class ToothController {
    private final JwtTokenService jwtTokenService;
    private final InvalidTokenService invalidTokenService;

    @GetMapping("")
    public ResponseEntity<?> mypageGetinfo(@RequestHeader("AccessToken") String AccessToken) {
        // 1. RefreshToken Valid?
        try {
            if(jwtTokenService.validateAccessToken(AccessToken) == false) {
                throw new CException(ErrorBase.INVALID_TOKEN);
            }
        } catch (Exception e) {
            throw new CException(ErrorBase.INVALID_TOKEN);
        }

        // 2. Check userId
        Long userId;
        try {
            userId = jwtTokenService.extractIdFromAccessToken(AccessToken);
            if(invalidTokenService.existsById(userId) == false) {
                throw new CException(ErrorBase.INVALID_TOKEN);
            }
        } catch (Exception e) {
            throw new CException(ErrorBase.INVALID_TOKEN);
        }
        
        // 3. Create New Data


        return ResponseEntity
            .status(SuccessBase.SUCCESS.getStatus())
            .body(BaseResponse.success(SuccessBase.SUCCESS, "."));
    }
}
