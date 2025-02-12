package com.example.scheduler2.service;

import com.example.scheduler2.dto.ScheduleRequestDto;
import com.example.scheduler2.dto.ScheduleResponseDto;
import com.example.scheduler2.entity.Schedule;
import com.example.scheduler2.entity.User;
import com.example.scheduler2.exception.AuthenticationException;
import com.example.scheduler2.repository.CommentRepository;
import com.example.scheduler2.repository.ScheduleRepository;
import com.example.scheduler2.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ScheduleResponseDto addSchedule(Long userId, ScheduleRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationException("사용자를 찾을 수 없습니다."));
        // Builder 사용해 Schedule 엔티티 생성
        Schedule schedule = Schedule.builder()
                .user(user)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .dateTime(requestDto.getDateTime())
                .important(requestDto.getImportant() != null ? requestDto.getImportant() : false)
                .build();
        scheduleRepository.save(schedule);
        return toScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto findScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("해당 일정이 존재하지 않습니다."));
        return toScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long userId, Long scheduleId, ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("해당 일정이 존재하지 않습니다."));
        if (!schedule.getUser().getUserId().equals(userId)) {
            throw new AuthenticationException("본인의 일정만 수정할 수 있습니다.");
        }
        if (requestDto.getTitle() != null && !requestDto.getTitle().trim().isEmpty()) {
            schedule.setTitle(requestDto.getTitle());
        }
        if (requestDto.getDescription() != null && !requestDto.getDescription().trim().isEmpty()) {
            schedule.setDescription(requestDto.getDescription());
        }
        if (requestDto.getDateTime() != null) {
            schedule.setDateTime(requestDto.getDateTime());
        }
        if (requestDto.getImportant() != null) {
            schedule.setImportant(requestDto.getImportant());
        }
        scheduleRepository.save(schedule);
        return toScheduleResponseDto(schedule);
    }

    @Transactional
    public void deleteSchedule(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("해당 일정이 존재하지 않습니다."));
        if (!schedule.getUser().getUserId().equals(userId)) {
            throw new AuthenticationException("본인의 일정만 삭제할 수 있습니다.");
        }
        scheduleRepository.delete(schedule);
    }

    @Transactional
    public Page<ScheduleResponseDto> getAllSchedules(Boolean filterImportant, Long filterUserId, Boolean filterFuture, int page, int size) {
        // 페이징 + 수정일 기준 내림차순 정렬을 적용
        Pageable pageable = PageRequest.of(page, size, org.springframework.data.domain.Sort.by("updatedAt").descending());
        Page<Schedule> schedulePage = scheduleRepository.findAll(pageable);
        return schedulePage.map(this::toScheduleResponseDto);
    }

    // Schedule 를 ScheduleResponseDto 로 변환할 때 Builder 패턴 사용
    // => Setter 없이 변환 가능
    private ScheduleResponseDto toScheduleResponseDto(Schedule schedule) {
        Long commentCount = commentRepository.countBySchedule_ScheduleId(schedule.getScheduleId());
        return ScheduleResponseDto.builder()
                .scheduleId(schedule.getScheduleId())
                .userId(schedule.getUser().getUserId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .dateTime(schedule.getDateTime())
                .important(schedule.isImportant())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .commentCount(commentCount)
                .build();
    }

}
