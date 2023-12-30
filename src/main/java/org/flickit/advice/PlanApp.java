package org.flickit.advice;

import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;
import org.flickit.advice.domain.Plan;
import org.flickit.advice.domain.Step;
import org.flickit.advice.domain.TakenStep;
import org.flickit.advice.domain.Target;
import org.flickit.advice.solver.PlanConstraintProvider;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class PlanApp {

    public static void main(String[] args) {
        SolverFactory<Plan> solverFactory = SolverFactory.create(new SolverConfig().withSolutionClass(Plan.class).withEntityClasses(TakenStep.class).withConstraintProviderClass(PlanConstraintProvider.class)
                // The solver runs only for 5 seconds on this small dataset.
                // It's recommended to run for at least 5 minutes ("5m") otherwise.
                .withTerminationSpentLimit(Duration.ofSeconds(5)));
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

        List<TakenStep> takenSteps = solution.getTakenSteps().stream().filter(TakenStep::getIsOnPlan).toList();
        takenSteps.forEach(takenStep -> System.out.println(takenStep.getStep())

        );
    }

    public static Plan generateDemoData() {
        Target target = new Target(10, 15);

        long id = 0L;
        List<TakenStep> takenSteps = new ArrayList<>();
        takenSteps.add(new TakenStep(id++, target, new Step(12, 4)));
        takenSteps.add(new TakenStep(id++, target, new Step(12, 6)));
        takenSteps.add(new TakenStep(id++, target, new Step(13, 6)));
        takenSteps.add(new TakenStep(id++, target, new Step(14, 5)));
        takenSteps.add(new TakenStep(id++, target, new Step(10, 5)));


        return new Plan(target, takenSteps);
    }

}
