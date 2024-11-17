package com.example.server.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.jwt.JwtProvider;
import com.example.server.user.User;
import com.example.server.user.UserRepository;

import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class KakaoController {
    private final KakaoApi kakaoApi = new KakaoApi();
    private final String REDIRECT_URL = "http://localhost:8080/api/kakao/login/oauth2/code";
    @Value("{key.kakao-api-key}")
    private final String CLIENT_ID;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    // 1. 코드 발급 받기
    @GetMapping("/getcode")
    public String getcode() throws Exception {
        String apiUrl = "https://kauth.kakao.com/oauth/authorize" +
                "?client_id=" + CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URL +
                "&response_type=code" +
                "&scope=account_email" +
                "&prompt=select_account consent";

        return "redirect:" + apiUrl;
    }

    // Kakao 버튼을 클릭시 발생.
    @GetMapping("/login/oauth2/code")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String code) throws Exception {
        // 1. 코드 발급 받기 (위에 작성)

        // 2. 코드로 액세스토큰과 리프레시토큰 받기
        KakaoLoginDto kakaoLoginDto = kakaoApi.getAccessToken(code, CLIENT_ID, REDIRECT_URL);

        // 3. 사용자 정보 받기 (email)
        String email = kakaoApi.getUserInfo(kakaoLoginDto.getAccessToken());

        // 4. 이메일 형식이 다르면 login 화면으로 이동
        if (email.equals("")) {
            return ResponseEntity
                    .status(200)
                    .header("Location", "/api/user/login")
                    .build();
        }

        // 5. 데이터베이스에서 확인, 있으면 Home으로 이동 & 갱신된 Token 저장 : 없으면 펫 설정으로 이동 & 새 Token 저장.
        User user = userRepository.findByEmail(email);

        if (user == null) {
            User newUser = new User(email, "", 0, kakaoLoginDto.getAccessToken(), kakaoLoginDto.getRefreshToken());
            userRepository.save(newUser);
            return ResponseEntity
                    .status(200)
                    .header("Email", email)
                    .header("AccessToken", jwtProvider.createJWTToken(kakaoLoginDto.getAccessToken()))
                    .header("RefreshToken", jwtProvider.createJWTToken(kakaoLoginDto.getRefreshToken()))
                    .header("Location", "/api/user/petsetting")
                    .build();
        }

        String accessTokenJWT = jwtProvider.createJWTToken(user.getAccessToken());
        String refreshTokenJWT = jwtProvider.createJWTToken(user.getRefreshToken());

        return ResponseEntity
                .status(200)
                .header("Email", email)
                .header("AccessToken", accessTokenJWT)
                .header("RefreshToken", refreshTokenJWT)
                .header("Location", "/api/home")
                .build();
    }
}
