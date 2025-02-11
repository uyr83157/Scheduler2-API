package com.example.scheduler2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    // Member 와 연관관계매핑
    // fetch = FetchType.LAZY: 지연 로딩(Lazy Loading)
    // 연관된 엔티티를 처음 조회할 때 바로 가져오지 않고, 실제로 해당 연관 객체를 사용할 때 데이터베이스에서 조회하는 방식 (fetch 전략)
    // => 불필요한 데이터 로딩을 방지하여 애플리케이션의 효율성을 높여줌
    // 반대는 '즉시 로딩(EAGER Loading)' = EAGER (fetch 기본값)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 외래키 컬럼 지정
    private Member member;

}
