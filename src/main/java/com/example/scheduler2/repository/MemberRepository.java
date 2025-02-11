package com.example.scheduler2.repository;


import com.example.scheduler2.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


// JpaRepository 을 사용하면 기본적으로 JPA 에서 CRUD 를 지원함 (JpaRepository<Entity, PK 타입>)
// => 기존 JDBC 처럼 SQL 쿼리문을 하나하나 작성할 필요 없음
public interface MemberRepository extends JpaRepository<Member, Long> {

    // Optional: 빈 값을 처리하는 방식
    // => null 대신 Optional.empty() 을 반환시켜 NullPointerException 방지
    // findByEmail (이메일) 로 회원 정보를 조회할 때, 해당 회원 정보 유무를 체크
    Optional<Member> findByEmail(String email);

    // 이메일 + 패스워드로 회원 정보 조회 => 로그인 방식
    Optional<Member> findByEmailAndPassword(String email, String password);

}
