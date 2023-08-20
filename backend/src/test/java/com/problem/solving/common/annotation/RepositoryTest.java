package com.problem.solving.common.annotation;
import com.problem.solving.bookmark.persistence.BookmarkRepository;
import com.problem.solving.global.config.JpaConfig;
import com.problem.solving.global.config.QueryDslConfig;
import com.problem.solving.member.persistence.MemberRepository;
import com.problem.solving.problem.persistence.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({JpaConfig.class,
        QueryDslConfig.class})
@ActiveProfiles("test")
public abstract class RepositoryTest {
    @Autowired
    protected ProblemRepository problemRepository;
    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected BookmarkRepository bookmarkRepository;

}