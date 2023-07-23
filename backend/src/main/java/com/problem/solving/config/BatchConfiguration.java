package com.problem.solving.config;

import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.persistence.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final ProblemRepository problemRepository;


    @Bean
    public RepositoryItemReader<Problem> problemsDeleteReader() {
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("created_at", Sort.Direction.ASC);

        RepositoryItemReader<Problem> reader = new RepositoryItemReader<>();
        reader.setRepository(problemRepository);
        reader.setMethodName("findDeletedProblems");
        reader.setPageSize(30);
        reader.setSort(sorts);
        return reader;
    }

    @Bean
    public ItemWriter<Problem> problemsDeletionWriter() {
        return items -> problemRepository.deleteAll(items);
    }

    @Bean
    public Step problemsDeleteStep(ItemReader<Problem> reader, ItemWriter<Problem> writer) {
        return stepBuilderFactory.get("problemsDeleteStep")
                .<Problem, Problem>chunk(30)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Job problemsDeleteJob(Step step) {
        return jobBuilderFactory.get("problemsDeleteJob")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }
}