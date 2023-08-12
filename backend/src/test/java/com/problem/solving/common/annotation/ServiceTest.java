package com.problem.solving.common.annotation;

import com.problem.solving.bookmark.application.BookmarkService;
import com.problem.solving.bookmark.domain.Bookmark;
import com.problem.solving.bookmark.persistence.BookmarkRepository;
import com.problem.solving.member.application.MemberService;
import com.problem.solving.member.domain.Member;
import com.problem.solving.member.domain.SessionInfo;
import com.problem.solving.member.persistence.MemberRepository;
import com.problem.solving.member.util.PasswordUtil;
import com.problem.solving.problem.application.ProblemService;
import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.persistence.ProblemRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static com.problem.solving.member.util.PasswordUtil.encodePassword;
import static com.problem.solving.member.util.PasswordUtil.generateSalt;

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
    protected PasswordUtil passwordUtil;
    @Mock
    protected ProblemRepository problemRepository;
    @Mock
    protected MemberRepository memberRepository;
    @Mock
    protected HttpServletRequest request;

    protected static Pageable pageable = PageRequest.of(0, 3);
    protected SessionInfo sessionInfo;
    protected MockHttpSession session;

    protected Member member;
    protected Problem problem1;
    protected Problem problem2;
    protected Problem problem3;
    protected Bookmark bookmark;
    protected String password = "1234";
    protected String salt = generateSalt();
    protected String encodePassword = encodePassword(password, salt);
}
