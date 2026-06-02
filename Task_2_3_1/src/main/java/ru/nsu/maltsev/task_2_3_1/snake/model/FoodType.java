package ru.nsu.maltsev.task_2_3_1.snake.model;

public enum FoodType {
    REGULAR(1, 1);

    private final int growth;
    private final int score;

    FoodType(int growth, int score) {
        this.growth = growth;
        this.score = score;
    }

    public int getGrowth() {
        return growth;
    }

    public int getScore() {
        return score;
    }
}