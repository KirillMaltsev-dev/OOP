package ru.nsu.maltsev.task_2_3_1.snake.controller.port;

public interface GameLoop {
    void start(int tickMillis, Runnable tickHandler);

    void restartIfDelayChanged(int tickMillis);

    void stop();
}
