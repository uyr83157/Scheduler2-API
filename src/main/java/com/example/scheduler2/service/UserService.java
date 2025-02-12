package com.example.scheduler2.service;

import com.example.scheduler2.config.PasswordEncoder;
import com.example.scheduler2.dto.UserLoginRequestDto;
import com.example.scheduler2.dto.UserPatchRequestDto;
import com.example.scheduler2.dto.UserRequestDto;
import com.example.scheduler2.dto.UserResponseDto;
import com.example.scheduler2.entity.User;
import com.example.scheduler2.exception.AuthenticationException;
import com.example.scheduler2.exception.DuplicateEmailException;
import com.example.scheduler2.exception.DuplicateUserAccountException;
import com.example.scheduler2.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        // 이메일 중복 체크
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new DuplicateEmailException("이미 사용중인 이메일 입니다.");
        }

        // 비밀번호 암호화 후 User 엔티티 생성 (Builder 패턴 사용)
        User user = User.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .userPassword(passwordEncoder.encode(userRequestDto.getUserPassword()))
                .build();
        userRepository.save(user);
        return toUserResponseDto(user);
    }

    @Transactional
    public UserResponseDto login(UserLoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new AuthenticationException("이메일 또는 비밀번호가 일치하지 않습니다."));
        if (!passwordEncoder.matches(loginRequestDto.getUserPassword(), user.getUserPassword())) {
            throw new AuthenticationException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        return toUserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateUser(Long userId, UserPatchRequestDto patchRequestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(patchRequestDto.getUserPassword(), user.getUserPassword())) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }
        if (patchRequestDto.getName() != null && !patchRequestDto.getName().isEmpty()) {
            user.setName(patchRequestDto.getName());
        }
        if (patchRequestDto.getNewPassword() != null && !patchRequestDto.getNewPassword().isEmpty()) {
            user.setUserPassword(passwordEncoder.encode(patchRequestDto.getNewPassword()));
        }
        userRepository.save(user);
        return toUserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationException("사용자를 찾을 수 없습니다."));
        userRepository.delete(user);
    }

    // User 를 UserResponseDto 로 변환할 때 Builder 패턴 사용
    private UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .userCreatedAt(user.getUserCreatedAt())
                .userUpdatedAt(user.getUserUpdatedAt())
                .build();
    }
}
