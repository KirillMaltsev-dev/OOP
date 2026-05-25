package ru.nsu.maltsev.task_2_4_1.checker.model;

public class BonusConfig {
    private String studentGithub;
    private String taskId;
    private double points;
    private String reason;

    public String getStudentGithub() {
        return studentGithub;
    }

    public String getTaskId() {
        return taskId;
    }

    public double getPoints() {
        return points;
    }

    public String getReason() {
        return reason;
    }

    public void setStudentGithub(String studentGithub) {
        this.studentGithub = studentGithub;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
