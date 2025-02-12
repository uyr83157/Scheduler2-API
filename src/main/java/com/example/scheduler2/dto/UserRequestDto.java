package com.example.scheduler2.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    @NotNull(message = "이름을 반드시 입력해주세요.")
    @Size(max = 4, message = "이름은 4글자 이내여야 합니다.")
    private String name;

    @NotNull(message = "이메일을 반드시 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotNull(message = "비밀번호를 반드시 입력해주세요.")
    private String userPassword;
}
