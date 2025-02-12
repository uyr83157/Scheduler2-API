package com.example.scheduler2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "schedule")
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    // User 와 연관관계매핑
    // 여러 일정이 하나의 User 에 속함
    // fetch = FetchType.LAZY: 지연 로딩(Lazy Loading)
    // 연관된 엔티티를 처음 조회할 때 바로 가져오지 않고, 실제로 해당 연관 객체를 사용할 때 데이터베이스에서 조회하는 방식 (fetch 전략)
    // => 불필요한 데이터 로딩을 방지하여 애플리케이션의 효율성을 높여줌
    // 반대는 '즉시 로딩(EAGER Loading)' = EAGER (fetch 기본값)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    @Size(max = 10, message = "제목은 10글자 이내여야 합니다.")
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    private boolean important;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;




}
