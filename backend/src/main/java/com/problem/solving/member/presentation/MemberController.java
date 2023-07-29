package com.problem.solving.member.presentation;

import com.problem.solving.member.application.MemberService;
import com.problem.solving.member.dto.request.MemberSignUpRequest;
import com.problem.solving.problem.dto.response.ProblemListResponse;
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

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid MemberSignUpRequest request) {
        memberService.signup(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/problems")
    public ResponseEntity<List<ProblemListResponse>> getProblemList(@PathVariable Long id,
                                                                    Pageable pageable) {
        List<ProblemListResponse> response = memberService.getProblemList(id, pageable);
        return ResponseEntity.ok(response);
    }
}
