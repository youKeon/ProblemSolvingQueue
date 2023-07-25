package com.problem.solving.common.annotation;

import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.persistence.ProblemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public abstract class ServiceTest {

    @Autowired protected ProblemRepository problemRepository;

    @BeforeEach
    void setup() {
        problemRepository.save(new Problem("test", 1, Category.DFS, false));
    }
}
