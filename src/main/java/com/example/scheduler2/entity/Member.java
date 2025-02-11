package com.example.scheduler2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// @EntityListeners(AuditingEntityListener.class)
// 여러 엔티티에 createdAt, updatedAt 항목이 공통으로 들어가기 때문에 BaseEntity 부모 엔티티를 만들고
// createdAt, updatedAt 를 사용하는 자식 엔티티에 상속시키면 리팩터링 가능함 (@EntityListeners 매번 사용하지 않아도 됨)
@Entity
@Getter
@Setter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;


}
