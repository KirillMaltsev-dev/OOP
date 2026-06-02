package ru.nsu.maltsev.task_2_3_1.snake.view;

import ru.nsu.maltsev.task_2_3_1.snake.model.Direction;
import ru.nsu.maltsev.task_2_3_1.snake.model.GameConfig;
import ru.nsu.maltsev.task_2_3_1.snake.model.GameModel;
import ru.nsu.maltsev.task_2_3_1.snake.model.GameStatus;

public class GameController {
    private final GameModel model;
    private final GameView gameView;
    private final GameLoop gameLoop;
    private final SoundPlayer soundPlayer;

    public GameController(GameView gameView, GameLoop gameLoop, SoundPlayer soundPlayer) {
        this.model = new GameModel(GameConfig.defaultConfig());
        this.gameView = gameView;
        this.gameLoop = gameLoop;
        this.soundPlayer = soundPlayer;
    }

    public void initialize() {
        gameView.showStartMenu();
        gameView.render(model);
        soundPlayer.playMenuMusic();
        gameView.requestFocus();
    }

    public void startGame() {
        gameView.stopExplosionAnimation();
        model.reset();

        gameView.showGameScreen();
        gameView.render(model);

        gameLoop.start(model.getTickMillis(), this::onTick);
        soundPlayer.playGameMusic();
        gameView.requestFocus();
    }

    public void restartGame() {
        startGame();
    }

    public void changeDirection(Direction direction) {
        if (model.getStatus() == GameStatus.RUNNING) {
            model.setDirection(direction);
        }
    }

    public boolean isGameRunning() {
        return model.getStatus() == GameStatus.RUNNING;
    }

    public void shutdown() {
        gameLoop.stop();
        gameView.shutdown();
        soundPlayer.stopMusic();
    }

    private void onTick() {
        model.step();

        if (model.isFoodEatenOnLastStep()) {
            soundPlayer.playEatSound();
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
        soundPlayer.stopMusic();
        soundPlayer.playDeathSound();
        gameView.playExplosion(model, model.getDeathPosition(), this::showGameOverMenu);
    }

    private void showGameOverMenu() {
        boolean victoryAchieved = model.isVictoryAchieved();

        gameView.showGameOver(victoryAchieved, model.getScore());

        if (victoryAchieved) {
            soundPlayer.playWinMusic();
        } else {
            soundPlayer.playLoseMusic();
        }
    }
}
