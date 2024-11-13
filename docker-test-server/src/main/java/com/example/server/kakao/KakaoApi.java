package com.example.server.kakao;

import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.server.jwt.JwtProvider;
import com.example.server.user.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KakaoApi {
    private RestTemplate restTemplate = new RestTemplate();
    @Value("{key.kakao-api-key}")
    private  String CLIENT_ID;
    private UserRepository userRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    private JwtProvider jwtProvider = new JwtProvider();

    // 인가 코드를 받아서 accessToken을 반환
    public KakaoLoginDto getAccessToken(String code, String CLIENT_ID, String REDIRECT_URL) throws Exception {
        final String apiUrl = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
        requestData.add("grant_type", "authorization_code");
        requestData.add("client_id", CLIENT_ID);
        requestData.add("redirect_uri", REDIRECT_URL);
        requestData.add("code", code);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestData,
                String.class);

        // 에러 발생
        if (responseEntity.getStatusCode() != HttpStatus.OK)
            throw new Exception(responseEntity.getStatusCode().toString());

        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());

        KakaoLoginDto kakaoLoginDto = new KakaoLoginDto();
        kakaoLoginDto.setAccessToken((String) jsonNode.get("access_token").asText());
        kakaoLoginDto.setRefreshToken((String) jsonNode.get("refresh_token").asText());

        return kakaoLoginDto;
    }

    // accessToken을 받아서 사용자 정보(Email)로 반환
    public String getUserInfo(String accessToken) throws Exception {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                requestEntity,
                String.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK)
            throw new Exception("사용자 정보를 받아오는 중 에러.");

        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
        JSONObject kakaoAccount = jsonObject.getJSONObject("kakao_account");

        if (!kakaoAccount.has("email")) { // 로그인 화면으로 이동.
            return "";
        }

        return kakaoAccount.get("email").toString();
    }

    // accessToken을 받아서 로그아웃 시키는 메서드
    public void kakaoLogout(String accessToken) throws Exception {
        String logoutUrl = "https://kapi.kakao.com/v1/user/logout";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Bearer 인증 방식으로 액세스 토큰 설정

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                logoutUrl,
                requestEntity,
                String.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("카카오 로그아웃 실패: " + responseEntity.getStatusCode());
        }
    }

    // kakao 토큰 유효한지 확인하는 함수.
    // 1-1. 만료되지X 통과 (True)
    // 1-2. 만료시 refreshToken 만료체크
    // 2-1. refreshToken 만료되지X accessToken 재발급 (True)
    // 2-2. refreshToken 만료시 로그인 화면 이동 (False)
    public HashMap<String, String> validateToken(String accessTokenJWT, String refreshTokenJWT) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtProvider.decodeJWTToken(accessTokenJWT)); // Bearer 토큰 설정

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://kapi.kakao.com/v1/user/access_token_info",
                HttpMethod.GET,
                entity,
                String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            map.put("status", "true");
            map.put("accessTokenJWT", accessTokenJWT);
            map.put("refreshTokenJWT", refreshTokenJWT);
            return map;
        }

        // RefreshToken 확인
        String refreshToken = jwtProvider.decodeJWTToken(refreshTokenJWT);
        String userInfoUrl = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
        requestData.add("grant_type", "refresh_token");
        requestData.add("client_id", CLIENT_ID);
        requestData.add("refresh_token", refreshToken);

        responseEntity = restTemplate.postForEntity(userInfoUrl,
                requestData, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            userRepository.updateUserByRefreshToken(jwtProvider.decodeJWTToken(refreshTokenJWT),
                    jwtProvider.createJWTToken((String) jsonNode.get("access_token").asText()));
            map.put("status", "true");
            map.put("accessTokenJWT", jwtProvider.createJWTToken((String) jsonNode.get("access_token").asText()));
            map.put("refreshTokenJWT", refreshTokenJWT);
            return map;
        }

        map.put("status", "false");
        map.put("accessTokenJWT", "");
        map.put("refreshTokenJWT", "");
        return map;
    }
}