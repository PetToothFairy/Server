package com.example.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.model.User;
import com.example.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getPetInformation(Long id) throws Exception{
        return userRepository.getUserInformationByAccessToken(id);
    }

    public void setPetInformation(Long id, String PetName, Integer PetWeight) throws Exception {
        userRepository.setUserPetInformationByAccessToken(id, PetName, PetWeight);
    }
}
