package com.problem.solving.problem.domain;

import com.problem.solving.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Problem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private int level;
    @Enumerated(EnumType.STRING)
    private Type type;
    private boolean isSolved = false;

    private boolean isDeleted = true;

    @Builder
    public Problem(Long id,
                   String url,
                   int level,
                   Type type) {
        this.id = id;
        this.url = url;
        this.level = level;
        this.type = type;
    }

    public void softDelete() {
        isDeleted = true;
    }
}
