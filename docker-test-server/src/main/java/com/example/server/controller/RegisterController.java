package com.example.server.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.Response.BaseResponse;
import com.example.server.Response.CException;
import com.example.server.Response.ErrorBase;
import com.example.server.Response.SuccessBase;
import com.example.server.jwt.JwtTokenService;
import com.example.server.model.Token;
import com.example.server.model.UserPet;
import com.example.server.service.RegisterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegisterController {
    private final RegisterService registerService;
    private final JwtTokenService jwtTokenService;

    // 회원가입이 끝나면 저장하기 위한 API
    // Input : AccessToken, PetName, PetWeight
    // Output : status(200, 401, 500)
    @PostMapping("")
    public ResponseEntity<?> register(
                                    @RequestHeader("AccessToken") String AccessToken,
                                    @Valid @RequestBody UserPet userPet
                                    ) {
        Long userId = Long.valueOf(0);
        // 1. KAKAO OPEN API로 userId 확인.
        // => Error : 유효하지 않는 토큰(에러)
        try {
            userId = registerService.getUserIdByKakaoOpenApi(AccessToken);
        } catch(Exception e) {
            throw new CException(ErrorBase.KAKAO_INVALID_TOKEN);
        }

        // 2. userId로 데이터베이스 확인
        // => 데이터베이스 true : 이미 존재하는 유저(Bad Request 400)
        try {
            if(registerService.getUserIdByDatabase(userId) == true) {
                throw new CException(ErrorBase.REGISTERED_USER);
            }
        } catch(Exception e) {
            throw new CException(ErrorBase.REGISTERED_USER);
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

        // 4. 모든 정보 저장.
        try {
            registerService.createUserInfo(userId, randomId, userPet.getPetName(), userPet.getPetWeight());;
        } catch(Exception e) {
            throw new CException(ErrorBase.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity
            .status(SuccessBase.SUCCESS.getStatus())
            .body(BaseResponse.success(SuccessBase.SUCCESS, token));
    }
}
