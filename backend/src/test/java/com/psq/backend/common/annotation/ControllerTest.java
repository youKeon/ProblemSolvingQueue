package com.psq.backend.common.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psq.backend.bookmark.application.BookmarkService;
import com.psq.backend.bookmark.presentation.BookmarkController;
import com.psq.backend.member.application.MemberService;
import com.psq.backend.member.domain.Member;
import com.psq.backend.member.presentation.MemberController;
import com.psq.backend.problem.application.ProblemService;
import com.psq.backend.problem.presentation.ProblemController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;

@WebMvcTest({ProblemController.class,
        MemberController.class,
        BookmarkController.class})
@ActiveProfiles("test")
public abstract class ControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected ProblemService problemService;
    @MockBean
    protected MemberService memberService;
    @MockBean
    protected BookmarkService bookmarkService;

    protected Member member;
    protected static Pageable pageable = PageRequest.of(0, 3);
    protected MockHttpSession session;
    protected HttpServletRequest request;
}
