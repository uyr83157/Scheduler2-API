package com.example.scheduler2.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleResponseDto {
    private Long scheduleId;
    private Long userId;
    private String title;
    private String description;
    private LocalDateTime dateTime;
    private boolean important;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long commentCount;
}
