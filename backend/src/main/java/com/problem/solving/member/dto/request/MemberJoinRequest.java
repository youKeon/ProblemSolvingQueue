package com.problem.solving.member.dto.request;

import com.problem.solving.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class MemberJoinRequest {
    @NotBlank(message = "공백일 수 없습니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;
    @NotBlank(message = "공백일 수 없습니다.")
    private String password;

    public Member toEntity() {
        return new Member(email, password);
    }

}
