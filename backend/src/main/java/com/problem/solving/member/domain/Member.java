package com.problem.solving.member.domain;

import com.problem.solving.common.BaseEntity;
import com.problem.solving.member.exception.InvalidEmailException;
import com.problem.solving.problem.exception.InvalidProblemException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private void validateEmail(String email) {
        if (email.length() == 0) {
            throw new InvalidEmailException("Email은 공백일 수 없습니다.");
        }
    }

    private void validatePassword(String password) {
        if (password.length() == 0) {
            throw new InvalidEmailException("Password는 공백일 수 없습니다.");
        }
    }
}
