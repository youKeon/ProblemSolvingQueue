package com.problem.solving.problem.domain;

import com.problem.solving.problem.exception.InvalidProblemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ProblemTest {
    @Test
    @DisplayName("문제를 생성한다")
    public void createProblem() throws Exception {
        //given, when, then
        assertDoesNotThrow(() -> new Problem("test", 1, Category.DFS, false));
    }

    @Test
    @DisplayName("문제 생성 시 url이 공백이면 예외가 발생한다")
    public void createProblemEmptyUrlException() throws Exception {
        //given, when, then
        assertThatThrownBy(() -> new Problem("", 1, Category.DFS, false))
                .isInstanceOf(InvalidProblemException.class);
    }

    @Test
    @DisplayName("문제 생성 시 level이 없으면 예외가 발생한다")
    public void createProblemEmptyLevelException() throws Exception {
        //given, when, then
        assertThatThrownBy(() -> new Problem("test", null, Category.TWO_POINTER, false))
                .isInstanceOf(InvalidProblemException.class);
    }

    @Test
    @DisplayName("문제 생성 시 level이 1보다 작으면 예외가 발생한다")
    public void createProblemLowLevelException() throws Exception {
        //given, when, then
        assertThatThrownBy(() -> new Problem("test", 0, Category.TWO_POINTER, false))
                .isInstanceOf(InvalidProblemException.class);
    }

    @Test
    @DisplayName("문제 생성 시 level이 5보다 크면 예외가 발생한다")
    public void createProblemHighLevelException() throws Exception {
        //given, when, then
        assertThatThrownBy(() -> new Problem("test", 6, Category.TWO_POINTER, false))
                .isInstanceOf(InvalidProblemException.class);
    }
}