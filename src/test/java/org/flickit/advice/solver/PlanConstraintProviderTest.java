package org.flickit.advice.solver;

import ai.timefold.solver.test.api.score.stream.ConstraintVerifier;
import org.flickit.advice.domain.Plan;
import org.flickit.advice.domain.Question;
import org.flickit.advice.domain.Target;
import org.junit.jupiter.api.Test;

class PlanConstraintProviderTest {

    ConstraintVerifier<PlanConstraintProvider, Plan> constraintVerifier = ConstraintVerifier.build(
            new PlanConstraintProvider(), Plan.class, Question.class);

    @Test
    void initScore() {
        Target target = new Target(10);

        Question question1 = new Question(0L, target, 8, 10);
        question1.setPower(true);

        constraintVerifier.verifyThat(PlanConstraintProvider::initScore)
                .given(question1)
                .penalizesBy(1);

        Question question2 = new Question(1L, target, 8, 10);
        question2.setPower(false);

        constraintVerifier.verifyThat(PlanConstraintProvider::initScore)
                .given(question2)
                .penalizesBy(1);
    }

    @Test
    void minCount() {
        Target target = new Target(10);

        Question question1 = new Question(0L, target, 8, 10);
        Question question2 = new Question(1L, target, 8, 10);
        question1.setPower(true);
        question2.setPower(true);

        constraintVerifier.verifyThat(PlanConstraintProvider::minCount)
                .given(question1, question2)
                .rewardsWith(1);
    }

    @Test
    void gainLeast() {
        Target target = new Target(10);

        Question question = new Question(0L, target, 8, 10);
        question.setPower(true);

        constraintVerifier.verifyThat(PlanConstraintProvider::minGain)
                .given(question, target)
                .penalizesBy(2);
    }

    @Test
    void totalBenefit() {
        Target target = new Target(10);

        Question question = new Question(0L, target, 8, 10);
        question.setPower(true);

        constraintVerifier.verifyThat(PlanConstraintProvider::totalBenefit)
                .given(question)
                .rewardsWith(16);
    }
}