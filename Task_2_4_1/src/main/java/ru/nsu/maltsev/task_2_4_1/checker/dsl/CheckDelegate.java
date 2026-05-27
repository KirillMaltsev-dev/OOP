package ru.nsu.maltsev.task_2_4_1.checker.dsl;

import ru.nsu.maltsev.task_2_4_1.checker.model.CheckRequest;

public class CheckDelegate {
    private final CheckRequest checkRequest = new CheckRequest();

    public void group(String groupName) {
        checkRequest.addGroupName(groupName);
        saveIfNeeded();
    }

    public void student(String studentGithub) {
        checkRequest.addStudentGithub(studentGithub);
        saveIfNeeded();
    }

    public void task(String taskId) {
        checkRequest.addTaskId(taskId);
        saveIfNeeded();
    }

    private void saveIfNeeded() {
        if (!DslEnvironment.current().getConfig().getCheckRequests().contains(checkRequest)) {
            DslEnvironment.current().getConfig().getCheckRequests().add(checkRequest);
        }
    }
}