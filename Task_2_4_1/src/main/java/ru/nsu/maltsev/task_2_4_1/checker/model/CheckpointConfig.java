package ru.nsu.maltsev.task_2_4_1.checker.model;

import java.time.LocalDate;

public class CheckpointConfig {
    private final String name;
    private LocalDate date;

    public CheckpointConfig(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
