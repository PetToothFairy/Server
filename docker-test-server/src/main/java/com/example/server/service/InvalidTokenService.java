package com.example.server.service;

import org.springframework.stereotype.Service;

import com.example.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // final 필드의 생성자 자동 생성
public class InvalidTokenService {
    private final UserRepository userRepository;

    public boolean existsById(Long userId) throws Exception{
        return userRepository.existsById(userId);
    }
}
