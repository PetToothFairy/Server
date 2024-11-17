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
import com.example.server.model.User;
import com.example.server.model.UserId;
import com.example.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private String GET_USER_INFORMATION_URL="https://kapi.kakao.com/v2/user/me";
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    // AccessToken으로 UserID가져오기
    // Input : AccessToken, Output : status(200, 401, 500)
    public void isUserRejoin(
                            String AccessToken,
                            Integer ExpiresIn,
                            String RefreshToken,
                            Integer RefreshTokenExpiresIn,
                            String PetName,
                            Integer PetWeight) throws Exception {
        // ID값 가져오기
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
        
        // 시간 변형
        LocalDateTime ExpiresInTime = LocalDateTime.now().plusSeconds(ExpiresIn);
        LocalDateTime RefreshTokenExpiresInTime = LocalDateTime.now().plusSeconds(RefreshTokenExpiresIn);

        User user = new User(userId.getId(), 
                    PetName, 
                    PetWeight, 
                    AccessToken, 
                    ExpiresInTime, 
                    RefreshToken, 
                    RefreshTokenExpiresInTime);

        // Database에 Data 저장.
        userRepository.insertUserInformation(user);
    }
}
