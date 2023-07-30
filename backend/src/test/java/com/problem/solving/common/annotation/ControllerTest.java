package com.problem.solving.common.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.problem.solving.bookmark.application.BookmarkService;
import com.problem.solving.bookmark.presentation.BookmarkController;
import com.problem.solving.member.application.MemberService;
import com.problem.solving.member.presentation.MemberController;
import com.problem.solving.problem.application.ProblemService;
import com.problem.solving.problem.presentation.ProblemController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
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
}
