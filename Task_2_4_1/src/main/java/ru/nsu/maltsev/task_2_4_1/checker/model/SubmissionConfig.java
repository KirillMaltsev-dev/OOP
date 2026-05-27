package ru.nsu.maltsev.task_2_4_1.checker.model;

public class SubmissionConfig {
    private String studentGithub;
    private String taskId;
    private boolean softPassed;
    private boolean hardPassed;

    public String getStudentGithub() {
        return studentGithub;
    }

    public String getTaskId() {
        return taskId;
    }

    public boolean isSoftPassed() {
        return softPassed;
    }

    public boolean isHardPassed() {
        return hardPassed;
    }

    public void setStudentGithub(String studentGithub) {
        this.studentGithub = studentGithub;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setSoftPassed(boolean softPassed) {
        this.softPassed = softPassed;
    }

    public void setHardPassed(boolean hardPassed) {
        this.hardPassed = hardPassed;
    }
}