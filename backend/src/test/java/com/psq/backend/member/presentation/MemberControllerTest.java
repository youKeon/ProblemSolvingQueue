package com.psq.backend.member.presentation;

import com.psq.backend.common.annotation.ControllerTest;
import com.psq.backend.member.domain.Member;
import com.psq.backend.member.dto.request.MemberSignInRequest;
import com.psq.backend.member.dto.request.MemberSignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends ControllerTest {
    private static final String baseURL = "/api/v1/members";

    @BeforeEach
    void setup() {
        member = new Member("yukeon97@gmail.com", "123", "salt");
        ReflectionTestUtils.setField(member, "id", 1L);

        session = new MockHttpSession();
    }

    @Test
    @DisplayName("이메일과 비밀번호를 받아 회원가입을 한다")
    public void signUpMember() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest(member.getEmail(), member.getPassword());

        // when, then
        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 시 이메일 형식이 올바르지 않은 경우 예외가 발생한다")
    public void signUpMemberInvalidEmailFormatException() throws Exception {
        // given
        String 잘못된_이메일_형식 = "InvaildEmailFormat";
        MemberSignUpRequest request = new MemberSignUpRequest(잘못된_이메일_형식, "1234");

        // when, then
        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 시 이메일이 공백인 경우 예외가 발생한다")
    public void signUpMemberInvalidEmailEmptyException() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("", "1234");

        // when, then
        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 시 비밀번호가 공백인 경우 예외가 발생한다")
    public void signUpMemberInvalidPasswordEmptyException() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest(member.getEmail(), "");

        // when, then
        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일과 비밀번호를 받아 로그인을 한다")
    public void signInMember() throws Exception {
        // given
        MemberSignInRequest request = new MemberSignInRequest(member.getEmail(), member.getPassword());

        // when, then
        mockMvc.perform(post(baseURL + "/signin")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 시 이메일 형식이 올바르지 않은 경우 예외가 발생한다")
    public void signInMemberInvalidEmailFormatException() throws Exception {
        // given
        String 잘못된_이메일_형식 = "InvaildEmailFormat";
        MemberSignUpRequest request = new MemberSignUpRequest(잘못된_이메일_형식, "1234");

        // when, then
        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 시 이메일이 공백인 경우 예외가 발생한다")
    public void signInMemberEmptyEmailException() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("", "1234");

        // when, then
        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 시 비밀번호가 공백인 경우 예외가 발생한다")
    public void signInMemberEmptyPasswordException() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest(member.getEmail(), "");

        // when, then
        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
