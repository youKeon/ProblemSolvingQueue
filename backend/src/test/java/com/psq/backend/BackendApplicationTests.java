package com.psq.backend;

import com.psq.backend.member.domain.Member;
import com.psq.backend.member.persistence.MemberRepository;
import com.psq.backend.problem.application.ProblemService;
import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.domain.Problem;
import com.psq.backend.problem.persistence.ProblemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
//@ActiveProfiles("test")
class BackendApplicationTests {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProblemService problemService;

    @Autowired
    ProblemRepository problemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void contextLoads() {
    }

    @Test @Transactional
    public void concurrencyTEST() throws Exception {
        Member member = memberRepository.save(new Member("email@naver.com", "password", "slat"));
        Problem problem = problemRepository.save(new Problem(member, "title", "url", 3, Category.DFS, false));

        int threadCount = 32; // 원하는 스레드 수
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++){
            service.submit(()->{
                try{
                    problemService.increaseSolvedCount(problem.getId()); //문제의 메서드 호출
                } finally {
                    latch.countDown(); //완료되었음을 알림
                }
            });
        }

        latch.await();

        Integer solvedCount = problemRepository.findById(problem.getId()).get().getSolvedCount();

        System.out.println(solvedCount);
    }
}