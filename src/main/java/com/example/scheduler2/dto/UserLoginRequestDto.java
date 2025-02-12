package com.example.scheduler2.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {
    @NotNull(message = "이메일을 반드시 입력해주세요.")
    private String email;

    @NotNull(message = "비밀번호를 반드시 입력해주세요.")
    private String userPassword;
}
