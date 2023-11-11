package com.psq.backend.member.domain;

import com.psq.backend.member.exception.InvalidEmailFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MemberTest {

    @Test
    @DisplayName("유저를 만든다")
    public void createMemberTest() throws Exception {
        // given, when, then
        assertDoesNotThrow(() -> new Member("yukeon97@gmail.com", "123", "salt"));
    }

    @Test
    @DisplayName("유저 생성 시 email이 공백이면 예외가 발생한다")
    public void createMemberEmptyEmailExceptionTest() throws Exception {
        // given, when, then
        assertThatThrownBy(() -> new Member("", "123", "salt"))
                .isInstanceOf(InvalidEmailFormatException.class)
                .hasMessageContaining("Email은 공백일 수 없습니다.");
    }


    @Test
    @DisplayName("유저 생성 시 password가 공백이면 예외가 발생한다")
    public void createMemberEmptyPasswordExceptionTest() throws Exception {
        // given, when, then
        assertThatThrownBy(() -> new Member("yukeon97@gmail.com", "", "salt"))
                .isInstanceOf(InvalidEmailFormatException.class)
                .hasMessageContaining("Password는 공백일 수 없습니다.");
    }
}
