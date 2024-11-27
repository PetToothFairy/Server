package com.example.server.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPet {
    @NotNull(message = "펫 이름은 필수입니다")
    private String petName;
    @NotNull(message = "펫 무게는 필수입니다")
    private Integer petWeight;
}
