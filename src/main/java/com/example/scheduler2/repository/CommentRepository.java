package com.example.scheduler2.repository;

import com.example.scheduler2.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findBySchedule_ScheduleId(Long scheduleId);
    Long countBySchedule_ScheduleId(Long scheduleId);
}
