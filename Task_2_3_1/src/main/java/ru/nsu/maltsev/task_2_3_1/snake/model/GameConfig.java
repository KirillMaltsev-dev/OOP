package ru.nsu.maltsev.task_2_3_1.snake.model;

public final class GameConfig {
    private final int rows;
    private final int columns;
    private final int foodCount;
    private final int winLength;
    private final int obstacleCount;
    private final int initialTickMillis;
    private final int minTickMillis;
    private final int speedStepMillis;
    private final int levelLengthStep;

    public GameConfig(
            int rows,
            int columns,
            int foodCount,
            int winLength,
            int obstacleCount,
            int initialTickMillis,
            int minTickMillis,
            int speedStepMillis,
            int levelLengthStep
    ) {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Board size must be positive");
        }

        if (foodCount < 0 || winLength <= 0 || obstacleCount < 0) {
            throw new IllegalArgumentException("Game parameters must be non-negative");
        }

        if (initialTickMillis <= 0 || minTickMillis <= 0 || speedStepMillis < 0 || levelLengthStep <= 0) {
            throw new IllegalArgumentException("Speed parameters are invalid");
        }

        if (winLength > rows * columns) {
            throw new IllegalArgumentException("Win length cannot be greater than board size");
        }

        this.rows = rows;
        this.columns = columns;
        this.foodCount = foodCount;
        this.winLength = winLength;
        this.obstacleCount = obstacleCount;
        this.initialTickMillis = initialTickMillis;
        this.minTickMillis = minTickMillis;
        this.speedStepMillis = speedStepMillis;
        this.levelLengthStep = levelLengthStep;
    }

    public static GameConfig defaultConfig() {
        return new GameConfig(
                25,
                25,
                3,
                30,
                8,
                160,
                70,
                15,
                5
        );
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public int getWinLength() {
        return winLength;
    }

    public int getObstacleCount() {
        return obstacleCount;
    }

    public int getInitialTickMillis() {
        return initialTickMillis;
    }

    public int getMinTickMillis() {
        return minTickMillis;
    }

    public int getSpeedStepMillis() {
        return speedStepMillis;
    }

    public int getLevelLengthStep() {
        return levelLengthStep;
    }
}