package com.example.server.controller;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.error.CException;
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

    // 홈페이지
    // 펫이름 가져오기
    // Input : 파라미터 AccessToken
    // Output(200) : 홈페이지[이미 가입한 회원]
    // Output(401) : 유효하지 않은 토큰
    // Output(402) : 리프레시토큰으로 토큰 재발급 필요.
    // Output(500) : 서버에러
    @GetMapping("")
    public ResponseEntity<?> home(@RequestHeader("AccessToken") String AccessToken) {
        User user = null;
        try {
            invalidTokenService.isTokenInvalid(AccessToken);
            System.out.println(AccessToken);
            user = homeService.getPetInformation(AccessToken);
        } catch (CException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.toString());
        }

        if(Objects.isNull(user))
            return ResponseEntity.status(500).body("유저 정보를 가져오지 못 했습니다.");
        
        UserPet userPet = new UserPet(user.getPetName(), user.getPetWeight());

        // access 토큰 확인 
        return ResponseEntity.status(200).body(userPet);
    }
}
