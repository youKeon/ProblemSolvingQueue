package com.psq.backend.member.domain;

import com.psq.backend.common.BaseEntity;
import com.psq.backend.member.exception.InvalidEmailFormatException;
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

    @Column(nullable = false)
    private boolean isRecommended;

    @Column(nullable = false)
    private String salt;

    public Member(String email,
                  String password,
                  String salt) {
        validateEmail(email);
        validatePassword(password);

        this.email = email;
        this.password = password;
        this.salt = salt;
        this.isRecommended = false;
    }

    private void validateEmail(String email) {
        if (email.isEmpty()) throw new InvalidEmailFormatException("Email은 공백일 수 없습니다.");
    }

    private void validatePassword(String password) {
        if (password.isEmpty()) throw new InvalidEmailFormatException("Password는 공백일 수 없습니다.");
    }

    public void hadRecommendation() {
        this.isRecommended = true;
    }
}

