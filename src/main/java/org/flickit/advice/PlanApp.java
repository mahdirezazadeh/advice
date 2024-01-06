package org.flickit.advice;

import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;
import org.flickit.advice.domain.Plan;
import org.flickit.advice.domain.Step;
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
                        .withEntityClasses(Step.class)
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

        List<Step> steps = solution.getSteps().stream().filter(Step::getIsOnPlan).toList();
        steps.forEach(System.out::println

        );
    }

    public static Plan generateDemoData() {
        Target target = new Target(32);

        long id = 0L;
        List<Step> steps = new ArrayList<>();
        steps.add(new Step(id++, target, 11, 17));
        steps.add(new Step(id++, target, 4, 16));
//        steps.add(new Step(id++, target, 6, 4));
//        steps.add(new Step(id++, target, 4, 4));
        steps.add(new Step(id++, target, 8, 10));
        steps.add(new Step(id++, target, 12, 15));
        steps.add(new Step(id++, target, 13, 17));
        steps.add(new Step(id++, target, 14, 18));
        steps.add(new Step(id++, target, 10, 11));
        steps.add(new Step(id++, target, 12, 25));
        steps.add(new Step(id++, target, 13, 17));
        steps.add(new Step(id++, target, 14, 25));

//        Comparator<Step> comparator = Comparator.comparing(Step::benefit, (b1, b2) -> -1 * b1.compareTo(b2));
//        steps.sort(comparator);

        return new Plan(target, steps);
    }

}
