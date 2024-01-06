package org.flickit.advice.solver;

import ai.timefold.solver.test.api.score.stream.ConstraintVerifier;
import org.flickit.advice.domain.Plan;
import org.flickit.advice.domain.Step;
import org.flickit.advice.domain.Target;
import org.junit.jupiter.api.Test;

class PlanConstraintProviderTest {

    ConstraintVerifier<PlanConstraintProvider, Plan> constraintVerifier = ConstraintVerifier.build(
            new PlanConstraintProvider(), Plan.class, Step.class);

    @Test
    void initScore() {
        Target target = new Target(10);

        Step step1 = new Step(0L, target, 8, 10);
        step1.setIsOnPlan(true);

        constraintVerifier.verifyThat(PlanConstraintProvider::initScore)
                .given(step1)
                .penalizesBy(1);

        Step step2 = new Step(1L, target, 8, 10);
        step2.setIsOnPlan(false);

        constraintVerifier.verifyThat(PlanConstraintProvider::initScore)
                .given(step2)
                .penalizesBy(1);
    }

    @Test
    void minCount() {
        Target target = new Target(10);

        Step step1 = new Step(0L, target, 8, 10);
        Step step2 = new Step(1L, target, 8, 10);
        step1.setIsOnPlan(true);
        step2.setIsOnPlan(true);

        constraintVerifier.verifyThat(PlanConstraintProvider::minCount)
                .given(step1, step2)
                .rewardsWith(1);
    }

    @Test
    void gainLeast() {
        Target target = new Target(10);

        Step step = new Step(0L, target, 8, 10);
        step.setIsOnPlan(true);

        constraintVerifier.verifyThat(PlanConstraintProvider::minGain)
                .given(step, target)
                .penalizesBy(2);
    }

    @Test
    void totalBenefit() {
        Target target = new Target(10);

        Step step = new Step(0L, target, 8, 10);
        step.setIsOnPlan(true);

        constraintVerifier.verifyThat(PlanConstraintProvider::totalBenefit)
                .given(step)
                .rewardsWith(16);
    }
}