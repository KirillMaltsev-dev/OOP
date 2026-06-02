package ru.nsu.maltsev.task_2_3_1.snake.model;

import java.util.Objects;

public final class Position {
    private final int row;
    private final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Position move(Direction direction) {
        return new Position(
                row + direction.getRowDelta(),
                column + direction.getColumnDelta()
        );
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Position)) {
            return false;
        }

        Position position = (Position) object;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}