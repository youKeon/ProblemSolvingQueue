package com.problem.solving.member.presentation;

import com.problem.solving.common.annotation.ControllerTest;
import com.problem.solving.member.domain.Member;
import com.problem.solving.member.domain.SessionInfo;
import com.problem.solving.member.dto.request.MemberSignUpRequest;
import com.problem.solving.member.exception.InvalidMemberException;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends ControllerTest {
    private static final String baseURL = "/api/v1/members";

    @BeforeEach
    void setup() {
        member = new Member("yukeon97@gmail.com", "123");
        ReflectionTestUtils.setField(member, "id", 1L);
    }

    @Test
    @DisplayName("이메일과 비밀번호를 받아 회원가입을 한다")
    public void joinMember() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("email@gmail.com", "1234");
        willDoNothing()
                .given(memberService)
                .signup(request);

        // when, then
        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 시 이메일 형식이 올바르지 않은 경우 예외가 발생한다")
    public void joinMemberInvalidEmailFormatException() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("email", "1234");

        // when, then
        willThrow(new InvalidMemberException("올바른 이메일 형식이어야 합니다."))
                .given(memberService)
                .signup(request);

        // then
        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 시 이메일이 공백인 경우 예외가 발생한다")
    public void joinMemberInvalidEmailEmptyException() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("", "1234");

        // when
        willThrow(new InvalidMemberException("공백일 수 없습니다."))
                .given(memberService)
                .signup(request);

        // then
        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 시 비밀번호가 공백인 경우 예외가 발생한다")
    public void joinMemberInvalidPasswordEmptyException() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("email@gmail.com", "");

        // when
        willThrow(new InvalidMemberException("공백일 수 없습니다."))
                .given(memberService)
                .signup(request);

        // then
        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
