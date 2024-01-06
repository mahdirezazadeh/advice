package org.flickit.advice.domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@PlanningEntity
@Getter
@Setter
@NoArgsConstructor
public class Step {

    @PlanningId
    private Long id;
    private int gain;
    private int cost;
    @PlanningVariable
    private Boolean isOnPlan;

    private Target target;

    public Step(Long id, Target target, int gain, int cost) {
        this.id = id;
        this.target = target;
        this.gain = gain;
        this.cost = cost;
    }

//    public float benefit() {
//        return (float) gain / cost;
//    }

    @Override
    public String toString() {
        return "TakenStep{" +
                "gain=" + gain +
                ", cost=" + cost +
                ", isOnPlan=" + isOnPlan +
                '}';
    }

    public float benefit() {
        return (float) gain / cost;
    }
}
