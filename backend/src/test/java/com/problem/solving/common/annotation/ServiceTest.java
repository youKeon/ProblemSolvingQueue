package com.problem.solving.common.annotation;

import com.problem.solving.bookmark.application.BookmarkService;
import com.problem.solving.bookmark.domain.Bookmark;
import com.problem.solving.bookmark.persistence.BookmarkRepository;
import com.problem.solving.member.application.MemberService;
import com.problem.solving.member.domain.Member;
import com.problem.solving.member.domain.SessionInfo;
import com.problem.solving.member.persistence.MemberRepository;
import com.problem.solving.problem.application.ProblemService;
import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.persistence.ProblemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
@ExtendWith(MockitoExtension.class)
@Transactional
public abstract class ServiceTest {
    @InjectMocks
    protected BookmarkService bookmarkService;
    @InjectMocks
    protected MemberService memberService;
    @InjectMocks
    protected ProblemService problemService;
    @Mock
    protected BookmarkRepository bookmarkRepository;
    @Mock
    protected ProblemRepository problemRepository;
    @Mock
    protected MemberRepository memberRepository;
    @Mock
    protected HttpServletRequest request;

    protected SessionInfo sessionInfo;
    protected static Pageable pageable = PageRequest.of(0, 3);

    protected Member member;
    protected Problem problem1;
    protected Problem problem2;
    protected Problem problem3;
    protected Bookmark bookmark;
}
