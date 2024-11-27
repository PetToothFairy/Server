package com.example.server.service;

import org.springframework.stereotype.Service;

import com.example.server.model.User;
import com.example.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final UserRepository userRepository;

    public User getPetInformation(Long userId) throws Exception{
        return userRepository.getUserInformationByAccessToken(userId);
    }
}