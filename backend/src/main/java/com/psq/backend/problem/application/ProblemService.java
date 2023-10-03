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

        List<ProblemListResponse> problemList = problemRepository.findAllProblem(memberId, level, category, isSolved, pageable);

        if (problemList.isEmpty()) throw new NoSuchProblemException("문제가 존재하지 않습니다.");
        return problemList;
    }


    public void delete(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(NoSuchProblemException::new);
        problem.softDelete();
    }

    public ProblemResponse getProblemInfo(Long id) {
        return problemRepository.findProblem(id).orElseThrow(NoSuchProblemException::new);
    }

    public ProblemResponse pollProblem(HttpServletRequest request) {
        Long memberId = memberService.getMember(request).getId();
        return problemRepository.pollProblem(memberId).orElseThrow(NoSuchProblemException::new);
    }

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

    public Problem getProblem(Long id) {
        return problemRepository.findById(id).orElseThrow(NoSuchProblemException::new);
    }

    public void increaseSolvedCount(Long id) {
        long increasedCount = problemRepository.increaseSovledCount(id);
        if (increasedCount == 0) throw new NoSuchProblemException();
    }
}
