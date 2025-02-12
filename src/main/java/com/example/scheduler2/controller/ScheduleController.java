package com.example.scheduler2.controller;

import com.example.scheduler2.dto.ScheduleRequestDto;
import com.example.scheduler2.dto.ScheduleResponseDto;
import com.example.scheduler2.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;


    // 일정 등록
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> postSchedule(@Valid @RequestBody ScheduleRequestDto scheduleRequestDto,
                                                            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ScheduleResponseDto response = scheduleService.addSchedule(userId, scheduleRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 특정 일정 조회
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long scheduleId) {
        ScheduleResponseDto response = scheduleService.findScheduleById(scheduleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 특정 일정 수정
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long scheduleId,
                                                              @Valid @RequestBody ScheduleRequestDto scheduleRequestDto,
                                                              HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ScheduleResponseDto response = scheduleService.updateSchedule(userId, scheduleId, scheduleRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 특정 일정 삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long scheduleId,
                                                 HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        scheduleService.deleteSchedule(userId, scheduleId);
        return new ResponseEntity<>("일정 삭제에 성공했습니다.", HttpStatus.OK);
    }

    // 전체 일정 조회 + 페이징
    // 기존 GetMapping 임에도 RequestBody 받는거 수정 (GetMapping 은 RequestBody 안받는게 일반적)
    // 의문점: 필터링할 항목(필드)이 많아질 경우, RequestBody 를 사용하지 않고 RequestParam 로 하면 엔드포인트가 너무 길어지지 않을까 하는 의문 (혹은 상관 없는 것인지) => 나중에 여쭤보기
    // (GetMapping 이 아니라 PostMapping 써야하나? 그런데 그렇기에는 목적이 생성(Post)이 아니라 조회(Get)임)
    @GetMapping
    public ResponseEntity<Page<ScheduleResponseDto>> getSchedules(
            @RequestParam(required = false) Boolean filterImportant,
            @RequestParam(required = false) Long filterUserId,
            @RequestParam(required = false) Boolean filterFuture,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ScheduleResponseDto> responses = scheduleService.getAllSchedules(filterImportant, filterUserId, filterFuture, page, size);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 페이징의 경우,  Spring Data JPA 의 Pageable/Page 인터페이스를 사용하는데,
    // 만약 기본 페이지 객체의 JSON 표현 방식이 복잡하면, Service 계층에서 Page 객체를 받아 필요한 정보를 커스텀할 수 있음 (DTO 나 Map 으로 변환해서 응답 시킴)
    // => 날것을 보기 편하게 한 번 가공하는 느낌인듯

}
