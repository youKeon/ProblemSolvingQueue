package com.problem.solving.common.annotation;
import com.problem.solving.global.config.JpaConfig;
import com.problem.solving.global.config.QueryDslConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({JpaConfig.class,
        QueryDslConfig.class})
@ActiveProfiles("test")
public abstract class RepositoryTest {
}