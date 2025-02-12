package com.example.scheduler2.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Builder 패턴 적용 => 서비스에서 setter 대신 객체 생성에 사용
public class UserResponseDto {
    private Long userId;
    private String name;
    private String email;
    private LocalDateTime userCreatedAt;
    private LocalDateTime userUpdatedAt;
}
