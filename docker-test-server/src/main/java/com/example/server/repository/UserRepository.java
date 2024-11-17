package com.example.server.repository;

import java.time.LocalDateTime;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.accessToken = :accessToken")
        boolean existsByAccessToken(@Param("accessToken") String accessToken);

        @Query("SELECT u.refreshToken FROM User u WHERE u.accessToken = :accessToken")
        String getRefreshTokenByAccessToken(@Param("accessToken") String accessToken);

        @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.accessToken = :accessToken AND u.expiresIn < CURRENT_TIMESTAMP")
        boolean isAccessTokenInvalid(@Param("accessToken") String accessToken);

        @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.refreshToken = :refreshToken AND u.refreshTokenExpiresIn < CURRENT_TIMESTAMP")
        boolean isRefreshTokenInvalid(@Param("refreshToken") String refreshToken);

        @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.id = :id")
        boolean existsById(@Param("id") Long id);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO user_tb (id, pet_name, pet_weight, access_token, expires_in, refresh_token, refresh_token_expires_in) VALUES (:#{#user.id}, :#{#user.petName}, :#{#user.petWeight}, :#{#user.accessToken}, :#{#user.expiresIn}, :#{#user.refreshToken}, :#{#user.refreshTokenExpiresIn})", nativeQuery = true)
        void insertUserInformation(@Param("user") User user);
        
        @Modifying
        @Transactional
        @Query(value = "UPDATE user_tb u SET u.access_token=:accessToken, u.expires_in=:expiresIn, u.refresh_token=:refreshToken, u.refresh_token_expires_in=:refreshTokenExpiresIn WHERE u.id = :id", nativeQuery = true)
        void updateUserTokenInformationById(@Param("id") Long id, @Param("accessToken") String accessToken, @Param("expiresIn") LocalDateTime expiresIn, @Param("refreshToken") String refreshToken, @Param("refreshTokenExpiresIn") LocalDateTime refreshTokenExpiresIn);
        
        @Query("SELECT u FROM User u WHERE u.accessToken = :accessToken")
        User getUserInformationByAccessToken(@Param("accessToken") String accessToken);
        
        @Modifying
        @Transactional
        @Query(value = "UPDATE user_tb u SET u.pet_name=:petName, u.pet_weight=:petWeight WHERE u.access_token=:accessToken", nativeQuery = true)
        void setUserPetInformationByAccessToken(@Param("accessToken") String accessToken, @Param("petName") String petName, @Param("petWeight") Integer petWeight);

        @Modifying
        @Transactional
        @Query(value = "UPDATE user_tb u SET u.access_token=:accessToken, u.expires_in=:expiresIn WHERE u.id = :id", nativeQuery = true)
        void updateAccessTokenById(@Param("id") Long id, @Param("accessToken") String accessToken, @Param("expiresIn") LocalDateTime expiresIn);
}