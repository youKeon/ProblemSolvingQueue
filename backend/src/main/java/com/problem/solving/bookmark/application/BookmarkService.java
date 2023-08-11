package com.problem.solving.bookmark.application;

import com.problem.solving.bookmark.domain.Bookmark;
import com.problem.solving.bookmark.dto.request.BookmarkSaveRequest;
import com.problem.solving.bookmark.exception.DuplicatedBookmarkException;
import com.problem.solving.bookmark.exception.NoSuchBookmarkException;
import com.problem.solving.bookmark.persistence.BookmarkRepository;
import com.problem.solving.member.domain.Member;
import com.problem.solving.member.exception.NoSuchMemberException;
import com.problem.solving.member.persistence.MemberRepository;
import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import com.problem.solving.problem.exception.NoSuchProblemException;
import com.problem.solving.problem.persistence.ProblemRepository;
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
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;

    public void save(BookmarkSaveRequest request) {
        Member member = memberRepository.findById(request.getMemberId()).orElseThrow(
                () -> new NoSuchMemberException()
        );

        Problem problem = problemRepository.findById(request.getProblemId()).orElseThrow(
                () -> new NoSuchProblemException()
        );

        if (bookmarkRepository.existsBookmarkByMember_IdAndProblem_Id(member.getId(), problem.getId()))
            throw new DuplicatedBookmarkException();

        bookmarkRepository.save(request.toEntity(member, problem));
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
