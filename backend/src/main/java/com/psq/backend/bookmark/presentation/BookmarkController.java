package com.psq.backend.bookmark.presentation;

import com.psq.backend.bookmark.application.BookmarkService;
import com.psq.backend.bookmark.dto.request.BookmarkSaveRequest;
import com.psq.backend.problem.dto.response.ProblemListResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Operation(summary = "북마크 등록")
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid BookmarkSaveRequest saveRequest,
                                     HttpServletRequest request) {
        bookmarkService.save(request, saveRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "북마크 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookmarkService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "북마크 리스트 조회")
    @GetMapping
    public ResponseEntity<List<ProblemListResponse>> getBookmarkList(HttpServletRequest request) {
        List<ProblemListResponse> response = bookmarkService.getBookmarkList(request);
        return ResponseEntity.ok(response);
    }
}
