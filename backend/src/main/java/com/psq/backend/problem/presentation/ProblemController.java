package com.psq.backend.problem.presentation;

import com.psq.backend.member.domain.CurrentUser;
import com.psq.backend.member.domain.Member;
import com.psq.backend.problem.application.ProblemService;
import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.dto.request.ProblemSaveRequest;
import com.psq.backend.problem.dto.request.ProblemUpdateRequest;
import com.psq.backend.problem.dto.response.ProblemListResponse;
import com.psq.backend.problem.dto.response.ProblemRecommendResponse;
import com.psq.backend.problem.dto.response.ProblemResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problems")
public class ProblemController {

    private final ProblemService problemService;
    @Operation(summary = "문제 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> getProblem(@PathVariable Long id) {
        ProblemResponse response = problemService.getProblemInfo(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문제 추천")
    @GetMapping("/recommendation")
    public ResponseEntity<List<ProblemRecommendResponse>> recommendProblem(@CurrentUser Member member) {
        List<ProblemRecommendResponse> response = problemService.recommendProblem(member.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문제 조회")
    @GetMapping
    public ResponseEntity<List<ProblemListResponse>> getProblemList(@CurrentUser Member member,
                                                                    @RequestParam(required = false) Integer level,
                                                                    @RequestParam(required = false) Category category,
                                                                    @RequestParam(required = false) Boolean isSolved,
                                                                    Pageable pageable) {
        List<ProblemListResponse> response = problemService.getProblemList(member, level, category, isSolved, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "가장 먼저 등록한 문제 조회")
    @GetMapping("/poll")
    public ResponseEntity<ProblemResponse> pollProblem(@CurrentUser Member member) {
        ProblemResponse response = problemService.pollProblem(member);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문제 등록")
    @PostMapping
    public ResponseEntity<Void> save(@CurrentUser Member member,
                                     @RequestBody @Valid ProblemSaveRequest request) {
        problemService.save(member, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "문제 삭제(isDeleted -> true)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        problemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "문제 되돌리기(isDeleted -> false)")
    @PutMapping("/{id}/recovery")
    public ResponseEntity<Void> recovery(@PathVariable Long id) {
        problemService.recovery(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "문제 수정")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody @Valid ProblemUpdateRequest request) {
        problemService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "문제 풀이 횟수 증가")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> increaseSolvedCount(@PathVariable Long id) {
        problemService.increaseSolvedCount(id);
        return ResponseEntity.noContent().build();
    }
}
