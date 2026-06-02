package ru.nsu.maltsev.task_2_3_1.snake.model;

public final class Food {
    private final Position position;
    private final FoodType type;

    public Food(Position position, FoodType type) {
        this.position = position;
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public FoodType getType() {
        return type;
    }
}