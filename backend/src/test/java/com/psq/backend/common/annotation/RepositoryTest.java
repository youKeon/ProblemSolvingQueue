package com.psq.backend.common.annotation;
import com.psq.backend.bookmark.persistence.BookmarkRepository;
import com.psq.backend.global.config.JpaConfig;
import com.psq.backend.global.config.QueryDslConfig;
import com.psq.backend.member.persistence.MemberRepository;
import com.psq.backend.problem.persistence.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({
        JpaConfig.class,
        QueryDslConfig.class
})
@ActiveProfiles("test")
public abstract class RepositoryTest {
    @Autowired
    protected ProblemRepository problemRepository;
    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected BookmarkRepository bookmarkRepository;

}