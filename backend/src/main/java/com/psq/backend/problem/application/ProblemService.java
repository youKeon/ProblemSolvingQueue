package com.psq.backend.problem.application;

import com.psq.backend.member.domain.Member;
import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.domain.Problem;
import com.psq.backend.problem.dto.request.ProblemSaveRequest;
import com.psq.backend.problem.dto.request.ProblemUpdateRequest;
import com.psq.backend.problem.dto.response.ProblemListResponse;
import com.psq.backend.problem.dto.response.ProblemRecommendResponse;
import com.psq.backend.problem.dto.response.ProblemResponse;
import com.psq.backend.problem.exception.InvalidProblemException;
import com.psq.backend.problem.exception.NoSuchProblemException;
import com.psq.backend.problem.persistence.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;

    public void save(Member member,
                     ProblemSaveRequest request) {

        Problem problem = request.toEntity(member);
        problemRepository.save(problem);
    }

    @Cacheable(value = "getProblemList",
            key = "#member.id + '_' + #level + '_' + #category + '_' + #isSolved + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public List<ProblemListResponse> getProblemList(Member member,
                                                    Integer level,
                                                    Category category,
                                                    Boolean isSolved,
                                                    Pageable pageable) {


        List<ProblemListResponse> problemList = problemRepository.findAllProblem(member.getId(), level, category, isSolved, pageable);

        if (problemList.isEmpty()) throw new NoSuchProblemException("문제가 존재하지 않습니다.");
        return problemList;
    }


    public void delete(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(NoSuchProblemException::new);
        problem.softDelete();
    }

    @Transactional(readOnly = true)
    public ProblemResponse getProblemInfo(Long id) {
        return problemRepository.findProblem(id).orElseThrow(NoSuchProblemException::new);
    }

    @Transactional(readOnly = true)
    public ProblemResponse pollProblem(Member member) {
        return problemRepository.pollProblem(member.getId()).orElseThrow(NoSuchProblemException::new);
    }

    @CachePut
    public void update(Long id,
                       ProblemUpdateRequest request) {

        Problem problem = problemRepository.findById(id).orElseThrow(NoSuchProblemException::new);
        problem.update(request);
    }

    public void recovery(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(NoSuchProblemException::new);
        if (!problem.isDeleted()) throw new InvalidProblemException("삭제되지 않은 문제입니다.");
        problem.recovery();
    }

    @Transactional(readOnly = true)
    public Problem getProblem(Long id) {
        return problemRepository.findById(id).orElseThrow(NoSuchProblemException::new);
    }

    public void increaseSolvedCount(Long id) {
        long increasedCount = problemRepository.increaseSolvedCount(id);
        if (increasedCount == 0) throw new NoSuchProblemException();
    }

    public List<ProblemRecommendResponse> recommendProblem(Long memberId) {
        List<ProblemRecommendResponse> recommendProblemList = problemRepository.recommendProblem(memberId);
        if (recommendProblemList.isEmpty()) throw new NoSuchProblemException();
        return recommendProblemList;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    protected void deleteSoftProblem() {
        log.info("[문제 완전 삭제 메서드 실행]");
        long deletedProblemCount = problemRepository.deleteSoftDeletedProblem();
        log.info("삭제된 문제 개수 : {}", deletedProblemCount);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    protected void initializeRecommendedProblem() {
        log.info("[문제 추천 여부 초기화 메서드 실행]");
        problemRepository.initializeRecommendedProblem();
    }
}