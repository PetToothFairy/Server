package com.example.server.service;

import org.springframework.stereotype.Service;

import com.example.server.model.User;
import com.example.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public User getPetInformation(String AccessToken) throws Exception{
        return userRepository.getUserInformationByAccessToken(AccessToken);
    }

    public void setPetInformation(String AccessToken, String PetName, Integer PetWeight) throws Exception {
        userRepository.setUserPetInformationByAccessToken(AccessToken, PetName, PetWeight);
    }
}
