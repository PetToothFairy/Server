package com.example.server.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자
@Builder // Builder 패턴
@Table(name = "user_tb")
@Entity
public class User {
    @Id
    private String email;

    @Column(name = "pet_name")
    private String petName;

    @Column(name = "pet_weight")
    private Integer petWeight;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;
}