package com.psq.backend.batch;

import com.psq.backend.problem.persistence.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ProblemRepository problemRepository;

    @Bean
    public Job removeDeletedProblemJob() {
        Job job = jobBuilderFactory.get("job")
                .start(removeDeletedProblemStep())
                .build();
        return job;
    }

    @Bean
    public Step removeDeletedProblemStep() {
        return stepBuilderFactory.get("step")
                .tasklet((contribution, chunkContext) -> {
                    log.info("[문제 완전 삭제] step start");
                    long deletedCount = problemRepository.deleteSoftDeletedProblem();
                    log.info("[문제 완전 삭제] 삭제된 문제 : {} 문제", deletedCount);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}