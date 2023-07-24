package com.problem.solving.problem.persistence;

import com.problem.solving.problem.domain.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long>, ProblemCustomRepository {

    Optional<Problem> findFirstByOrderByCreatedAtAsc();

    @Query(value = "SELECT * " +
                    "FROM Problem " +
                    "WHERE is_deleted = true " +
                    "AND " +
                    "DATE(updated_at) < DATE_SUB(NOW(), INTERVAL 3 DAY)"
            , nativeQuery = true)
    Page<Problem> findDeletedProblems(Pageable pageable);
}
