package ru.nsu.maltsev.task_2_4_1.checker.grading;

import java.math.BigDecimal;
import java.math.RoundingMode;
import ru.nsu.maltsev.task_2_4_1.checker.check.TaskCheckResult;
import ru.nsu.maltsev.task_2_4_1.checker.check.TestSummary;
import ru.nsu.maltsev.task_2_4_1.checker.model.BonusConfig;
import ru.nsu.maltsev.task_2_4_1.checker.model.CourseConfig;
import ru.nsu.maltsev.task_2_4_1.checker.model.SubmissionConfig;

public class ScoreCalculator {
    private final CourseConfig config;

    public ScoreCalculator(CourseConfig config) {
        this.config = config;
    }

    public double calculate(TaskCheckResult result) {
        double maxPoints = result.getTask().getMaxPoints();
        double score = 0.0;

        if (result.isCompilationPassed()) {
            score += maxPoints * 0.30;
        }

        if (result.isJavadocPassed()) {
            score += maxPoints * 0.15;
        }

        if (result.isStylePassed()) {
            score += maxPoints * 0.15;
        }

        if (result.isTestsPassed()) {
            score += maxPoints * testPart(result.getTestSummary());
        }

        score = applyDeadlinePenalty(score, result);
        score = applyBonuses(score, result);

        return round(score);
    }

    private double testPart(TestSummary summary) {
        if (summary.getTotal() == 0) {
            return 0.40;
        }

        return ((double) summary.getPassed() / summary.getTotal()) * 0.40;
    }

    private double applyDeadlinePenalty(double score, TaskCheckResult result) {
        SubmissionConfig submission = config.findSubmission(
                result.getStudent().getGithub(),
                result.getTask().getId()
        );

        if (submission == null) {
            return score;
        }

        if (!submission.isSoftPassed() && !submission.isHardPassed()) {
            return 0.0;
        }

        if (!submission.isSoftPassed() || !submission.isHardPassed()) {
            return Math.max(0.0, score - 0.5);
        }

        return score;
    }

    private double applyBonuses(double score, TaskCheckResult result) {
        double resultScore = score;

        for (BonusConfig bonus : config.findBonuses(
                result.getStudent().getGithub(),
                result.getTask().getId()
        )) {
            resultScore += bonus.getPoints();
        }

        return resultScore;
    }

    private double round(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}