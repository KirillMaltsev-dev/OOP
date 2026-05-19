package ru.nsu.maltsev.task_2_3_1.snake.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ru.nsu.maltsev.task_2_3_1.snake.model.Food;
import ru.nsu.maltsev.task_2_3_1.snake.model.GameModel;
import ru.nsu.maltsev.task_2_3_1.snake.model.Position;

public class GameRenderer {
    private final Canvas boardCanvas;

    public GameRenderer(Canvas boardCanvas) {
        this.boardCanvas = boardCanvas;
    }

    public void render(GameModel model) {
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();

        double width = boardCanvas.getWidth();
        double height = boardCanvas.getHeight();

        int rows = model.getConfig().getRows();
        int columns = model.getConfig().getColumns();

        double cellWidth = width / columns;
        double cellHeight = height / rows;

        gc.clearRect(0, 0, width, height);

        drawGrassBoard(gc, rows, columns, cellWidth, cellHeight);
        drawObstacles(gc, model, cellWidth, cellHeight);
        drawFood(gc, model, cellWidth, cellHeight);
        drawSnake(gc, model, cellWidth, cellHeight);
    }

    public void drawExplosion(GameModel model, Position explosionPosition, int explosionFrame, int maxFrames) {
        if (explosionPosition == null) {
            return;
        }

        GraphicsContext gc = boardCanvas.getGraphicsContext2D();

        int rows = model.getConfig().getRows();
        int columns = model.getConfig().getColumns();

        double cellWidth = boardCanvas.getWidth() / columns;
        double cellHeight = boardCanvas.getHeight() / rows;

        double centerX = explosionPosition.getColumn() * cellWidth + cellWidth / 2.0;
        double centerY = explosionPosition.getRow() * cellHeight + cellHeight / 2.0;

        double progress = (double) explosionFrame / maxFrames;
        double radius = cellWidth * (0.5 + progress * 2.4);

        gc.setFill(Color.rgb(255, 210, 70, 1.0 - progress * 0.7));
        gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        gc.setFill(Color.rgb(255, 90, 40, 1.0 - progress * 0.6));
        gc.fillOval(centerX - radius * 0.65, centerY - radius * 0.65, radius * 1.3, radius * 1.3);

        gc.setStroke(Color.rgb(255, 245, 140, 1.0 - progress * 0.5));
        gc.setLineWidth(4);

        for (int i = 0; i < 12; i++) {
            double angle = Math.PI * 2 * i / 12.0;
            double startRadius = radius * 0.4;
            double endRadius = radius * 1.4;

            double x1 = centerX + Math.cos(angle) * startRadius;
            double y1 = centerY + Math.sin(angle) * startRadius;
            double x2 = centerX + Math.cos(angle) * endRadius;
            double y2 = centerY + Math.sin(angle) * endRadius;

            gc.strokeLine(x1, y1, x2, y2);
        }
    }

    private void drawGrassBoard(GraphicsContext gc, int rows, int columns, double cellWidth, double cellHeight) {
        Color grass1 = Color.rgb(182, 232, 152);
        Color grass2 = Color.rgb(168, 222, 138);
        Color borderColor = Color.rgb(130, 185, 110);

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                boolean even = (row + column) % 2 == 0;
                gc.setFill(even ? grass1 : grass2);
                gc.fillRect(column * cellWidth, row * cellHeight, cellWidth, cellHeight);
            }
        }

        gc.setStroke(borderColor);
        gc.setLineWidth(0.5);

        for (int row = 0; row <= rows; row++) {
            double y = row * cellHeight;
            gc.strokeLine(0, y, boardCanvas.getWidth(), y);
        }

        for (int column = 0; column <= columns; column++) {
            double x = column * cellWidth;
            gc.strokeLine(x, 0, x, boardCanvas.getHeight());
        }
    }

    private void drawObstacles(GraphicsContext gc, GameModel model, double cellWidth, double cellHeight) {
        for (Position obstacle : model.getObstacles()) {
            gc.setFill(Color.rgb(130, 110, 90));
            fillCell(gc, obstacle, cellWidth, cellHeight, 0.10);

            gc.setFill(Color.rgb(160, 140, 120));
            double x = obstacle.getColumn() * cellWidth + cellWidth * 0.22;
            double y = obstacle.getRow() * cellHeight + cellHeight * 0.22;
            gc.fillOval(x, y, cellWidth * 0.18, cellHeight * 0.18);
        }
    }

    private void drawFood(GraphicsContext gc, GameModel model, double cellWidth, double cellHeight) {
        for (Food food : model.getFoods()) {
            drawApple(gc, food.getPosition(), cellWidth, cellHeight);
        }
    }

    private void drawApple(GraphicsContext gc, Position position, double cellWidth, double cellHeight) {
        double baseX = position.getColumn() * cellWidth;
        double baseY = position.getRow() * cellHeight;

        double bodyW = cellWidth * 0.34;
        double bodyH = cellHeight * 0.42;

        double leftX = baseX + cellWidth * 0.22;
        double rightX = baseX + cellWidth * 0.44;
        double bodyY = baseY + cellHeight * 0.30;

        gc.setFill(Color.rgb(220, 60, 70));
        gc.fillOval(leftX, bodyY, bodyW, bodyH);
        gc.fillOval(rightX, bodyY, bodyW, bodyH);
        gc.fillOval(baseX + cellWidth * 0.30, baseY + cellHeight * 0.42, cellWidth * 0.40, cellHeight * 0.28);

        gc.setFill(Color.rgb(255, 170, 170));
        gc.fillOval(baseX + cellWidth * 0.34, baseY + cellHeight * 0.38, cellWidth * 0.12, cellHeight * 0.12);

        gc.setFill(Color.rgb(110, 70, 35));
        gc.fillRoundRect(
                baseX + cellWidth * 0.47,
                baseY + cellHeight * 0.16,
                cellWidth * 0.06,
                cellHeight * 0.16,
                4,
                4
        );

        gc.setFill(Color.rgb(80, 170, 90));
        gc.fillOval(
                baseX + cellWidth * 0.50,
                baseY + cellHeight * 0.16,
                cellWidth * 0.18,
                cellHeight * 0.10
        );
    }

    private void drawSnake(GraphicsContext gc, GameModel model, double cellWidth, double cellHeight) {
        boolean head = true;

        for (Position part : model.getSnake()) {
            if (head) {
                gc.setFill(Color.rgb(255, 105, 180));
                fillCell(gc, part, cellWidth, cellHeight, 0.08);

                double x = part.getColumn() * cellWidth;
                double y = part.getRow() * cellHeight;

                gc.setFill(Color.WHITE);
                gc.fillOval(x + cellWidth * 0.23, y + cellHeight * 0.22, cellWidth * 0.16, cellHeight * 0.16);
                gc.fillOval(x + cellWidth * 0.61, y + cellHeight * 0.22, cellWidth * 0.16, cellHeight * 0.16);

                gc.setFill(Color.BLACK);
                gc.fillOval(x + cellWidth * 0.29, y + cellHeight * 0.28, cellWidth * 0.07, cellHeight * 0.07);
                gc.fillOval(x + cellWidth * 0.67, y + cellHeight * 0.28, cellWidth * 0.07, cellHeight * 0.07);

                head = false;
            } else {
                gc.setFill(Color.rgb(255, 150, 205));
                fillCell(gc, part, cellWidth, cellHeight, 0.10);
            }
        }
    }

    private void fillCell(GraphicsContext gc, Position position, double cellWidth, double cellHeight, double paddingRatio) {
        double paddingX = cellWidth * paddingRatio;
        double paddingY = cellHeight * paddingRatio;

        gc.fillRoundRect(
                position.getColumn() * cellWidth + paddingX,
                position.getRow() * cellHeight + paddingY,
                cellWidth - paddingX * 2,
                cellHeight - paddingY * 2,
                12,
                12
        );
    }
}