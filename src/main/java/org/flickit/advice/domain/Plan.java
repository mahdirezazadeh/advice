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
    private List<Boolean> isOnPlans = List.of(Boolean.FALSE, Boolean.TRUE);

    @PlanningEntityCollectionProperty
    private List<TakenStep> takenSteps;

    @PlanningScore
    private HardSoftScore score;

    public Plan(Target target, List<TakenStep> takenSteps) {
        this.target = target;
        this.takenSteps = takenSteps;
    }
}

