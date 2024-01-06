package org.flickit.advice.solver;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ai.timefold.solver.core.api.score.stream.Joiners;
import org.flickit.advice.domain.Step;
import org.flickit.advice.domain.Target;

import java.util.function.Function;

import static ai.timefold.solver.core.api.score.stream.ConstraintCollectors.count;
import static ai.timefold.solver.core.api.score.stream.ConstraintCollectors.sum;

public class PlanConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                // Hard constraints
                initScore(constraintFactory),
                minCount(constraintFactory),
                minGain(constraintFactory),
//                // Soft constraints
                totalBenefit(constraintFactory)
        };
    }

    Constraint initScore(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Step.class)
                .groupBy(count())
                .penalize(HardSoftScore.ONE_HARD, c -> 1)
                .asConstraint("initScore");
    }

    Constraint minCount(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Step.class)
                .filter(Step::getIsOnPlan)
                .groupBy(count())
                .filter(count -> count > 0)
                .reward(HardSoftScore.ONE_HARD, c -> 1)
                .asConstraint("minCount");
    }


    Constraint minGain(ConstraintFactory constraintFactory) {

        return constraintFactory
                .forEach(Step.class)
                .join(Target.class,
                        Joiners.equal(Step::getTarget, Function.identity()),
                        Joiners.filtering((step, target) -> step.getIsOnPlan()))
                .groupBy((step, target) -> target, sum((stp, tgt) -> stp.getGain()))
                .filter((target, totalGain) -> totalGain < target.getMinGain())
                .penalize(HardSoftScore.ONE_SOFT,
                        (target, sum) -> target.getMinGain() - sum)
                .asConstraint("minGain");
    }

    Constraint totalBenefit(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Step.class)
                .filter(Step::getIsOnPlan)
                .groupBy(Step::getTarget, sum(Step::getGain), sum(Step::getCost))
                .reward(HardSoftScore.ONE_SOFT,
                        (target, totalGain, totalCost) -> Math.round(((float) totalGain / totalCost) * 20))
                .asConstraint("totalBenefit");
    }
}
