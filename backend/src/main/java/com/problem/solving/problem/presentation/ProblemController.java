package com.problem.solving.problem.presentation;

import com.problem.solving.problem.application.ProblemService;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.request.ProblemUpdateRequest;
import com.problem.solving.problem.dto.response.ProblemResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problems")
public class ProblemController {

    private final ProblemService problemService;
    @Operation(summary = "get problem", description = "문제 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> getProblem(@PathVariable Long id) {
        ProblemResponse response = problemService.getProblem(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "poll problem", description = "가장 먼저 등록한 문제 조회")
    @GetMapping("/poll")
    public ResponseEntity<ProblemResponse> pollProblem() {
        ProblemResponse response = problemService.pollProblem();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "add problem", description = "문제 등록")
    @PostMapping
    public ResponseEntity<Void> addProblem(@RequestBody @Valid ProblemSaveRequest request) {
        problemService.addProblem(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "delete problem", description = "문제 삭제(isDeleted -> true)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        problemService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "recovery problem", description = "문제 되돌리기(isDeleted -> false)")
    @PutMapping("/{id}/recovery")
    public ResponseEntity<Void> recovery(@PathVariable Long id) {
        problemService.recovery(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "update problem", description = "문제 수정")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody @Valid ProblemUpdateRequest request) {
        problemService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
