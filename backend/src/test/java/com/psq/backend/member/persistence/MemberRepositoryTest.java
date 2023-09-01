package com.psq.backend.member.persistence;

import com.psq.backend.common.annotation.RepositoryTest;
import com.psq.backend.member.domain.Member;
import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.domain.Problem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


public class MemberRepositoryTest extends RepositoryTest {
    private Member member;

    @BeforeEach
    void setup() {
        member = memberRepository.save(new Member("yukeon97@gmail.com", "123", "salt"));
    }

    @Test
    @DisplayName("이메일로 사용자를 찾는다")
    public void findByEmailTest() {
        // given
        String email = "yukeon97@gmail.com";

        // when
        Optional<Member> actual = memberRepository.findByEmail(email);

        // then
        assertThat(actual.get().getEmail()).isEqualTo(member.getEmail());
        assertThat(actual.get().getPassword()).isEqualTo(member.getPassword());
    }
    
    @Test
    @DisplayName("이미 등록된 이메일을 조회하는 경우 true를 반환한다")
    public void existsEmail() throws Exception {
        //given
        String email = "yukeon97@gmail.com";

        // when
        boolean actual = memberRepository.existsMemberByEmail(email);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("신규 이메일을 조회하는 경우 false를 반환한다")
    public void notExistsEmail() throws Exception {
        //given
        String email = "yukeon97@naver.com";

        // when
        boolean actual = memberRepository.existsMemberByEmail(email);

        // then
        assertThat(actual).isFalse();
    }

}
