package com.problem.solving.problem.application;

import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.request.ProblemUpdateRequest;
import com.problem.solving.problem.dto.response.ProblemResponse;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import com.problem.solving.problem.exception.NoSuchProblemException;
import com.problem.solving.problem.persistence.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;

    public void addProblem(ProblemSaveRequest request) {
        Problem problem = request.toEntity();
        problemRepository.save(problem);
    }

    public List<ProblemListResponse> getProblemList(Pageable pageable) {
        Page<Problem> problemList = problemRepository.findAllProblem(pageable);
        if (problemList.getNumberOfElements() == 0) throw new NoSuchProblemException("문제가 없습니다.");

        return problemList.stream()
                .map(ProblemListResponse::from)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException("문제를 찾을 수 없습니다.")
        );
        problem.softDelete();
    }

    public ProblemResponse getProblem(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException("문제를 찾을 수 없습니다.")
        );
        return ProblemResponse.from(problem);
    }

    public ProblemResponse pollProblem() {
        Problem problem = problemRepository.findFirstByOrderByCreatedAtAsc().orElseThrow(
                () -> new NoSuchProblemException("문제를 찾을 수 없습니다.")
        );
        return ProblemResponse.from(problem);
    }

    public void update(Long id,
                       ProblemUpdateRequest request) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException("문제를 찾을 수 없습니다.")
        );
        problem.update(request);
    }
}
