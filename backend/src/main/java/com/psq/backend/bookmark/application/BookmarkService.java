package com.psq.backend.bookmark.application;

import com.psq.backend.bookmark.domain.Bookmark;
import com.psq.backend.bookmark.dto.request.BookmarkSaveRequest;
import com.psq.backend.bookmark.exception.DuplicatedBookmarkException;
import com.psq.backend.bookmark.exception.NoSuchBookmarkException;
import com.psq.backend.bookmark.persistence.BookmarkRepository;
import com.psq.backend.member.application.MemberService;
import com.psq.backend.member.domain.Member;
import com.psq.backend.problem.application.ProblemService;
import com.psq.backend.problem.domain.Problem;
import com.psq.backend.problem.dto.response.ProblemListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final ProblemService problemService;

    public void save(Member member, BookmarkSaveRequest saveRequest) {
        Problem problem = problemService.getProblem(saveRequest.getProblemId());

        if (bookmarkRepository.isExistedBookmark(member.getId(), problem.getId()))
            throw new DuplicatedBookmarkException();

        bookmarkRepository.save(saveRequest.toEntity(member, problem));
    }

    public void delete(Long id) {
        Bookmark bookmark = bookmarkRepository.findById(id).orElseThrow(
                NoSuchBookmarkException::new
        );
        bookmarkRepository.delete(bookmark);
    }

    public List<ProblemListResponse> getBookmarkList(Member member) {
        List<Bookmark> bookmarkList = bookmarkRepository.findBookmarkByFetchJoin(member.getId());

        if (bookmarkList.isEmpty()) throw new NoSuchBookmarkException();

        return bookmarkList.stream()
                .map(bookmark -> ProblemListResponse.from(bookmark.getProblem()))
                .collect(Collectors.toList());
    }
}
