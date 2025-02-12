package com.example.scheduler2.controller;

import com.example.scheduler2.dto.UserLoginRequestDto;
import com.example.scheduler2.dto.UserPatchRequestDto;
import com.example.scheduler2.dto.UserRequestDto;
import com.example.scheduler2.dto.UserResponseDto;
import com.example.scheduler2.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    // 서비스 생성자는 @RequiredArgsConstructor 에 의해 주입됨.
    private final UserService userService;


    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto response = userService.addUser(userRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 로그인 성공 시 세션에 userId 저장
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody UserLoginRequestDto loginRequestDto,
                                                 HttpSession session) {
        UserResponseDto user = userService.login(loginRequestDto);
        session.setAttribute("userId", user.getUserId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // 유저 정보 수정
    @PatchMapping
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserPatchRequestDto patchRequestDto,
                                                      HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserResponseDto response = userService.updateUser(userId, patchRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        userService.deleteUser(userId);
        session.invalidate();
        return new ResponseEntity<>("사용자 정보 삭제에 성공했습니다.", HttpStatus.OK);
    }

}
