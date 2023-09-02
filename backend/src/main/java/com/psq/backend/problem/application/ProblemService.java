package com.psq.backend.problem.application;

import com.psq.backend.member.application.MemberService;
import com.psq.backend.member.domain.Member;
import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.domain.Problem;
import com.psq.backend.problem.dto.request.ProblemSaveRequest;
import com.psq.backend.problem.dto.request.ProblemUpdateRequest;
import com.psq.backend.problem.dto.response.ProblemListResponse;
import com.psq.backend.problem.dto.response.ProblemResponse;
import com.psq.backend.problem.exception.InvalidProblemException;
import com.psq.backend.problem.exception.NoSuchProblemException;
import com.psq.backend.problem.persistence.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final MemberService memberService;

    public void save(HttpServletRequest servletRequest,
                     ProblemSaveRequest request) {

        Member member = memberService.getMember(servletRequest);
        Problem problem = request.toEntity(member);
        problemRepository.save(problem);
    }

    public List<ProblemListResponse> getProblemList(HttpServletRequest request,
                                                    Integer level,
                                                    Category category,
                                                    Boolean isSolved,
                                                    Pageable pageable) {


        Long memberId = memberService.getMember(request).getId();

        List<Problem> problemList = problemRepository.findAllProblem(memberId, level, category, isSolved, pageable);

        if (problemList.size() == 0) throw new NoSuchProblemException("문제가 존재하지 않습니다.");

        return problemList.stream()
                .map(ProblemListResponse::from)
                .collect(Collectors.toList());
    }


    public void delete(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException()
        );
        problem.softDelete();
    }

    public ProblemResponse getProblemInfo(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException()
        );
        return ProblemResponse.from(problem);
    }

    public ProblemResponse pollProblem(HttpServletRequest request) {
        Long memberId = memberService.getMember(request).getId();

        Problem problem = problemRepository.pollProblem(memberId).orElseThrow(
                () -> new NoSuchProblemException()
        );
        return ProblemResponse.from(problem);
    }

    public void update(Long id,
                       ProblemUpdateRequest request) {

        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException()
        );
        problem.update(request);
    }

    public void recovery(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException()
        );
        if (!problem.isDeleted()) throw new InvalidProblemException("삭제되지 않은 문제입니다.");
        problem.recovery();
    }

    public Problem getProblem(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException()
        );
        return problem;
    }
}
