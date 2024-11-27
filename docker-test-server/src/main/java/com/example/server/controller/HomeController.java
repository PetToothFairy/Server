package com.example.server.controller;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.error.CErrorResponse;
import com.example.server.error.CException;
import com.example.server.error.ErrorCode;
import com.example.server.jwt.JwtTokenService;
import com.example.server.model.User;
import com.example.server.model.UserPet;
import com.example.server.service.HomeService;
import com.example.server.service.InvalidTokenService;

import lombok.RequiredArgsConstructor;

@RestController             // REST API 컨트롤러 선언, @Controller + @ResponseBody 결합
@RequiredArgsConstructor    // final 필드의 생성자 자동 생성
@RequestMapping("/api/home")
public class HomeController {
    private final InvalidTokenService invalidTokenService;
    private final HomeService homeService;
    private final JwtTokenService jwtTokenService;
    // 홈페이지
    // 펫이름 가져오기
    // Input : 파라미터 AccessToken
    // Output(200) : 홈페이지[이미 가입한 회원]
    // Output(401) : 유효하지 않은 토큰
    // Output(402) : 리프레시토큰으로 토큰 재발급 필요.
    // Output(500) : 서버에러
    @GetMapping("")
    public ResponseEntity<?> home(@RequestHeader("AccessToken") String AccessToken) {
        // 1. RefreshToken Valid?
        try {
            if(jwtTokenService.validateAccessToken(AccessToken) == false) {
                throw new CException(ErrorCode.INVALID_TOKEN);
            }

        } catch (Exception e) {
            throw new CException(ErrorCode.INVALID_TOKEN);
        }

        // 2. Check userId
        Long userId;
        try {
            userId = jwtTokenService.extractIdFromAccessToken(AccessToken);
            if(invalidTokenService.existsById(userId) == false) {
                throw new CException(ErrorCode.INVALID_TOKEN);
            }
        } catch (Exception e) {
            throw new CException(ErrorCode.INVALID_TOKEN);
        }

        // 3. Get Pet Information By UserId
        User user = null;
        try {
            user = homeService.getPetInformation(userId);
        } catch (Exception e) {
            throw new CException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        if(Objects.isNull(user))
            throw new CException(ErrorCode.INTERNAL_SERVER_ERROR);
        
        UserPet userPet = new UserPet(user.getPetName(), user.getPetWeight());

        return ResponseEntity
            .status(ErrorCode.SUCCESS.getStatus())
            .body(CErrorResponse.builder()
                .status(ErrorCode.SUCCESS.getStatus())
                .message(userPet)
                .build()
            );
    }
}