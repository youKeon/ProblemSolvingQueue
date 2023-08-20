package com.problem.solving.member.persistence;


import com.problem.solving.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsMemberByEmail(String email);

    boolean existsById(Long id);
}
