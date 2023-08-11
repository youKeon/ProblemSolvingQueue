package com.problem.solving.problem.presentation;

import com.problem.solving.problem.application.ProblemService;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.request.ProblemUpdateRequest;
import com.problem.solving.problem.dto.response.ProblemResponse;
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

    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> getProblem(@PathVariable Long id) {
        ProblemResponse response = problemService.getProblem(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/poll")
    public ResponseEntity<ProblemResponse> pollProblem() {
        ProblemResponse response = problemService.pollProblem();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> addProblem(@RequestBody @Valid ProblemSaveRequest request) {
        problemService.addProblem(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        problemService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}/recovery")
    public ResponseEntity<Void> recovery(@PathVariable Long id) {
        problemService.recovery(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody @Valid ProblemUpdateRequest request) {
        problemService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
