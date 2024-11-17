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
public class UpdateAccessService {
    private String GET_USER_INFORMATION_URL="https://kapi.kakao.com/v2/user/me";
    private RestTemplate restTemplate;
    private UserRepository userRepository;

    public void updateAccessTokenById(String AccessToken, Integer ExpiresIn) {
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

        LocalDateTime ExpiresInTime = LocalDateTime.now().plusSeconds(ExpiresIn);

        userRepository.updateAccessTokenById(userId.getId(), AccessToken, ExpiresInTime);
    }
}
