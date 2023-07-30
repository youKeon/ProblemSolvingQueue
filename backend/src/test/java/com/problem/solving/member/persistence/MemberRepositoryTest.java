package com.problem.solving.member.persistence;

import com.problem.solving.common.annotation.RepositoryTest;
import com.problem.solving.member.domain.Member;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.domain.Problem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.mysema.commons.lang.Assert.assertThat;

public class MemberRepositoryTest extends RepositoryTest {
    private Problem problem1;
    private Problem problem2;
    private Member member;

    @BeforeEach
    void setup() {
        member = memberRepository.save(new Member("yukeon97@gmail.com", "123"));
        problem1 = problemRepository.save(
                new Problem(
                        this.member,
                        "title",
                        "test",
                        1,
                        Category.DFS,
                        false));

        problem2 = problemRepository.save(
                new Problem(
                        this.member,
                        "title",
                        "test",
                        1,
                        Category.DFS,
                        false));
    }
    @Test
    @DisplayName("이메일로 사용자를 찾는다")
    public void findByEmailTest() {
        // given
        String email = "yukeon97@gmail.com";

        // when
        Member actual = memberRepository.findByEmail(email);

        // then
        Assertions.assertThat(actual.getEmail()).isEqualTo(member.getEmail());
        Assertions.assertThat(actual.getPassword()).isEqualTo(member.getPassword());
    }
    
    @Test
    @DisplayName("이미 등록된 이메일을 조회하는 경우 true를 반환한다")
    public void existsEmail() throws Exception {
        //given
        String email = "yukeon97@gmail.com";

        // when
        boolean actual = memberRepository.existsMemberByEmail(email);

        // then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("신규 이메일을 조회하는 경우 false를 반환한다")
    public void notExistsEmail() throws Exception {
        //given
        String email = "yukeon97@naver.com";

        // when
        boolean actual = memberRepository.existsMemberByEmail(email);

        // then
        Assertions.assertThat(actual).isFalse();
    }

}
