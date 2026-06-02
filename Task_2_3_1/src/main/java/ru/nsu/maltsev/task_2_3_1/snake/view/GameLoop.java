package ru.nsu.maltsev.task_2_3_1.snake.view;

public interface GameLoop {
    void start(int tickMillis, Runnable tickHandler);

    void restartIfDelayChanged(int tickMillis);

    void stop();
}
