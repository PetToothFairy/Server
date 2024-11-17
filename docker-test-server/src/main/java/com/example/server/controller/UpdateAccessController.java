package com.example.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.error.CException;
import com.example.server.service.UpdateAccessService;

import lombok.RequiredArgsConstructor;

@RestController             // REST API 컨트롤러 선언, @Controller + @ResponseBody 결합
@RequiredArgsConstructor    // final 필드의 생성자 자동 생성
@RequestMapping("/api/update")
public class UpdateAccessController {
    private UpdateAccessService updateAccessService;

    // AccessToken을 새로 발급 받았을때 사용하는 API.
    // 서버에 새로운 AccessToken을 제공.
    @GetMapping("/accesstoken")
    public ResponseEntity<?> updateAccessToken(@RequestHeader("AccessToken") String AccessToken,
                                                @RequestHeader("ExpiresIn") Integer ExpiresIn) {
        try {
            updateAccessService.updateAccessTokenById(AccessToken, ExpiresIn);
        } catch(CException e) {
            ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
        } catch(Exception e) {
            ResponseEntity.status(500).body(e.toString());
        }
        return ResponseEntity.status(null).body(null);
    }
}
