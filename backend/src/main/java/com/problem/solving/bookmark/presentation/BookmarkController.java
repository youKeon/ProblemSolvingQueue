package com.problem.solving.bookmark.presentation;

import com.problem.solving.bookmark.application.BookmarkService;
import com.problem.solving.bookmark.dto.request.BookmarkSaveRequest;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Operation(summary = "북마크 등록")
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid BookmarkSaveRequest request) {
        bookmarkService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "북마크 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookmarkService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @Operation(summary = "북마크 리스트 조회")
    @GetMapping("/{id}")
    public ResponseEntity<List<ProblemListResponse>> getBookmarkList(@PathVariable Long id) {
        List<ProblemListResponse> response = bookmarkService.getBookmarkList(id);
        return ResponseEntity.ok(response);
    }
}
