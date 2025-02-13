package com.example.scheduler2.service;

import com.example.scheduler2.dto.CommentPatchRequestDto;
import com.example.scheduler2.dto.CommentRequestDto;
import com.example.scheduler2.dto.CommentResponseDto;
import com.example.scheduler2.entity.Comment;
import com.example.scheduler2.entity.Schedule;
import com.example.scheduler2.entity.User;
import com.example.scheduler2.exception.AuthenticationException;
import com.example.scheduler2.repository.CommentRepository;
import com.example.scheduler2.repository.ScheduleRepository;
import com.example.scheduler2.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    // 댓글 작성
    @Transactional
    public CommentResponseDto addComment(Long userId, Long scheduleId, CommentRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationException("사용자를 찾을 수 없습니다."));
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다."));
        Comment comment = Comment.builder()
                .user(user)
                .schedule(schedule)
                .content(requestDto.getContent())
                .build();
        commentRepository.save(comment);
        return toCommentResponseDto(comment);
    }

    // 댓글 조회
    @Transactional
    public List<CommentResponseDto> getCommentsBySchedule(Long scheduleId) {
        List<Comment> comments = commentRepository.findBySchedule_ScheduleId(scheduleId);
        return comments.stream().map(this::toCommentResponseDto).collect(Collectors.toList());
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long userId, Long commentId, CommentPatchRequestDto patchRequestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new AuthenticationException("본인의 댓글만 수정할 수 있습니다.");
        }
        comment.setContent(patchRequestDto.getContent());
        commentRepository.save(comment);
        return toCommentResponseDto(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new AuthenticationException("본인의 댓글만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }

    // Comment -> CommentResponseDto 로 변환 (Builder)
    private CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .scheduleId(comment.getSchedule().getScheduleId())
                .userId(comment.getUser().getUserId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
