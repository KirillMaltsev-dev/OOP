package ru.nsu.maltsev.task_2_4_1.checker.model;

import java.util.ArrayList;
import java.util.List;

public class CheckRequest {
    private final List<String> groupNames = new ArrayList<>();
    private final List<String> studentGithubs = new ArrayList<>();
    private final List<String> taskIds = new ArrayList<>();

    public List<String> getGroupNames() {
        return groupNames;
    }

    public List<String> getStudentGithubs() {
        return studentGithubs;
    }

    public List<String> getTaskIds() {
        return taskIds;
    }

    public void addGroupName(String groupName) {
        groupNames.add(groupName);
    }

    public void addStudentGithub(String studentGithub) {
        studentGithubs.add(studentGithub);
    }

    public void addTaskId(String taskId) {
        taskIds.add(taskId);
    }
}