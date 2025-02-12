package com.example.scheduler2.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPatchRequestDto {

    @NotNull(message = "현재 사용중인 비밀번호를 반드시 입력해주세요.")
    private String userPassword;
    // 인증용
    
    @Size(max = 4, message = "이름은 4글자 이내여야 합니다.")
    private String name; // 선택 업데이트
    
    private String newPassword; // 선택 업데이트
}
