package com.example.server.service;

import org.springframework.stereotype.Service;

import com.example.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserRepository userRepository;
    
    public boolean checkIdAndRandomId(Long id, String RandomID) throws Exception {   
        return userRepository.existsByIdAndRandomId(id, RandomID);
    }

    public void updateRandomIdByUserId(Long userId, String RandomID) {
        userRepository.updateRandomIdByUserId(userId, RandomID);
    }
}
