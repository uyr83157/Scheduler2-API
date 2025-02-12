package com.example.scheduler2.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleRequestDto {
    @NotNull(message = "제목을 반드시 입력해주세요.")
    @Size(max = 10, message = "일정 제목은 10글자 이내여야 합니다.")
    private String title;

    @NotNull(message = "내용을 반드시 입력해주세요.")
    private String description;

    @NotNull(message = "해당 날짜 및 시간을 반드시 입력해주세요.")
    private LocalDateTime dateTime;

    private Boolean important;
}
