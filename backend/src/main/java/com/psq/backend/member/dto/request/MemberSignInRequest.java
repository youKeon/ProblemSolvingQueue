package com.psq.backend.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class MemberSignInRequest {
    @NotBlank(message = "공백일 수 없습니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;
    @NotBlank(message = "공백일 수 없습니다.")
    private String password;
}
