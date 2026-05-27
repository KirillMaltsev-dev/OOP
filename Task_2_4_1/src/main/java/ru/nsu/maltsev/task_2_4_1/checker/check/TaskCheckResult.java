package ru.nsu.maltsev.task_2_4_1.checker.check;

import ru.nsu.maltsev.task_2_4_1.checker.model.StudentConfig;
import ru.nsu.maltsev.task_2_4_1.checker.model.TaskConfig;

public class TaskCheckResult {
    private final StudentConfig student;
    private final TaskConfig task;
    private boolean repositoryPrepared;
    private boolean taskDirectoryExists;
    private boolean compilationPassed;
    private boolean javadocPassed;
    private boolean stylePassed;
    private boolean testsPassed;
    private double points;
    private String errorMessage = "";
    private TestSummary testSummary = new TestSummary();

    public TaskCheckResult(StudentConfig student, TaskConfig task) {
        this.student = student;
        this.task = task;
    }

    public StudentConfig getStudent() {
        return student;
    }

    public TaskConfig getTask() {
        return task;
    }

    public boolean isRepositoryPrepared() {
        return repositoryPrepared;
    }

    public boolean isTaskDirectoryExists() {
        return taskDirectoryExists;
    }

    public boolean isCompilationPassed() {
        return compilationPassed;
    }

    public boolean isJavadocPassed() {
        return javadocPassed;
    }

    public boolean isStylePassed() {
        return stylePassed;
    }

    public boolean isTestsPassed() {
        return testsPassed;
    }

    public double getPoints() {
        return points;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public TestSummary getTestSummary() {
        return testSummary;
    }

    public void setRepositoryPrepared(boolean repositoryPrepared) {
        this.repositoryPrepared = repositoryPrepared;
    }

    public void setTaskDirectoryExists(boolean taskDirectoryExists) {
        this.taskDirectoryExists = taskDirectoryExists;
    }

    public void setCompilationPassed(boolean compilationPassed) {
        this.compilationPassed = compilationPassed;
    }

    public void setJavadocPassed(boolean javadocPassed) {
        this.javadocPassed = javadocPassed;
    }

    public void setStylePassed(boolean stylePassed) {
        this.stylePassed = stylePassed;
    }

    public void setTestsPassed(boolean testsPassed) {
        this.testsPassed = testsPassed;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setTestSummary(TestSummary testSummary) {
        this.testSummary = testSummary;
    }
}