package org.flickit.advice.domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@PlanningEntity
@Getter
@Setter
@NoArgsConstructor
public class Question {

    @PlanningId
    private Long id;
    private int gain;
    private int cost;

    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Boolean> options = List.of(Boolean.FALSE, Boolean.TRUE);

    @PlanningVariable
    private Boolean power;

    private Target target;

    public Question(Long id, Target target, int gain, int cost) {
        this.id = id;
        this.target = target;
        this.gain = gain;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "TakenStep{" +
                "gain=" + gain +
                ", cost=" + cost +
                ", isOnPlan=" + power +
                '}';
    }
}
