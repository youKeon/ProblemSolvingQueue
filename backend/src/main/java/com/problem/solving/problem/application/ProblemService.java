package com.problem.solving.problem.application;

import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.response.ProblemsResponse;
import com.problem.solving.problem.exception.NoSuchProblemException;
import com.problem.solving.problem.persistence.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;

    public void register(ProblemSaveRequest request) {
        Problem problem = request.toEntity();
        problemRepository.save(problem);
    }

    public List<ProblemsResponse> getProblems() {
        List<Problem> problems = problemRepository.findAllProblems();
        return problems.stream()
                .map(ProblemsResponse::from)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException("문제를 찾을 수 없습니다.")
        );
        problem.softDelete();
    }
}
