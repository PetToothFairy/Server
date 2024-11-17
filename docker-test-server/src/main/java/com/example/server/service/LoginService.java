package com.example.server.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.server.error.CException;
import com.example.server.error.ErrorCode;
import com.example.server.model.UserId;
import com.example.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
    private String GET_USER_INFORMATION_URL="https://kapi.kakao.com/v2/user/me";
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    // 0. 유효하지 않은 토큰이면 (401)
    // 1. AccessToken으로 UserID가져오기
    // 2. id로 데이터베이스에 id값이 있는지 확인
    // 3-1. 있으면 이미 회원가입한 사용자 (403), Token update할 것.
    // 3-2. 없으면 회원가입으로 이동 (200)

    // Input : AccessToken, Output : status(200, 401, 403)
    // Output(403) : 이미 재가입 Yes -> 데이터 Update -> 홈페이지
    // Output(401) : 유효하지 않은 토큰
    // Output(200) : 재가입 No -> 회원가입 페이지
    public void isUserRejoin(String AccessToken, Integer ExpiresIn, String RefreshToken, Integer RefreshTokenExpiresIn) throws Exception {      
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Bearer " + AccessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserId> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(
                                                    GET_USER_INFORMATION_URL,
                                                    httpEntity,
                                        UserId.class
                                                    );
        } catch(Exception e) {
            throw new CException(ErrorCode.INVALID_TOKEN);
        }
                                                
        UserId userId = responseEntity.getBody();
        if(userId == null) {
            throw new CException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        boolean isIdFlag = userRepository.existsById(userId.getId());

        // 이미 회원가입한 사용자(403오류), Token update후 홈페이지로 이동.
        if(isIdFlag == true) {
            LocalDateTime ExpiresInTime = LocalDateTime.now().plusSeconds(ExpiresIn);
            System.out.println(ExpiresIn);
            System.out.println(LocalDateTime.now());
            System.out.println(ExpiresInTime);
            LocalDateTime RefreshTokenExpiresInTime = LocalDateTime.now().plusSeconds(RefreshTokenExpiresIn);
            
            userRepository.updateUserTokenInformationById(userId.getId(), AccessToken, ExpiresInTime, AccessToken, RefreshTokenExpiresInTime);
            
            throw new CException(ErrorCode.ALREADY_REGISTER_USER);
        }
    }
}
