package com.problem.solving.member.domain;

import com.problem.solving.member.exception.InvalidMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MemberTest {

    @Test
    @DisplayName("유저를 만든다")
    public void createMember() throws Exception {
        //given, when, then
        assertDoesNotThrow(() -> new Member("yukeon97@gmail.com", "123"));
    }

    @Test
    @DisplayName("유저 생성 시 email이 공백이면 예외가 발생한다")
    public void createMemberEmptyEmailException() throws Exception {
        //given, when, then
        assertThatThrownBy(() -> new Member("", "123"))
                .isInstanceOf(InvalidMemberException.class);
    }

    @Test
    @DisplayName("유저 생성 시 password가 공백이면 예외가 발생한다")
    public void createMemberEmptyPasswordException() throws Exception {
        //given, when, then
        assertThatThrownBy(() -> new Member("yukeon97@gmail.com", ""))
                .isInstanceOf(InvalidMemberException.class);
    }
}
