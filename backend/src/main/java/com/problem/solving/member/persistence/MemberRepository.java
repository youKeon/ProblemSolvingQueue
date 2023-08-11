package com.problem.solving.member.persistence;


import com.problem.solving.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);

    boolean existsMemberByEmail(String email);

    boolean existsById(Long id);
}
