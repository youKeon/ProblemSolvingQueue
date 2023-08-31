package com.psq.backend.batch;

import com.psq.backend.member.domain.Member;
import com.psq.backend.member.persistence.MemberRepository;
import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.domain.Problem;
import com.psq.backend.problem.persistence.ProblemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBatchTest
@ActiveProfiles("test")
@SpringBootTest
public class RemoveDeletedProblemTest {

    @Autowired
    private BatchScheduler batchScheduler;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private Job problemDeleteJob;

    @AfterEach
    public void cleanup() {
        problemRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @BeforeEach
    public void setup() {
        Member member = new Member("google@naver.kakao", "password", "salt");
        memberRepository.save(member);

        LocalDateTime dateTime = LocalDateTime.of(2023, Month.AUGUST, 14, 0, 0);

        // 삭제 대상 Problem 데이터 7개 생성
        for (int i = 0; i < 7; i++) {
            Problem problem = new Problem(member, "title" + i, "url" + i, i % 5 + 1, Category.DFS, false);
            problemRepository.save(problem);
            problem.softDelete();
            ReflectionTestUtils.setField(problem, "updatedAt", dateTime);
            problemRepository.save(problem);
        }

        // 일반 Problem 데이터 5개 생성
        for (int i = 0; i < 5; i++) {
            problemRepository.save(new Problem(member, "title" + (i + 7), "url" + (i + 7), i % 5 + 1, Category.DFS, false));
        }
    }

    @Test
    @DisplayName("BatchScheduler를 통해 problemDeleteJob을 실행시킨다")
    public void removeDeletedProblemJobTest() throws Exception {
        // when
        batchScheduler.deleteProblemJob();
        List<Problem> actual = problemRepository.findAll();

        // then
        assertThat(actual).hasSize(5);
    }

    @Test
    @DisplayName("삭제된지 3일이 지난 문제는 완전 삭제된다")
    public void removeDeletedProblemTest() throws Exception {
        // when
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(problemDeleteJob, jobParameters);

        List<Problem> actual = problemRepository.findAll();

        // then 전체 문제(12) - 삭제된 문제(7) = 5
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(actual).hasSize(5);
    }
}
