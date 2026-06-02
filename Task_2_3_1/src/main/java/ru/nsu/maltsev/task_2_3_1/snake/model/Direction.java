package ru.nsu.maltsev.task_2_3_1.snake.model;

public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final int rowDelta;
    private final int columnDelta;

    Direction(int rowDelta, int columnDelta) {
        this.rowDelta = rowDelta;
        this.columnDelta = columnDelta;
    }

    public int getRowDelta() {
        return rowDelta;
    }

    public int getColumnDelta() {
        return columnDelta;
    }

    public boolean isOpposite(Direction other) {
        return rowDelta + other.rowDelta == 0 && columnDelta + other.columnDelta == 0;
    }
}
