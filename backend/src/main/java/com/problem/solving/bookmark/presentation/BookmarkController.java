package com.problem.solving.bookmark.presentation;

import com.problem.solving.bookmark.application.BookmarkService;
import com.problem.solving.bookmark.dto.request.BookmarkSaveRequest;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import com.problem.solving.problem.dto.response.ProblemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Valid BookmarkSaveRequest request) {
        bookmarkService.register(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookmarkService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ProblemListResponse>> getBookmarkList(@PathVariable Long id) {
        List<ProblemListResponse> response = bookmarkService.getBookmarkList(id);
        return ResponseEntity.ok(response);
    }
}
