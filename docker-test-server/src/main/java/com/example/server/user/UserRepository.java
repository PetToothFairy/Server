package com.example.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(@Param("email") String email);

    User findByAccessToken(@Param("accessToken") String accessToken);

    User findByRefreshToken(@Param("refreshToken") String refreshToken);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.petName = :petName, u.petWeight = :petWeight WHERE u.email = :email")
    void updateUserByEmail(@Param("petName") String petName, @Param("petWeight") Integer petWeight,
            @Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.petName = :petName, u.petWeight = :petWeight WHERE u.accessToken = :accessToken")
    void updateUserByAccessToken(@Param("petName") String petName, @Param("petWeight") Integer petWeight,
            @Param("accessToken") String accessToken);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.accessToken = :accessToken WHERE u.refreshToken = :refreshToken")
    void updateUserByRefreshToken(@Param("accessToken") String accessToken, @Param("refreshToken") String refreshToken);
}