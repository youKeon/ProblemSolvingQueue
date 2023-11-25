package com.psq.backend.member.persistence;


import com.psq.backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsById(Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.isRecommended = false WHERE m.isRecommended = true")
    Long initializeRecommendation();
}
