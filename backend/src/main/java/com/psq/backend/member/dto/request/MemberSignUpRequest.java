package com.psq.backend.member.dto.request;

import com.psq.backend.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class MemberSignUpRequest {
    @NotBlank(message = "공백일 수 없습니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @NotBlank(message = "공백일 수 없습니다.")
    @Length(min = 8, max = 15)
    private String password;

    public Member toEntity(String encodedPassword,
                           String salt) {
        return new Member(email, encodedPassword, salt);
    }
}
