package ru.nsu.maltsev.task_2_4_1.checker.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CheckerSettings {
    private Path workDir = Paths.get("build", "checker-work");
    private int commandTimeoutSeconds = 120;
    private final List<GradeRule> gradeRules = new ArrayList<>();

    public Path getWorkDir() {
        return workDir;
    }

    public int getCommandTimeoutSeconds() {
        return commandTimeoutSeconds;
    }

    public List<GradeRule> getGradeRules() {
        return gradeRules;
    }

    public void setWorkDir(Path workDir) {
        this.workDir = workDir;
    }

    public void setCommandTimeoutSeconds(int commandTimeoutSeconds) {
        this.commandTimeoutSeconds = commandTimeoutSeconds;
    }

    public void addGradeRule(GradeRule gradeRule) {
        gradeRules.add(gradeRule);
    }
}
