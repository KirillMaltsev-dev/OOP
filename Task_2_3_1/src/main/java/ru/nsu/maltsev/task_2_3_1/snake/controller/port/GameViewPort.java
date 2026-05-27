package ru.nsu.maltsev.task_2_3_1.snake.controller.port;

import ru.nsu.maltsev.task_2_3_1.snake.model.GameModel;
import ru.nsu.maltsev.task_2_3_1.snake.model.Position;

public interface GameViewPort {
    void render(GameModel model);

    void showStartMenu();

    void showGameScreen();

    void showGameOver(boolean victoryAchieved, int score);

    void playExplosion(GameModel model, Position explosionPosition, Runnable onFinished);

    void stopExplosionAnimation();

    void requestFocus();

    void shutdown();
}
