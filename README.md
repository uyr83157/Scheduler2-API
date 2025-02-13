# 일정 관리2 API (Spring Boot + MySQL)
이 프로젝트는 Spring Boot와 MySQL을 사용하여 개발된 일정 관리 API으로, 기존의 일정 관리 API(Scheduler-API)를 보완하여 만들었습니다. 사용자는 일정을 생성, 조회, 수정, 삭제할 수 있으며, 필터링과 정렬 기능을 활용하여 원하는 데이터를 조회할 수 있습니다. 추가로 일정뿐 아니라 사용자 데이터도 관리할 수 있습니다. 필터링 값은 인덱스 처리하여 빠르게 필터링 가능합니다.

## 사용한 주요 기술 스택
- Java 17+
- Spring Boot 3.4.2 
- Spring Data JPA
- JPA Auditing
- Lombok 
- MySQL
- BCrypt (at.favre.lib:bcrypt:0.10.2)

## 주요 변경점
- DB 관리 방법을 JDBC에서 JPA로 전환
- 댓글 기능 추가
- 회원가입, 로그인, 세션 기반 인증 추가 (기존 아이디+패스워드 검증 방법에서 세션 검증 방법으로 변경)
- 비밀번호 암호화 추가
- `@Builder`을 사용하여 서비스 계층에서의 setter 사용 최소화
- 전체 일정 조회의 `GetMapping` 부분에서 `@RequestBody` 요청받는 것을 `@RequestParam`으로 변경
- 페이징 구현에 `Pageable/Page` 인터페이스 사용
- `Bean Validation`으로 검증 애너테이션 적용

## 프로젝트 구조
```
src/
 ├── main/
 │    ├── java/com/example/scheduler/
 │    │     ├── config/             # JPA Auditing, PasswordEncoder, AuthenticationFilter 등 설정
 │    │     ├── controller/         # REST API 엔드포인트 컨트롤러
 │    │     ├── dto/                # 요청/응답 DTO 클래스
 │    │     ├── entity/             # JPA 엔티티
 │    │     ├── exception/          # 커스텀 예외 클래스 및 전역 예외 처리
 │    │     ├── repository/         # Spring Data JPA Repository 인터페이스
 │    │     └── service/            # 비즈니스 로직
 │    └── resources/
 │         └── application.properties
 └── test/
```

## API 목록
### 사용자 API (`/users`)
| 기능 | HTTP 메서드 | 엔드포인트 | 설명 |
|------|------------|------------|------|
| 사용자 가입 | `POST` | `/users` | 새 사용자 등록 |
| 사용자 로그인 | `POST` | `/users/login` | 특정 사용자 로그인 |
| 사용자 수정 | `PATCH` | `/users/{userAccount}` | 특정 사용자 수정 (세션 인증) |
| 사용자 삭제 | `DELETE` | `/users` | 특정 사용자 삭제 (세션 인증) |

### 일정 API (`/schedules`)
| 기능 | HTTP 메서드 | 엔드포인트 | 설명 |
|------|------------|------------|------|
| 일정 등록 | `POST` | `/schedules` | 새 일정 추가 (세션 인증) |
| 일정 조회 | `GET` | `/schedules/{scheduleId}` | 특정 일정 조회 (세션 인증) |
| 일정 수정 | `PATCH` | `/schedules/{scheduleId}` | 특정 일정 수정 (세션 인증) |
| 일정 삭제 | `DELETE` | `/schedules/{scheduleId}` | 특정 일정 삭제 (세션 인증) |
| 일정 목록 조회 | `GET` | `/schedules?RequestParam` | 전체 일정 조회 (필터 및 페이징 지원, 세션인증) |

### 댓글 API (`/users`)
| 기능 | HTTP 메서드 | 엔드포인트 | 설명 |
|------|------------|------------|------|
| 댓글 작성 | `POST` | `/comments/schedule/{scheduleId}` | 특정 일정에 댓글 작성 (세션 인증) |
| 댓글 조회 | `GET` | `/comments/schedule/{scheduleId}` | 특정 일정에서의 모든 댓글 조회 (세션 인증) |
| 댓글 수정 | `PATCH` | `/comments/{commentId}` | 특정 댓글 수정 (세션 인증) |
| 댓글 삭제 | `DELETE` | `/comments/{commentId}` | 특정 댓글 삭제 (세션 인증) |

## API 상세 명세
### 사용자 가입
- 요청
  ```
  {
  "name": "이름",
  "email": "email@example.com",
  "userPassword": "패스워드"
  }
  ```
- 응답
  ```
  {
  "userId": 1,
  "name": "이름",
  "email": "email@example.com",
  "userCreatedAt": "2025-02-12T10:00:00",
  "userUpdatedAt": "2025-02-12T10:00:00"
  }
  ```

### 사용자 로그인
- 요청
  ```
  {
  "email": "email@example.com",
  "userPassword": "패스워드"
  }
  ```
- 응답
  ```
  {
  "userId": 1,
  "name": "이름",
  "email": "email@example.com",
  "userCreatedAt": "2025-02-12T10:00:00",
  "userUpdatedAt": "2025-02-12T10:00:00"
  }
  ```

### 사용자 수정
- 요청
  ```
  {
  "name": "바꿀 이름",
  "userPassword": "기존 패스워드", // 검증용
  "newPassword": "바꿀 패스워드"
  }
  ```
- 응답
  ```
   {
  "userId": 1,
  "name": "바꾼 이름",
  "email": "email@example.com",
  "userCreatedAt": "2025-02-12T10:00:00",
  "userUpdatedAt": "2025-02-13T10:00:00"
  }
  ```

### 사용자 삭제
- 요청
  ```
  없음
  ```
- 응답
  ```
  "사용자 정보 삭제에 성공했습니다."
  ```
  
### 일정 등록
- 요청
  ```
  {
  "title": "일정 제목",
  "description": "일정 내용",
  "dateTime": "2025-02-13T14:00:00",
  "important": true
  }

  ```
- 응답
  ```
  {
  "scheduleId": 1,
  "userId": 1,
  "title": "일정 제목",
  "description": "일정 내용e",
  "dateTime": "2025-02-13T14:00:00",
  "important": true,
  "createdAt": "2025-02-12T10:10:00",
  "updatedAt": "2025-02-12T10:10:00",
  "commentCount": 0
  }
  ```

### 일정 조회
- 요청
  ```
  없음
  ```
- 응답
  ```
  {
  "scheduleId": 1,
  "userId": 1,
  "title": "일정 제목",
  "description": "일정 내용",
  "dateTime": "2025-02-13T14:00:00",
  "important": true,
  "createdAt": "2025-02-12T10:10:00",
  "updatedAt": "2025-02-12T10:10:00",
  "commentCount": 0
  }
  ```

### 일정 수정
- 요청
  ```
  {
  "title": "수정할 일정 제목",
  "description": "수정할 일정 내용",
  "dateTime": "2025-02-13T16:00:00",
  "important": false
  }
  ```
- 응답
  ```
  {
  "scheduleId": 1,
  "userId": 1,
  "title": "수정된 일정 제목",
  "description": "수정된 일정 내용",
  "dateTime": "2025-02-13T16:00:00",
  "important": false,
  "createdAt": "2025-02-12T10:10:00",
  "updatedAt": "2025-02-13T10:10:00",
  "commentCount": 0
  }
  ```

### 일정 삭제
- 요청
  ```
  없음
  ```
- 응답
  ```
  "일정 삭제에 성공했습니다."
  ```

일정 목록 조회
- 요청 (URL)
  ```
  http://localhost:8080/schedules?filterImportant=true&filterUserId=1&filterFuture=true&page=0&size=10
  ```
- 응답
  ```
  {
    "content": [
        {
            일정1
        },
        {
            일정2
        }.
        {
            ... 일정 목록
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": {
            "sorted": true,
            "empty": false,
            "unsorted": false
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 2,
    "first": true,
    "numberOfElements": 2,
    "size": 10,
    "number": 0,
    "sort": {
        "sorted": true,
        "empty": false,
        "unsorted": false
    },
    "empty": false
  }
  ```

### 댓글 작성
- 요청
  ```
  {
  "content": "댓글 내용"
  }
  ```
- 응답
  ```
  {
  "commentId": 1,
  "scheduleId": 1,
  "userId": 1,
  "content": "댓글 내용",
  "createdAt": "2025-02-12T10:20:00",
  "updatedAt": "2025-02-12T10:20:00"
  }
  ```

### 댓글 조회
- 요청
  ```
  없음
  ```
- 응답
  ```
  [
  {
    "commentId": 1,
    "scheduleId": 1,
    "userId": 1,
    "content": "댓글 내용",
    "createdAt": "2025-02-12T10:20:00",
    "updatedAt": "2025-02-12T10:20:00"
  },
  {
    ... 댓글 목록
  }
  ]
  ```

### 댓글 수정
- 요청
  ```
  {
  "content": "수정할 댓글 내용"
  }
  ```
- 응답
  ```
  {
  "commentId": 1,
  "scheduleId": 1,
  "userId": 1,
  "content": "수정된 댓글 내용",
  "createdAt": "2025-02-12T10:20:00",
  "updatedAt": "2025-02-12T10:20:00"
  }
  ```

### 댓글 삭제
- 요청
  ```
  없음
  ```
- 응답
  ```
  "댓글 삭제에 성공했습니다."
  ```
