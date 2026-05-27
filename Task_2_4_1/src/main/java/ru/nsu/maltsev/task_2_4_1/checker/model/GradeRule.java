package ru.nsu.maltsev.task_2_4_1.checker.model;

public class GradeRule {
    private final String name;
    private final double minPoints;

    public GradeRule(String name, double minPoints) {
        this.name = name;
        this.minPoints = minPoints;
    }

    public String getName() {
        return name;
    }

    public double getMinPoints() {
        return minPoints;
    }
}