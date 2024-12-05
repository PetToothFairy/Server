package com.example.server.service;


import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

    // Kakao Open Api에 AccessToken으로 UserId 가져오는 메소드
    // Input : AccessToken(KakaoAccessToken)
    // Output : UserId (Type : Long)
    public Long getUserIdByKakaoOpenApi(String AccessToken) throws Exception {      
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Bearer " + AccessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserId> responseEntity;
        responseEntity = restTemplate.postForEntity(GET_USER_INFORMATION_URL, httpEntity, UserId.class);
                                                
        UserId userId = responseEntity.getBody();

        return userId.getId();
    }

    public boolean getUserIdByDatabase(Long userId) throws Exception {   
        return userRepository.existsById(userId);
    }

    // TODO : 수정
    // AccessToken으로 UserID가져오기
    // Input : AccessToken, Output : status(200, 401, 500)
    public void createUserInfo(
                            Long userId,
                            String RandomID,
                            String PetName,
                            Integer PetWeight) throws Exception {
        
        User user = new User(
            userId,
            PetName, 
            PetWeight,
            RandomID,
            0,
            LocalDate.now(ZoneId.of("Asia/Seoul"))
            );

        // Database에 Data 저장.
        userRepository.insertUserInformation(user);
    }
}
