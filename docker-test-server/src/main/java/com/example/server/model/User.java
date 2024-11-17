package com.example.server.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자
@Entity
@Table(name = "user_tb")
public class User {
    @Id
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "pet_name")
    private String petName;

    @Column(name = "pet_weight")
    private Integer petWeight;
    
    @Column(name = "access_token")
    private String accessToken;
    
    @Column(name = "expires_in")
    private LocalDateTime expiresIn;
    
    @Column(name = "refresh_token")
    private String refreshToken;
    
    @Column(name = "refresh_token_expires_in")
    private LocalDateTime refreshTokenExpiresIn;
}