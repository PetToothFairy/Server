package com.example.server.model;

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
    
    @Column(name = "random_id")
    private String randomId;
}