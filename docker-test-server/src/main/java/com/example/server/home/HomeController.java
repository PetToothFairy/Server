package com.example.server.home;

import org.springframework.web.bind.annotation.RestController;

import com.example.server.kakao.KakaoApi;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {
    private final KakaoApi kakaoApi = new KakaoApi();

    // 홈 화면
    // 1. 토큰 확인.
    // 2. 토큰 만료시 재발급.
    // 3. 리프레시 토큰 또한 만료시 로그인 화면 이동.
    @GetMapping("")
    public ResponseEntity<?> home(@RequestHeader("accesstoken") String accesstoken,
            @RequestHeader("refreshtoken") String refreshtoken)
            throws Exception {

        HashMap<String, String> map = kakaoApi.validateToken(accesstoken, refreshtoken);

        if (map.get("status").equals("false")) {
            return ResponseEntity
                    .status(401)
                    .header("Location", "/api/user/login")
                    .build();
        }

        return ResponseEntity
                .status(200)
                .header("AccessToken", map.get("accessTokenJWT"))
                .header("RefreshToken", map.get("refreshTokenJWT"))
                .header("Location", "")
                .build();
    }
}
