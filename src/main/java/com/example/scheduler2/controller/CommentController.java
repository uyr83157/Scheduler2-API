package com.example.scheduler2.controller;

import com.example.scheduler2.dto.CommentPatchRequestDto;
import com.example.scheduler2.dto.CommentRequestDto;
import com.example.scheduler2.dto.CommentResponseDto;
import com.example.scheduler2.service.CommentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("/schedule/{scheduleId}")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long scheduleId,
                                                         @Valid @RequestBody CommentRequestDto commentRequestDto,
                                                         HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        CommentResponseDto response = commentService.addComment(userId, scheduleId, commentRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 특정 일정의 댓글 전체 조회
    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long scheduleId) {
        List<CommentResponseDto> responses = commentService.getCommentsBySchedule(scheduleId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 특정 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
                                                            @Valid @RequestBody CommentPatchRequestDto patchRequestDto,
                                                            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        CommentResponseDto response = commentService.updateComment(userId, commentId, patchRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 특정 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        commentService.deleteComment(userId, commentId);
        return new ResponseEntity<>("댓글 삭제에 성공했습니다.", HttpStatus.OK);
    }

}
