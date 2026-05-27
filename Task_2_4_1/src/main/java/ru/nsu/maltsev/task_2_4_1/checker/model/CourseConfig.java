package ru.nsu.maltsev.task_2_4_1.checker.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CourseConfig {
    private final Map<String, TaskConfig> tasks = new LinkedHashMap<>();
    private final Map<String, GroupConfig> groups = new LinkedHashMap<>();
    private final List<CheckRequest> checkRequests = new ArrayList<>();
    private final List<CheckpointConfig> checkpoints = new ArrayList<>();
    private final List<SubmissionConfig> submissions = new ArrayList<>();
    private final List<BonusConfig> bonuses = new ArrayList<>();
    private final CheckerSettings settings = new CheckerSettings();

    public Map<String, TaskConfig> getTasks() {
        return tasks;
    }

    public Map<String, GroupConfig> getGroups() {
        return groups;
    }

    public List<CheckRequest> getCheckRequests() {
        return checkRequests;
    }

    public List<CheckpointConfig> getCheckpoints() {
        return checkpoints;
    }

    public List<SubmissionConfig> getSubmissions() {
        return submissions;
    }

    public List<BonusConfig> getBonuses() {
        return bonuses;
    }

    public CheckerSettings getSettings() {
        return settings;
    }

    public void addTask(TaskConfig task) {
        tasks.put(task.getId(), task);
    }

    public void addGroup(GroupConfig group) {
        groups.put(group.getName(), group);
    }

    public void addCheckpoint(CheckpointConfig checkpoint) {
        checkpoints.add(checkpoint);
    }

    public void addSubmission(SubmissionConfig submission) {
        submissions.add(submission);
    }

    public void addBonus(BonusConfig bonus) {
        bonuses.add(bonus);
    }

    public SubmissionConfig findSubmission(String studentGithub, String taskId) {
        for (SubmissionConfig submission : submissions) {
            if (submission.getStudentGithub().equals(studentGithub)
                    && submission.getTaskId().equals(taskId)) {
                return submission;
            }
        }

        return null;
    }

    public List<BonusConfig> findBonuses(String studentGithub, String taskId) {
        List<BonusConfig> result = new ArrayList<>();

        for (BonusConfig bonus : bonuses) {
            if (bonus.getStudentGithub().equals(studentGithub)
                    && bonus.getTaskId().equals(taskId)) {
                result.add(bonus);
            }
        }

        return result;
    }
}