package com.psq.backend.problem.presentation;

import com.psq.backend.problem.application.ProblemService;
import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.dto.request.ProblemSaveRequest;
import com.psq.backend.problem.dto.request.ProblemUpdateRequest;
import com.psq.backend.problem.dto.response.ProblemListResponse;
import com.psq.backend.problem.dto.response.ProblemResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        ProblemResponse response = problemService.getProblem(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문제 조회")
    @GetMapping("/problems")
    public ResponseEntity<List<ProblemListResponse>> getProblemList(HttpServletRequest request,
                                                                    @RequestParam(required = false) Integer level,
                                                                    @RequestParam(required = false) Category category,
                                                                    @RequestParam(required = false) Boolean isSolved,
                                                                    Pageable pageable) {
        List<ProblemListResponse> response = problemService.getProblemList(request, level, category, isSolved, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "가장 먼저 등록한 문제 조회")
    @GetMapping("/poll")
    public ResponseEntity<ProblemResponse> pollProblem(HttpServletRequest request) {
        ProblemResponse response = problemService.pollProblem(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문제 등록")
    @PostMapping
    public ResponseEntity<Void> save(HttpServletRequest servletRequest,
                                     @RequestBody @Valid ProblemSaveRequest request) {
        problemService.save(servletRequest, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "문제 삭제(isDeleted -> true)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        problemService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "문제 되돌리기(isDeleted -> false)")
    @PutMapping("/{id}/recovery")
    public ResponseEntity<Void> recovery(@PathVariable Long id) {
        problemService.recovery(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "문제 수정")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody @Valid ProblemUpdateRequest request) {
        problemService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
