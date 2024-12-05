package com.example.server.controller;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.Response.BaseResponse;
import com.example.server.Response.CException;
import com.example.server.Response.ErrorBase;
import com.example.server.Response.SuccessBase;
import com.example.server.jwt.JwtTokenService;
import com.example.server.model.User;
import com.example.server.model.UserPet;
import com.example.server.service.InvalidTokenService;
import com.example.server.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final InvalidTokenService invalidTokenService;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @GetMapping("test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().body(".");
    }

    // 마이페이지
    // 마이페이지를 보여주는 API
    // Input : AccessToken
    // Output : User status(200, 401, 500)
    @GetMapping("/getinfo")
    public ResponseEntity<?> mypageGetinfo(@RequestHeader("AccessToken") String AccessToken) {
        // 1. RefreshToken Valid?
        try {
            if(jwtTokenService.validateAccessToken(AccessToken) == false) {
                throw new CException(ErrorBase.INVALID_TOKEN);
            }
        } catch (Exception e) {
            throw new CException(ErrorBase.INVALID_TOKEN);
        }

        // 2. Check userId
        Long userId;
        try {
            userId = jwtTokenService.extractIdFromAccessToken(AccessToken);
            if(invalidTokenService.existsById(userId) == false) {
                throw new CException(ErrorBase.INVALID_TOKEN);
            }
        } catch (Exception e) {
            throw new CException(ErrorBase.INVALID_TOKEN);
        }

        // 3. Get Pet Information By UserId
        User user = null;
        try {
            user = userService.getPetInformation(userId);
        } catch (Exception e) {
            throw new CException(ErrorBase.INTERNAL_SERVER_ERROR);
        }

        if(Objects.isNull(user))
            throw new CException(ErrorBase.INTERNAL_SERVER_ERROR);
        
        UserPet userPet = new UserPet(user.getPetName(), user.getPetWeight());

        return ResponseEntity
            .status(SuccessBase.SUCCESS.getStatus())
            .body(BaseResponse.success(SuccessBase.SUCCESS, userPet));
    }

    // 마이페이지 수정
    // 마이페이지를 수정하는 API
    // Input : AccessToken, PetName, PetWeight
    // Output : status(200, 401, 500)
    @PatchMapping("/setinfo")
    public ResponseEntity<?> mypageGetinfo(
                                        @RequestHeader("AccessToken") String AccessToken,
                                        @Valid @RequestBody UserPet userPet
                                        ) {
                                            // 1. RefreshToken Valid?
        try {
            if(jwtTokenService.validateAccessToken(AccessToken) == false) {
                throw new CException(ErrorBase.INVALID_TOKEN);
            }
        } catch (Exception e) {
            throw new CException(ErrorBase.INVALID_TOKEN);
        }

        // 2. Check userId
        Long userId;
        try {
            userId = jwtTokenService.extractIdFromAccessToken(AccessToken);
            if(invalidTokenService.existsById(userId) == false) {
                throw new CException(ErrorBase.INVALID_TOKEN);
            }
        } catch (Exception e) {
            throw new CException(ErrorBase.INVALID_TOKEN);
        }

        try {
            userService.setPetInformation(userId, userPet.getPetName(), userPet.getPetWeight());
        } catch (Exception e) {
            throw new CException(ErrorBase.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity
            .status(SuccessBase.SUCCESS.getStatus())
            .body(BaseResponse.success(SuccessBase.SUCCESS, "."));
    }
}