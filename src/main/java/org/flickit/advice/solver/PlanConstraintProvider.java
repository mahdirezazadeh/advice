package org.flickit.advice.solver;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ai.timefold.solver.core.api.score.stream.Joiners;
import org.flickit.advice.domain.Question;
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
                .forEach(Question.class)
                .groupBy(count())
                .penalize(HardSoftScore.ONE_HARD, c -> 1)
                .asConstraint("initScore");
    }

    Constraint minCount(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Question.class)
                .filter(Question::getPower)
                .groupBy(count())
                .filter(count -> count > 0)
                .reward(HardSoftScore.ONE_HARD, c -> 1)
                .asConstraint("minCount");
    }


    Constraint minGain(ConstraintFactory constraintFactory) {

        return constraintFactory
                .forEach(Question.class)
                .join(Target.class,
                        Joiners.equal(Question::getTarget, Function.identity()),
                        Joiners.filtering((question, target) -> question.getPower()))
                .groupBy((question, target) -> target, sum((q, tgt) -> q.getGain()))
                .filter((target, totalGain) -> totalGain < target.getMinGain())
                .penalize(HardSoftScore.ONE_SOFT,
                        (target, sum) -> target.getMinGain() - sum)
                .asConstraint("minGain");
    }

    Constraint totalBenefit(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Question.class)
                .filter(Question::getPower)
                .groupBy(Question::getTarget, sum(Question::getGain), sum(Question::getCost))
                .reward(HardSoftScore.ONE_SOFT,
                        (target, totalGain, totalCost) -> Math.round(((float) totalGain / totalCost) * 20))
                .asConstraint("totalBenefit");
    }
}
