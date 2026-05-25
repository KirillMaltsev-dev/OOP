package ru.nsu.maltsev.task_2_4_1.checker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.maltsev.task_2_4_1.checker.check.TaskCheckResult;
import ru.nsu.maltsev.task_2_4_1.checker.check.TestSummary;
import ru.nsu.maltsev.task_2_4_1.checker.grading.ScoreCalculator;
import ru.nsu.maltsev.task_2_4_1.checker.model.CourseConfig;
import ru.nsu.maltsev.task_2_4_1.checker.model.StudentConfig;
import ru.nsu.maltsev.task_2_4_1.checker.model.TaskConfig;

class ScoreCalculatorTest {
    @Test
    void calculatesFullScoreForSuccessfulTask() {
        CourseConfig config = new CourseConfig();
        TaskConfig task = new TaskConfig("Task_2_1_1");
        task.setMaxPoints(2.0);

        StudentConfig student = new StudentConfig();
        student.setGithub("student");

        TaskCheckResult result = new TaskCheckResult(student, task);
        result.setCompilationPassed(true);
        result.setJavadocPassed(true);
        result.setStylePassed(true);
        result.setTestsPassed(true);

        TestSummary summary = new TestSummary();
        summary.add(10, 0, 0);
        result.setTestSummary(summary);

        double score = new ScoreCalculator(config).calculate(result);

        assertEquals(2.0, score);
    }

    @Test
    void calculatesPartialScoreWhenTestsFail() {
        CourseConfig config = new CourseConfig();
        TaskConfig task = new TaskConfig("Task_2_1_1");
        task.setMaxPoints(2.0);

        StudentConfig student = new StudentConfig();
        student.setGithub("student");

        TaskCheckResult result = new TaskCheckResult(student, task);
        result.setCompilationPassed(true);
        result.setJavadocPassed(true);
        result.setStylePassed(true);
        result.setTestsPassed(false);

        TestSummary summary = new TestSummary();
        summary.add(10, 5, 0);
        result.setTestSummary(summary);

        double score = new ScoreCalculator(config).calculate(result);

        assertEquals(1.2, score);
    }
}