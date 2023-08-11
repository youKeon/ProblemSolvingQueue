package com.problem.solving.member.presentation;

import com.problem.solving.member.application.MemberService;
import com.problem.solving.member.dto.request.MemberSignUpRequest;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;
    @Operation(summary = "sign up", description = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid MemberSignUpRequest request) {
        memberService.signup(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "get problem list", description = "문제 조회")
    @GetMapping("/{id}/problems")
    public ResponseEntity<List<ProblemListResponse>> getProblemList(@PathVariable Long id,
                                                                    @RequestParam(required = false) Integer level,
                                                                    @RequestParam(required = false) Category category,
                                                                    @RequestParam(required = false) Boolean isSolved,
                                                                    Pageable pageable) {
        List<ProblemListResponse> response = memberService.getProblemList(id, level, category, isSolved, pageable);
        return ResponseEntity.ok(response);
    }
}
