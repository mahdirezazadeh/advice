package org.flickit.advice.solver;

import ai.timefold.solver.test.api.score.stream.ConstraintVerifier;
import org.flickit.advice.domain.Plan;
import org.flickit.advice.domain.Step;
import org.flickit.advice.domain.TakenStep;
import org.flickit.advice.domain.Target;
import org.junit.jupiter.api.Test;


class PlanConstraintProviderTest {

    ConstraintVerifier<PlanConstraintProvider, Plan> constraintVerifier = ConstraintVerifier.build(
            new PlanConstraintProvider(), Plan.class, TakenStep.class);

    @Test
    void costsLimit() {
        Target target = new Target(10, 10);

        TakenStep takenStep = new TakenStep(0L, target, new Step(12, 12));
        takenStep.setIsOnPlan(true);

        constraintVerifier.verifyThat(PlanConstraintProvider::costsLimit)
                .given(takenStep)
                .penalizesBy(2);
    }

    @Test
    void gainLeast() {
        Target target = new Target(10, 10);

        TakenStep takenStep = new TakenStep(0L, target, new Step(8, 10));
        takenStep.setIsOnPlan(true);

        constraintVerifier.verifyThat(PlanConstraintProvider::gainLeast)
                .given(takenStep)
                .penalizesBy(2);
    }

    @Test
    void totalCost() {
        Target target = new Target(10, 10);

        TakenStep takenStep = new TakenStep(0L, target, new Step(8, 8));
        takenStep.setIsOnPlan(true);

        constraintVerifier.verifyThat(PlanConstraintProvider::totalCost)
                .given(takenStep)
                .rewardsWith(2);
    }

    @Test
    void totalGain() {
        Target target = new Target(10, 10);

        TakenStep takenStep = new TakenStep(0L, target, new Step(12, 10));
        takenStep.setIsOnPlan(true);

        constraintVerifier.verifyThat(PlanConstraintProvider::totalGain)
                .given(takenStep)
                .rewardsWith(2);
    }
}