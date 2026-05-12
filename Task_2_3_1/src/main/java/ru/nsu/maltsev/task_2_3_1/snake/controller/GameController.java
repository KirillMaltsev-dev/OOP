package ru.nsu.maltsev.task_2_3_1.snake.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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
    private StackPane root;

    @FXML
    private BorderPane gamePane;

    @FXML
    private StackPane startMenu;

    @FXML
    private StackPane gameOverMenu;

    @FXML
    private Canvas boardCanvas;

    @FXML
    private Label statusLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label levelLabel;

    @FXML
    private Label gameOverTitleLabel;

    @FXML
    private Label gameOverScoreLabel;

    private GameModel model;
    private Timeline timeline;
    private int currentTickMillis;

    @FXML
    private void initialize() {
        model = new GameModel(GameConfig.defaultConfig());

        root.setFocusTraversable(true);
        root.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);

        setMenuVisible(startMenu, true);
        setMenuVisible(gameOverMenu, false);

        render();

        Platform.runLater(root::requestFocus);
    }

    @FXML
    private void onStartClicked() {
        model.reset();

        setMenuVisible(startMenu, false);
        setMenuVisible(gameOverMenu, false);

        render();
        startGameLoop();

        root.requestFocus();
    }

    @FXML
    private void onRestartClicked() {
        onStartClicked();
    }

    private void handleKeyPressed(KeyEvent event) {
        if (startMenu.isVisible() && event.getCode().toString().equals("ENTER")) {
            onStartClicked();
            event.consume();
            return;
        }

        if (gameOverMenu.isVisible()) {
            switch (event.getCode()) {
                case ENTER:
                case R:
                    onStartClicked();
                    event.consume();
                    return;
                default:
                    return;
            }
        }

        if (model.getStatus() != GameStatus.RUNNING) {
            return;
        }

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
                onStartClicked();
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

        if (model.getStatus() == GameStatus.LOST) {
            stopGameLoop();
            showGameOverMenu();
            return;
        }

        if (model.getTickMillis() != currentTickMillis) {
            startGameLoop();
        }
    }

    private void showGameOverMenu() {
        if (model.isVictoryAchieved()) {
            gameOverTitleLabel.setText("Victory!");
        } else {
            gameOverTitleLabel.setText("Game over");
        }

        gameOverScoreLabel.setText("Final score: " + model.getScore());
        setMenuVisible(gameOverMenu, true);

        root.requestFocus();
    }

    private void setMenuVisible(Node menu, boolean visible) {
        menu.setVisible(visible);
        menu.setManaged(visible);
    }

    private void render() {
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();

        double width = boardCanvas.getWidth();
        double height = boardCanvas.getHeight();

        int rows = model.getConfig().getRows();
        int columns = model.getConfig().getColumns();

        double cellWidth = width / columns;
        double cellHeight = height / rows;

        gc.clearRect(0, 0, width, height);

        drawGrassBoard(gc, rows, columns, cellWidth, cellHeight);
        drawObstacles(gc, cellWidth, cellHeight);
        drawFood(gc, cellWidth, cellHeight);
        drawSnake(gc, cellWidth, cellHeight);
        updateLabels();
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

    private void drawObstacles(GraphicsContext gc, double cellWidth, double cellHeight) {
        for (Position obstacle : model.getObstacles()) {
            gc.setFill(Color.rgb(130, 110, 90));
            fillCell(gc, obstacle, cellWidth, cellHeight, 0.10);

            gc.setFill(Color.rgb(160, 140, 120));
            double x = obstacle.getColumn() * cellWidth + cellWidth * 0.22;
            double y = obstacle.getRow() * cellHeight + cellHeight * 0.22;
            gc.fillOval(x, y, cellWidth * 0.18, cellHeight * 0.18);
        }
    }

    private void drawFood(GraphicsContext gc, double cellWidth, double cellHeight) {
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

    private void drawSnake(GraphicsContext gc, double cellWidth, double cellHeight) {
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

    private void updateLabels() {
        scoreLabel.setText("Score: " + model.getScore());
        levelLabel.setText("Level: " + model.getLevel());

        if (model.getStatus() == GameStatus.LOST) {
            statusLabel.setText("Game over");
        } else if (model.isVictoryAchieved()) {
            statusLabel.setText("You won! Keep playing until collision");
        } else {
            statusLabel.setText("Use WASD or arrows to move");
        }
    }
}