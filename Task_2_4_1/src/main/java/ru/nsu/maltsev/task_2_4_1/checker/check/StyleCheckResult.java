package ru.nsu.maltsev.task_2_4_1.checker.check;

import java.util.ArrayList;
import java.util.List;

public class StyleCheckResult {
    private final List<String> violations = new ArrayList<>();

    public boolean isPassed() {
        return violations.isEmpty();
    }

    public List<String> getViolations() {
        return violations;
    }

    public void addViolation(String violation) {
        violations.add(violation);
    }
}