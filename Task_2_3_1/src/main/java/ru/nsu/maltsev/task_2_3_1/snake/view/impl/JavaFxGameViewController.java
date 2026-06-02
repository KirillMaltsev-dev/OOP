package ru.nsu.maltsev.task_2_3_1.snake.view.impl;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import ru.nsu.maltsev.task_2_3_1.snake.view.GameController;
import ru.nsu.maltsev.task_2_3_1.snake.model.Direction;

public class JavaFxGameViewController {
    @FXML
    private StackPane root;

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

    private GameViewImpl gameViewImpl;
    private GameController gameController;

    @FXML
    private void initialize() {
        gameViewImpl = new GameViewImpl(
                root,
                startMenu,
                gameOverMenu,
                boardCanvas,
                statusLabel,
                scoreLabel,
                levelLabel,
                gameOverTitleLabel,
                gameOverScoreLabel
        );

        gameController = new GameController(
                gameViewImpl,
                new JavaFxGameLoop(),
                new JavaFxSoundPlayer()
        );

        root.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        gameController.initialize();
    }

    @FXML
    private void onStartClicked() {
        gameController.startGame();
    }

    @FXML
    private void onRestartClicked() {
        gameController.restartGame();
    }

    public void shutdown() {
        gameController.shutdown();
    }

    private void handleKeyPressed(KeyEvent event) {
        if (gameViewImpl.isStartMenuVisible()) {
            handleStartMenuKey(event);
            return;
        }

        if (gameViewImpl.isGameOverMenuVisible()) {
            handleGameOverKey(event);
            return;
        }

        if (!gameController.isGameRunning()) {
            return;
        }

        handleGameKey(event);
    }

    private void handleStartMenuKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            gameController.startGame();
            event.consume();
        }
    }

    private void handleGameOverKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.R) {
            gameController.restartGame();
            event.consume();
        }
    }

    private void handleGameKey(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
            case W:
                gameController.changeDirection(Direction.UP);
                break;
            case DOWN:
            case S:
                gameController.changeDirection(Direction.DOWN);
                break;
            case LEFT:
            case A:
                gameController.changeDirection(Direction.LEFT);
                break;
            case RIGHT:
            case D:
                gameController.changeDirection(Direction.RIGHT);
                break;
            case R:
                gameController.restartGame();
                break;
            default:
                return;
        }

        event.consume();
    }
}
