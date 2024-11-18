package com.example.server.controller;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.error.CException;
import com.example.server.model.User;
import com.example.server.model.UserPet;
import com.example.server.service.InvalidTokenService;
import com.example.server.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private InvalidTokenService invalidTokenService;
    private UserService userService;

    @GetMapping("test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().body(".");
    }

    // 마이페이지
    // 마이페이지를 보여주는 API
    // Input : AccessToken
    // Output : User
    @GetMapping("/getinfo")
    public ResponseEntity<?> mypageGetinfo(@RequestHeader("AccessToken") String AccessToken) {
        User user = null;
        try {
            invalidTokenService.isTokenInvalid(AccessToken);
            user = userService.getPetInformation(AccessToken);
        } catch (CException e) {
            ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
        } catch (Exception e) {
            ResponseEntity.status(500).body(e.toString());
        }

        if(Objects.isNull(user))
            ResponseEntity.status(500).body("유저 정보를 가져오지 못 했습니다.");

        UserPet userPet = new UserPet(user.getPetName(), user.getPetWeight());
        
        return ResponseEntity.status(200).body(userPet);
    }

    // 마이페이지 수정
    // 마이페이지를 수정하는 API
    // Input : AccessToken, PetName, PetWeight
    // Output : status(200, 401, 500)
    @PatchMapping("/setinfo")
    public ResponseEntity<?> mypageGetinfo(
                                        @RequestHeader("AccessToken") String AccessToken,
                                        @RequestHeader("PetName") String PetName,
                                        @RequestHeader("PetWeight") Integer PetWeight
                                        ) {
        try {
            invalidTokenService.isTokenInvalid(AccessToken);
            userService.setPetInformation(AccessToken, PetName, PetWeight);
        } catch (CException e) {
            ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
        } catch (Exception e) {
            ResponseEntity.status(500).body(e.toString());
        }

        return ResponseEntity.ok().body(".");
    }
}