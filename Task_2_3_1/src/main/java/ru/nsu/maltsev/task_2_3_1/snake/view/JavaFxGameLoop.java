package ru.nsu.maltsev.task_2_3_1.snake.view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class JavaFxGameLoop {
    private Timeline timeline;
    private Runnable tickHandler;
    private int currentTickMillis;

    public void start(int tickMillis, Runnable tickHandler) {
        stop();

        this.tickHandler = tickHandler;
        this.currentTickMillis = tickMillis;

        timeline = new Timeline(new KeyFrame(Duration.millis(tickMillis), event -> tickHandler.run()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void restartIfDelayChanged(int tickMillis) {
        if (timeline != null && tickMillis != currentTickMillis) {
            start(tickMillis, tickHandler);
        }
    }

    public void stop() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }
}