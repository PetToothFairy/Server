package com.example.server.user;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.server.jwt.JwtProvider;
import com.example.server.kakao.KakaoApi;

import lombok.RequiredArgsConstructor;

// User will use
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider = new JwtProvider();

    @GetMapping("/database")
    public ResponseEntity<?> findAll() {
        List<User> userList = userRepository.findAll();
        return ResponseEntity.ok().body(userList);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().body("test");
    }

    // 앱 첫 실행시.
    // 1. jwt토큰을 kakako토큰으로 변환.
    // 2-1. 유효하지 않은 토큰인 경우, 로그인 화면으로 이동.
    // 2-2. 유효한 토큰인 경우, 재발급 후 홈화면 이동 (자동 로그인)
    @GetMapping("/checktoken")
    public ResponseEntity<?> checkToken(@RequestHeader("accessToken") String accesstokenJWT,
            @RequestHeader("refreshToken") String refreshtokenJWT)
            throws Exception {
        KakaoApi kakaoApi = new KakaoApi();
        HashMap<String, String> map = kakaoApi.validateToken(accesstokenJWT, refreshtokenJWT);

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
                .header("Location", "/api/home")
                .build();
    }

    // petsetting 유저의 펫 이름과 몸무게 설정 후 헤더로 받음. 이후 데이터베이스에 업데이트
    // 헤더 : Email, 펫 이름, 펫 몸무게.
    @PutMapping("/petsetting")
    public ResponseEntity<?> petSetting(@RequestHeader("email") String email,
            @RequestHeader("petname") String petname,
            @RequestHeader("petweight") Integer petweight) throws Exception {

        userRepository.updateUserByEmail(petname, petweight, email);

        return ResponseEntity
                .status(200)
                .header("Location", "/api/home")
                .build();
    }

    @PatchMapping("/petsetting")
    public ResponseEntity<?> petSetting(@RequestHeader("accessToken") String accesstokenJWT,
            @RequestHeader("refreshToken") String refreshtokenJWT,
            @RequestHeader("petname") String petname,
            @RequestHeader("petweight") Integer petweight) throws Exception {

        KakaoApi kakaoApi = new KakaoApi();
        HashMap<String, String> map = kakaoApi.validateToken(accesstokenJWT, refreshtokenJWT);

        if (map.get("status").equals("false")) {
            return ResponseEntity
                    .status(401)
                    .header("Location", "/api/user/login")
                    .build();
        }

        userRepository.updateUserByAccessToken(petname, petweight, jwtProvider.decodeJWTToken(accesstokenJWT));

        return ResponseEntity
                .status(200)
                .header("Location", "")
                .build();
    }
}