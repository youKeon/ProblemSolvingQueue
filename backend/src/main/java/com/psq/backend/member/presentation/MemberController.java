package com.psq.backend.member.presentation;

import com.psq.backend.member.application.MemberService;
import com.psq.backend.member.domain.Member;
import com.psq.backend.member.dto.request.MemberSignInRequest;
import com.psq.backend.member.dto.request.MemberSignUpRequest;
import com.psq.backend.util.annotation.CurrentUser;
import com.psq.backend.util.annotation.RecommendationCheck;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
    @RecommendationCheck
    public ResponseEntity<Void> signin(@RequestBody @Valid MemberSignInRequest request, HttpSession session) {
        memberService.signin(request, session);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        memberService.logout(session);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 삭제")
    @DeleteMapping
    public ResponseEntity<Void> delete(@CurrentUser Member member) {
        memberService.delete(member);
        return ResponseEntity.noContent().build();
    }
}
