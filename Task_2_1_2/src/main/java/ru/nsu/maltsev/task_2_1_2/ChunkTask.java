package ru.nsu.maltsev.task_2_1_2;

public class ChunkTask {
    private final String taskId;
    private final int[] numbers;

    public ChunkTask(String taskId, int[] numbers) {
        this.taskId = taskId;
        this.numbers = numbers;
    }

    public String getTaskId() {
        return taskId;
    }

    public int[] getNumbers() {
        return numbers;
    }
}