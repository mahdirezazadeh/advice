package org.flickit.advice.domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@PlanningEntity
@Getter
@Setter
@NoArgsConstructor
public class TakenStep {

    @PlanningId
    private Long id;
    private Step step;
    @PlanningVariable
    private Boolean isOnPlan;

    private Target target;

    public TakenStep(Long id, Target target, Step step) {
        this.id = id;
        this.target = target;
        this.step = step;
    }

    public int getStepCost() {
        return step.getRequiredCost();
    }

    @Override
    public String toString() {
        return "{" +
                "isOnPlan=" + isOnPlan +
                ", step=" + step +
                '}';
    }
}
