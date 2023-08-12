package com.problem.solving.member.presentation;

import com.problem.solving.member.application.MemberService;
import com.problem.solving.member.domain.SessionInfo;
import com.problem.solving.member.dto.request.MemberSignInRequest;
import com.problem.solving.member.dto.request.MemberSignUpRequest;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid MemberSignUpRequest request) {
        memberService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "로그인")
    @PostMapping("/signin")
    public ResponseEntity<Void> signin(@RequestBody @Valid MemberSignInRequest request, HttpSession session) {
        SessionInfo sessionInfo = memberService.createSessionInfo(request);
        session.setAttribute("sessionInfo", sessionInfo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "문제 조회")
    @GetMapping("/problems")
    public ResponseEntity<List<ProblemListResponse>> getProblemList(HttpServletRequest request,
                                                                    @RequestParam(required = false) Integer level,
                                                                    @RequestParam(required = false) Category category,
                                                                    @RequestParam(required = false) Boolean isSolved,
                                                                    Pageable pageable) {
        List<ProblemListResponse> response = memberService.getProblemList(request, level, category, isSolved, pageable);
        return ResponseEntity.ok(response);
    }
}
