package ru.nsu.maltsev.task_2_4_1.checker.model;

import java.time.LocalDate;

public class TaskConfig {
    private final String id;
    private String title;
    private double maxPoints;
    private LocalDate softDeadline;
    private LocalDate hardDeadline;

    public TaskConfig(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getMaxPoints() {
        return maxPoints;
    }

    public LocalDate getSoftDeadline() {
        return softDeadline;
    }

    public LocalDate getHardDeadline() {
        return hardDeadline;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
    }

    public void setSoftDeadline(LocalDate softDeadline) {
        this.softDeadline = softDeadline;
    }

    public void setHardDeadline(LocalDate hardDeadline) {
        this.hardDeadline = hardDeadline;
    }
}