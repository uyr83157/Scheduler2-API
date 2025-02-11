package com.example.scheduler2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // JPA Auditing 설정: 작성일(createdAt), 수정일(updatedAt) 같은 날짜 필드 자동 관리
@SpringBootApplication
public class Scheduler2Application {

    public static void main(String[] args) {
        SpringApplication.run(Scheduler2Application.class, args);
    }

}
