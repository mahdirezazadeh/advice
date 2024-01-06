package org.flickit.advice;

import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;
import org.flickit.advice.domain.Plan;
import org.flickit.advice.domain.Question;
import org.flickit.advice.domain.Target;
import org.flickit.advice.solver.PlanConstraintProvider;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class PlanApp {

    public static void main(String[] args) {
        SolverFactory<Plan> solverFactory = SolverFactory
                .create(new SolverConfig()
                        .withSolutionClass(Plan.class)
                        .withEntityClasses(Question.class)
                        .withConstraintProviderClass(PlanConstraintProvider.class)
                        // The solver runs only for 5 seconds on this small dataset.
                        // It's recommended to run for at least 5 minutes ("5m") otherwise.
                        .withTerminationSpentLimit(Duration.ofSeconds(10)));
//                .withTerminationSpentLimit(Duration.ofMinutes(5)));

        // Load the problem
        Plan problem = generateDemoData();

        // Solve the problem
        Solver<Plan> solver = solverFactory.buildSolver();
        Plan solution = solver.solve(problem);

        // Visualize the solution
        printPlan(solution);
    }

    private static void printPlan(Plan solution) {
        System.out.println("score is: " + solution.getScore());

        List<Question> questions = solution.getQuestions().stream().filter(Question::getPower).toList();
        questions.forEach(System.out::println

        );
    }

    public static Plan generateDemoData() {
        Target target = new Target(32);

        long id = 0L;
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(id++, target, 11, 17));
        questions.add(new Question(id++, target, 4, 16));
        questions.add(new Question(id++, target, 8, 10));
        questions.add(new Question(id++, target, 12, 15));
        questions.add(new Question(id++, target, 13, 17));
        questions.add(new Question(id++, target, 14, 18));
        questions.add(new Question(id++, target, 10, 11));
        questions.add(new Question(id++, target, 12, 25));
        questions.add(new Question(id++, target, 13, 17));
        questions.add(new Question(id++, target, 14, 25));

        return new Plan(target, questions);
    }

}
