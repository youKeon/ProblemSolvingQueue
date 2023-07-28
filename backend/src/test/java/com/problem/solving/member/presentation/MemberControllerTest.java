package com.problem.solving.member.presentation;

import com.problem.solving.common.annotation.ControllerTest;
import com.problem.solving.member.domain.Member;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
