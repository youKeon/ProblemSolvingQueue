package com.problem.solving.member.presentation;

import com.problem.solving.common.annotation.ControllerTest;
import com.problem.solving.member.domain.Member;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends ControllerTest {
    private static final String baseURL = "/api/v1/members";
    private static final Pageable pageable = PageRequest.of(0, 3);
    private Member member;
    @BeforeEach
    void setup() {
        member = new Member("yukeon97@gmail.com", "123");
    }

    @Test
    @DisplayName("문제 리스트를 조회된다")
    public void getProblems() throws Exception {
        //given
        Long memberId = 1L;
        ProblemListResponse test1 = new ProblemListResponse("test1", 1, Category.DFS, false);
        ProblemListResponse test2 = new ProblemListResponse("test2", 2, Category.DFS, false);
        ProblemListResponse test3 = new ProblemListResponse("test3", 3, Category.DFS, false);
        List<ProblemListResponse> responses = Arrays.asList(test1, test2, test3);

        given(memberService.getProblemList(memberId, pageable)).willReturn(responses);

        mockMvc.perform(get(baseURL + "/{id}/problems", memberId)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("이메일과 비밀번호를 받아 회원가입을 한다")
    public void joinMember() throws Exception {
        //given

        MemberSignUpRequest request = new MemberSignUpRequest("email@gmail.com", "1234");
        willDoNothing()
                .given(memberService)
                .signup(request);

        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("회원가입 시 이메일 형식이 올바르지 않은 경우 예외가 발생한다")
    public void joinMemberInvalidEmailFormatException() throws Exception {
        //given
        MemberSignUpRequest request = new MemberSignUpRequest("email", "1234");

        willThrow(new InvalidMemberException("올바른 이메일 형식이어야 합니다."))
                .given(memberService)
                .signup(request);

        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 시 이메일이 공백인 경우 예외가 발생한다")
    public void joinMemberInvalidEmailEmptyException() throws Exception {
        //given
        MemberSignUpRequest request = new MemberSignUpRequest("", "1234");

        willThrow(new InvalidMemberException("공백일 수 없습니다."))
                .given(memberService)
                .signup(request);

        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 시 비밀번호가 공백인 경우 예외가 발생한다")
    public void joinMemberInvalidPasswordEmptyException() throws Exception {
        //given
        MemberSignUpRequest request = new MemberSignUpRequest("email@gmail.com", "");

        willThrow(new InvalidMemberException("공백일 수 없습니다."))
                .given(memberService)
                .signup(request);

        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
