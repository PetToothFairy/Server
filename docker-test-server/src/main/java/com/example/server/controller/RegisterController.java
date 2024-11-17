package com.example.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.error.CException;
import com.example.server.service.RegisterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegisterController {
    private final RegisterService registerService;

    // 회원가입이 끝나면 저장하기 위한 API
    // Input : AccessToken, ExpiresI, RefreshToken, RefreshTokenExpiresIn, PetName, PetWeight
    // Output : status(200, 401, 500)
    @PostMapping("")
    public ResponseEntity<?> register(
                                    @RequestHeader("AccessToken") String AccessToken,
                                    @RequestHeader("ExpiresIn") Integer ExpiresIn,
                                    @RequestHeader("RefreshToken") String RefreshToken,
                                    @RequestHeader("RefreshTokenExpiresIn") Integer RefreshTokenExpiresIn,
                                    @RequestHeader("PetName") String PetName,
                                    @RequestHeader("PetWeight") Integer PetWeight
                                    ) {
        Integer status = 200;
        try {
            registerService.isUserRejoin(AccessToken, ExpiresIn, RefreshToken, RefreshTokenExpiresIn, PetName, PetWeight);
        } catch (CException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.toString());
        }

        return ResponseEntity.status(status).body(".");
    }
}
