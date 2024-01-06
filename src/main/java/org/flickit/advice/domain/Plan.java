package org.flickit.advice.domain;

import ai.timefold.solver.core.api.domain.solution.*;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import lombok.*;

import java.util.List;


@PlanningSolution
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Plan {

    @ProblemFactProperty
    @ValueRangeProvider
    private Target target;

    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Boolean> isOnPlans = List.of(Boolean.TRUE, Boolean.FALSE);

    @PlanningEntityCollectionProperty
    private List<Step> steps;

    @PlanningScore
    private HardSoftScore score;

    public Plan(Target target, List<Step> steps) {
        this.target = target;
        this.steps = steps;
    }
}

