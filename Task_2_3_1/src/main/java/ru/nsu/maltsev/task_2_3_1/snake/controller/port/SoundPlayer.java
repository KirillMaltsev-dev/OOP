package ru.nsu.maltsev.task_2_3_1.snake.controller.port;

public interface SoundPlayer {
    void playMenuMusic();

    void playGameMusic();

    void playWinMusic();

    void playLoseMusic();

    void playDeathSound();

    void playEatSound();

    void stopMusic();
}
