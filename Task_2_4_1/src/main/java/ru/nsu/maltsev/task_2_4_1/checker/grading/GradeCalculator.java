package ru.nsu.maltsev.task_2_4_1.checker.grading;

import java.util.Comparator;
import ru.nsu.maltsev.task_2_4_1.checker.model.CheckerSettings;
import ru.nsu.maltsev.task_2_4_1.checker.model.GradeRule;

public class GradeCalculator {
    private final CheckerSettings settings;

    public GradeCalculator(CheckerSettings settings) {
        this.settings = settings;
    }

    public String calculate(double points) {
        return settings.getGradeRules().stream()
                .sorted(Comparator.comparingDouble(GradeRule::getMinPoints).reversed())
                .filter(rule -> points >= rule.getMinPoints())
                .map(GradeRule::getName)
                .findFirst()
                .orElse("не аттестован");
    }
}