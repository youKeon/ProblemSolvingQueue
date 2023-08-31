package com.psq.backend.bookmark.application;

import com.psq.backend.bookmark.domain.Bookmark;
import com.psq.backend.bookmark.dto.request.BookmarkSaveRequest;
import com.psq.backend.bookmark.exception.DuplicatedBookmarkException;
import com.psq.backend.bookmark.exception.NoSuchBookmarkException;
import com.psq.backend.bookmark.persistence.BookmarkRepository;
import com.psq.backend.member.application.MemberService;
import com.psq.backend.member.domain.Member;
import com.psq.backend.member.domain.SessionInfo;
import com.psq.backend.member.exception.NoSuchMemberException;
import com.psq.backend.member.persistence.MemberRepository;
import com.psq.backend.problem.domain.Problem;
import com.psq.backend.problem.dto.response.ProblemListResponse;
import com.psq.backend.problem.exception.NoSuchProblemException;
import com.psq.backend.problem.persistence.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;

    public void save(HttpServletRequest request, BookmarkSaveRequest saveRequest) {

        SessionInfo sessionInfo = memberService.getSessionInfo(request);
        Long memberId = sessionInfo.getId();

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NoSuchMemberException()
        );

        Problem problem = problemRepository.findById(saveRequest.getProblemId()).orElseThrow(
                () -> new NoSuchProblemException()
        );

        if (bookmarkRepository.isExistedBookmark(member.getId(), problem.getId()))
            throw new DuplicatedBookmarkException();

        bookmarkRepository.save(saveRequest.toEntity(member, problem));
    }

    public void delete(Long id) {
        Bookmark bookmark = bookmarkRepository.findById(id).orElseThrow(
                () -> new NoSuchBookmarkException()
        );
        bookmarkRepository.delete(bookmark);
    }

    public List<ProblemListResponse> getBookmarkList(Long id) {
        List<Bookmark> bookmarkList = bookmarkRepository.findBookmarkByFetchJoin(id);

        if (bookmarkList.isEmpty()) throw new NoSuchBookmarkException();

        return bookmarkList.stream()
                .map(bookmark -> ProblemListResponse.from(bookmark.getProblem()))
                .collect(Collectors.toList());
    }
}
