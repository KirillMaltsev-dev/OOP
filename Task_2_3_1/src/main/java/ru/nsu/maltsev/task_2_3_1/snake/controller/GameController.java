package ru.nsu.maltsev.task_2_3_1.snake.controller;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import ru.nsu.maltsev.task_2_3_1.snake.model.Direction;
import ru.nsu.maltsev.task_2_3_1.snake.model.Food;
import ru.nsu.maltsev.task_2_3_1.snake.model.GameConfig;
import ru.nsu.maltsev.task_2_3_1.snake.model.GameModel;
import ru.nsu.maltsev.task_2_3_1.snake.model.GameStatus;
import ru.nsu.maltsev.task_2_3_1.snake.model.Position;

public class GameController {
    @FXML
    private BorderPane root;

    @FXML
    private Canvas boardCanvas;

    @FXML
    private Label statusLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label levelLabel;

    private GameModel model;
    private Timeline timeline;
    private int currentTickMillis;

    @FXML
    private void initialize() {
        model = new GameModel(GameConfig.defaultConfig());

        root.setFocusTraversable(true);
        root.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);

        Platform.runLater(root::requestFocus);

        render();
        startGameLoop();
    }

    @FXML
    private void onRestartClicked() {
        model.reset();
        render();
        startGameLoop();
        root.requestFocus();
    }

    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
            case W:
                model.setDirection(Direction.UP);
                break;
            case DOWN:
            case S:
                model.setDirection(Direction.DOWN);
                break;
            case LEFT:
            case A:
                model.setDirection(Direction.LEFT);
                break;
            case RIGHT:
            case D:
                model.setDirection(Direction.RIGHT);
                break;
            case R:
                onRestartClicked();
                break;
            default:
                return;
        }

        event.consume();
    }

    private void startGameLoop() {
        stopGameLoop();

        currentTickMillis = model.getTickMillis();
        timeline = new Timeline(new KeyFrame(Duration.millis(currentTickMillis), event -> onTick()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void stopGameLoop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    private void onTick() {
        model.step();
        render();

        if (model.getStatus() != GameStatus.RUNNING) {
            stopGameLoop();
            return;
        }

        if (model.getTickMillis() != currentTickMillis) {
            startGameLoop();
        }
    }

    private void render() {
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();

        double width = boardCanvas.getWidth();
        double height = boardCanvas.getHeight();

        int rows = model.getConfig().getRows();
        int columns = model.getConfig().getColumns();

        double cellWidth = width / columns;
        double cellHeight = height / rows;

        gc.setFill(Color.rgb(25, 28, 34));
        gc.fillRect(0, 0, width, height);

        drawGrid(gc, rows, columns, cellWidth, cellHeight);
        drawObstacles(gc, cellWidth, cellHeight);
        drawFood(gc, cellWidth, cellHeight);
        drawSnake(gc, cellWidth, cellHeight);
        updateLabels();
    }

    private void drawGrid(GraphicsContext gc, int rows, int columns, double cellWidth, double cellHeight) {
        gc.setStroke(Color.rgb(45, 50, 60));
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

    private void drawObstacles(GraphicsContext gc, double cellWidth, double cellHeight) {
        gc.setFill(Color.rgb(110, 110, 120));

        for (Position obstacle : model.getObstacles()) {
            fillCell(gc, obstacle, cellWidth, cellHeight, 0.12);
        }
    }

    private void drawFood(GraphicsContext gc, double cellWidth, double cellHeight) {
        gc.setFill(Color.rgb(230, 70, 70));

        for (Food food : model.getFoods()) {
            Position position = food.getPosition();

            double paddingX = cellWidth * 0.2;
            double paddingY = cellHeight * 0.2;

            gc.fillOval(
                    position.getColumn() * cellWidth + paddingX,
                    position.getRow() * cellHeight + paddingY,
                    cellWidth - paddingX * 2,
                    cellHeight - paddingY * 2
            );
        }
    }

    private void drawSnake(GraphicsContext gc, double cellWidth, double cellHeight) {
        boolean head = true;

        for (Position part : model.getSnake()) {
            if (head) {
                gc.setFill(Color.rgb(85, 220, 120));
                head = false;
            } else {
                gc.setFill(Color.rgb(55, 165, 95));
            }

            fillCell(gc, part, cellWidth, cellHeight, 0.08);
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
                8,
                8
        );
    }

    private void updateLabels() {
        scoreLabel.setText("Score: " + model.getScore());
        levelLabel.setText("Level: " + model.getLevel());

        if (model.getStatus() == GameStatus.RUNNING) {
            statusLabel.setText("Use WASD or arrows to move");
        } else if (model.getStatus() == GameStatus.WON) {
            statusLabel.setText("You won. Press R or New game");
        } else {
            statusLabel.setText("Game over. Press R or New game");
        }
    }
}
