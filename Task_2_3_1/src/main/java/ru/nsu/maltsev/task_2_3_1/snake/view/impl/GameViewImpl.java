package ru.nsu.maltsev.task_2_3_1.snake.view.impl;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import ru.nsu.maltsev.task_2_3_1.snake.view.GameView;
import ru.nsu.maltsev.task_2_3_1.snake.model.GameModel;
import ru.nsu.maltsev.task_2_3_1.snake.model.GameStatus;
import ru.nsu.maltsev.task_2_3_1.snake.model.Position;

public class GameViewImpl implements GameView {
    private static final int EXPLOSION_MAX_FRAMES = 10;
    private static final int EXPLOSION_FRAME_MILLIS = 45;

    private final StackPane root;
    private final StackPane startMenu;
    private final StackPane gameOverMenu;

    private final Label statusLabel;
    private final Label scoreLabel;
    private final Label levelLabel;
    private final Label gameOverTitleLabel;
    private final Label gameOverScoreLabel;

    private final GameRenderer renderer;

    private Timeline explosionTimeline;

    public GameViewImpl(
            StackPane root,
            StackPane startMenu,
            StackPane gameOverMenu,
            Canvas boardCanvas,
            Label statusLabel,
            Label scoreLabel,
            Label levelLabel,
            Label gameOverTitleLabel,
            Label gameOverScoreLabel
    ) {
        this.root = root;
        this.startMenu = startMenu;
        this.gameOverMenu = gameOverMenu;
        this.statusLabel = statusLabel;
        this.scoreLabel = scoreLabel;
        this.levelLabel = levelLabel;
        this.gameOverTitleLabel = gameOverTitleLabel;
        this.gameOverScoreLabel = gameOverScoreLabel;
        this.renderer = new GameRenderer(boardCanvas);
    }

    @Override
    public void render(GameModel model) {
        renderer.render(model);
        updateLabels(model);
    }

    @Override
    public void showStartMenu() {
        root.setFocusTraversable(true);
        setMenuVisible(startMenu, true);
        setMenuVisible(gameOverMenu, false);
        Platform.runLater(root::requestFocus);
    }

    @Override
    public void showGameScreen() {
        setMenuVisible(startMenu, false);
        setMenuVisible(gameOverMenu, false);
    }

    @Override
    public void showGameOver(boolean victoryAchieved, int score) {
        if (victoryAchieved) {
            gameOverTitleLabel.setText("Victory!");
        } else {
            gameOverTitleLabel.setText("Game over");
        }

        gameOverScoreLabel.setText("Final score: " + score);
        setMenuVisible(gameOverMenu, true);
        requestFocus();
    }

    public boolean isStartMenuVisible() {
        return startMenu.isVisible();
    }

    public boolean isGameOverMenuVisible() {
        return gameOverMenu.isVisible();
    }

    @Override
    public void requestFocus() {
        root.requestFocus();
    }

    @Override
    public void playExplosion(GameModel model, Position explosionPosition, Runnable onFinished) {
        stopExplosionAnimation();

        if (explosionPosition == null) {
            onFinished.run();
            return;
        }

        final int[] frame = {0};

        explosionTimeline = new Timeline(new KeyFrame(Duration.millis(EXPLOSION_FRAME_MILLIS), event -> {
            render(model);
            renderer.drawExplosion(model, explosionPosition, frame[0], EXPLOSION_MAX_FRAMES);

            frame[0]++;

            if (frame[0] > EXPLOSION_MAX_FRAMES) {
                stopExplosionAnimation();
                onFinished.run();
            }
        }));

        explosionTimeline.setCycleCount(Animation.INDEFINITE);
        explosionTimeline.play();
    }

    @Override
    public void stopExplosionAnimation() {
        if (explosionTimeline != null) {
            explosionTimeline.stop();
            explosionTimeline = null;
        }
    }

    @Override
    public void shutdown() {
        stopExplosionAnimation();
    }

    private void updateLabels(GameModel model) {
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

    private void setMenuVisible(Node menu, boolean visible) {
        menu.setVisible(visible);
        menu.setManaged(visible);
    }
}
