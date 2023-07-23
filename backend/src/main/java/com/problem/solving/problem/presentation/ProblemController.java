package com.problem.solving.problem.presentation;

import com.problem.solving.problem.application.ProblemService;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.response.ProblemsResponse;
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

    @GetMapping()
    public ResponseEntity<List<ProblemsResponse>> getProblem() {
        List<ProblemsResponse> response = problemService.getProblems();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Valid ProblemSaveRequest request) {
        problemService.register(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        problemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
