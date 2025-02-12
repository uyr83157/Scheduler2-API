package com.example.scheduler2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// @EntityListeners(AuditingEntityListener.class)
// 여러 엔티티에 createdAt, updatedAt 항목이 공통으로 들어가기 때문에 BaseEntity 부모 엔티티를 만들고
// createdAt, updatedAt 를 사용하는 자식 엔티티에 상속시키면 리팩터링 가능함 (@EntityListeners 매번 사용하지 않아도 됨)
@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Size(max = 4, message = "이름은 4글자 이내여야 합니다.")
    @Column(nullable = false)
    private String name;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "이메일 형식이 올바르지 않습니다.")
    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String userPassword;

    @CreatedDate
    private LocalDateTime userCreatedAt;

    @LastModifiedDate
    private LocalDateTime userUpdatedAt;


}
