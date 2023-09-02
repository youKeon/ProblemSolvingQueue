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

import static com.psq.backend.common.docs.ApiDocumentUtil.getDocumentRequest;
import static com.psq.backend.common.docs.ApiDocumentUtil.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        MemberSignUpRequest request = new MemberSignUpRequest("email@email.com", "password");

        // when, then
        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())

                .andDo(print())
                .andDo(document("member/signUp/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        )
                ));
    }

    @Test
    @DisplayName("회원가입 시 이메일 형식이 올바르지 않은 경우 예외가 발생한다")
    public void signUpMemberInvalidEmailFormatException() throws Exception {
        // given
        String 잘못된_이메일_형식 = "InvalidEmailFormat";
        MemberSignUpRequest request = new MemberSignUpRequest(잘못된_이메일_형식, "1234");

        // when, then
        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email 필드는 올바른 이메일 형식이어야 합니다. (전달된 값: InvalidEmailFormat)"))

                .andDo(print())
                .andDo(document("member/signUp/fail/invalidEmail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email 필드는 공백일 수 없습니다. (전달된 값: )"))

                .andDo(print())
                .andDo(document("member/signUp/fail/emptyEmail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }

    @Test
    @DisplayName("회원가입 시 비밀번호가 공백인 경우 예외가 발생한다")
    public void signUpMemberInvalidPasswordEmptyException() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("email@email.com", "");

        // when, then
        mockMvc.perform(post(baseURL + "/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("password 필드는 공백일 수 없습니다. (전달된 값: )"))

                .andDo(print())
                .andDo(document("member/signUp/fail/emptyPassword",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
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
                .andExpect(status().isOk())

                .andDo(print())
                .andDo(document("member/signIn/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        )
                ));
    }

    @Test
    @DisplayName("로그인 시 이메일 형식이 올바르지 않은 경우 예외가 발생한다")
    public void signInMemberInvalidEmailFormatException() throws Exception {
        // given
        String 잘못된_이메일_형식 = "InvaildEmailFormat";
        MemberSignUpRequest request = new MemberSignUpRequest(잘못된_이메일_형식, "1234");

        // when, then
        mockMvc.perform(post(baseURL + "/signin")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email 필드는 올바른 이메일 형식이어야 합니다. (전달된 값: InvaildEmailFormat)"))

                .andDo(print())
                .andDo(document("member/signIn/fail/invalidEmail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }

    @Test
    @DisplayName("로그인 시 이메일이 공백인 경우 예외가 발생한다")
    public void signInMemberEmptyEmailException() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("", "1234");

        // when, then
        mockMvc.perform(post(baseURL + "/signin")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email 필드는 공백일 수 없습니다. (전달된 값: )"))
                
                .andDo(print())
                .andDo(document("member/signIn/fail/emptyEmail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }

    @Test
    @DisplayName("로그인 시 비밀번호가 공백인 경우 예외가 발생한다")
    public void signInMemberEmptyPasswordException() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest(member.getEmail(), "");

        // when, then
        mockMvc.perform(post(baseURL + "/signin")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("password 필드는 공백일 수 없습니다. (전달된 값: )"))
                
                .andDo(print())
                .andDo(document("member/signIn/fail/emptyPassword",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }
}
