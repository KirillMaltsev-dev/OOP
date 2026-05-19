package ru.nsu.maltsev.task_2_3_1.snake.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import ru.nsu.maltsev.task_2_3_1.snake.model.Direction;
import ru.nsu.maltsev.task_2_3_1.snake.model.GameConfig;
import ru.nsu.maltsev.task_2_3_1.snake.model.GameModel;
import ru.nsu.maltsev.task_2_3_1.snake.model.GameStatus;
import ru.nsu.maltsev.task_2_3_1.snake.sound.AudioManager;
import ru.nsu.maltsev.task_2_3_1.snake.view.GameView;
import ru.nsu.maltsev.task_2_3_1.snake.view.JavaFxGameLoop;

public class GameController {
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

    private GameModel model;
    private GameView gameView;
    private JavaFxGameLoop gameLoop;

    private final AudioManager audioManager = new AudioManager();

    @FXML
    private void initialize() {
        model = new GameModel(GameConfig.defaultConfig());

        gameView = new GameView(
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

        gameLoop = new JavaFxGameLoop();

        root.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);

        gameView.initialize(model);
        audioManager.playMenuMusic();
    }

    @FXML
    private void onStartClicked() {
        gameView.stopExplosionAnimation();

        model.reset();

        gameView.showGameScreen();
        gameView.render(model);

        gameLoop.start(model.getTickMillis(), this::onTick);

        audioManager.playGameMusic();
        gameView.requestFocus();
    }

    @FXML
    private void onRestartClicked() {
        onStartClicked();
    }

    private void handleKeyPressed(KeyEvent event) {
        if (gameView.isStartMenuVisible() && event.getCode() == KeyCode.ENTER) {
            onStartClicked();
            event.consume();
            return;
        }

        if (gameView.isGameOverMenuVisible()) {
            handleGameOverKey(event);
            return;
        }

        if (model.getStatus() != GameStatus.RUNNING) {
            return;
        }

        handleGameKey(event);
    }

    private void handleGameOverKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.R) {
            onStartClicked();
            event.consume();
        }
    }

    private void handleGameKey(KeyEvent event) {
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

    private void onTick() {
        model.step();

        if (model.isFoodEatenOnLastStep()) {
            audioManager.playEatSound();
        }

        gameView.render(model);

        if (model.getStatus() == GameStatus.LOST) {
            gameLoop.stop();
            startDeathSequence();
            return;
        }

        gameLoop.restartIfDelayChanged(model.getTickMillis());
    }

    private void startDeathSequence() {
        audioManager.stopMusic();
        audioManager.playDeathSound();

        gameView.playExplosion(model, model.getDeathPosition(), this::showGameOverMenu);
    }

    private void showGameOverMenu() {
        boolean victoryAchieved = model.isVictoryAchieved();

        gameView.showGameOver(victoryAchieved, model.getScore());

        if (victoryAchieved) {
            audioManager.playWinMusic();
        } else {
            audioManager.playLoseMusic();
        }
    }

    public void shutdown() {
        gameLoop.stop();
        gameView.shutdown();
        audioManager.stopMusic();
    }
}