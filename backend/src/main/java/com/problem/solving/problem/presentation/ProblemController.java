package com.problem.solving.problem.presentation;

import com.problem.solving.problem.application.ProblemService;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.response.ProblemResponse;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    @GetMapping()
    public ResponseEntity<List<ProblemListResponse>> getProblemList() {
        List<ProblemListResponse> response = problemService.getProblemList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> addProblem(@RequestBody @Valid ProblemSaveRequest request) {
        problemService.addProblem(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        problemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
