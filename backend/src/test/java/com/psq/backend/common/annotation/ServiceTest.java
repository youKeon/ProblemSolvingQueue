package com.psq.backend.common.annotation;

import com.psq.backend.bookmark.application.BookmarkService;
import com.psq.backend.bookmark.domain.Bookmark;
import com.psq.backend.bookmark.persistence.BookmarkRepository;
import com.psq.backend.member.application.MemberService;
import com.psq.backend.member.domain.Member;
import com.psq.backend.member.persistence.MemberRepository;
import com.psq.backend.problem.application.ProblemService;
import com.psq.backend.problem.domain.Problem;
import com.psq.backend.problem.persistence.ProblemRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static com.psq.backend.util.PasswordUtil.*;
import static com.psq.backend.util.PasswordUtil.generateSalt;


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
    protected MemberRepository memberRepository;
    @Mock
    protected ProblemRepository problemRepository;
    @Mock
    protected HttpServletRequest request;

    protected static Pageable pageable = PageRequest.of(0, 3);

    protected Member member;
    protected Problem problem1;
    protected Problem problem2;
    protected Problem problem3;
    protected Bookmark bookmark;
    protected String password = "1234";
    protected String salt = generateSalt();
    protected String encodePassword = encodePassword(password, salt);
}
