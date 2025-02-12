package com.example.scheduler2.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    @NotNull(message = "댓글 내용을 반드시 입력해주세요.")
    private String content;
}
