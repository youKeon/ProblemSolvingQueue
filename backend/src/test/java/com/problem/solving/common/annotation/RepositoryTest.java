package com.problem.solving.common.annotation;
import com.problem.solving.config.JpaConfig;
import com.problem.solving.config.QueryDslConfig;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.domain.Problem;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({JpaConfig.class,
        QueryDslConfig.class})
@ActiveProfiles("test")
public abstract class RepositoryTest {
}