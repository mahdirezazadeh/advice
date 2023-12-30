package org.flickit.advice.solver;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import org.flickit.advice.domain.TakenStep;

import static ai.timefold.solver.core.api.score.stream.ConstraintCollectors.sum;

public class PlanConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                // Hard constraints
                costsLimit(constraintFactory),
                gainLeast(constraintFactory),
//                // Soft constraints
                totalGain(constraintFactory),
                totalCost(constraintFactory)
        };
    }

    Constraint costsLimit(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(TakenStep.class)
                .filter(TakenStep::getIsOnPlan)
                .groupBy(TakenStep::getTarget, sum(takenStep -> takenStep.getStep().getRequiredCost()))
                .filter((target, reqCost) -> reqCost > target.getRequiredCost())
                .penalize(HardSoftScore.ONE_HARD,
                        (target, reqCost) -> reqCost - target.getRequiredCost())
                .asConstraint("costLimit");
    }

    Constraint gainLeast(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(TakenStep.class)
                .filter(TakenStep::getIsOnPlan)
                .groupBy(TakenStep::getTarget, sum(takenStep -> takenStep.getStep().getGain()))
                .filter((target, reqCost) -> reqCost < target.getTargetGain())
                .penalize(HardSoftScore.ONE_HARD,
                        (t, sum) -> t.getTargetGain() - sum)
                .asConstraint("gainLeast");
    }

    Constraint totalGain(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(TakenStep.class)
                .filter(TakenStep::getIsOnPlan)
                .groupBy(TakenStep::getTarget, sum(takenStep -> takenStep.getStep().getGain()))
                .filter((t, sum) -> sum >= t.getTargetGain())
                .reward(HardSoftScore.ONE_SOFT,
                        (t, sum) -> sum - t.getTargetGain())
                .asConstraint("totalGain");
    }

    Constraint totalCost(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(TakenStep.class)
                .filter(TakenStep::getIsOnPlan)
                .groupBy(TakenStep::getTarget, sum(takenStep -> takenStep.getStep().getRequiredCost()))
                .filter((t, sum) -> sum < t.getRequiredCost())
                .reward(HardSoftScore.ONE_SOFT,
                        (t, sum) -> t.getRequiredCost() - sum)
                .asConstraint("totalCost");
    }

}
