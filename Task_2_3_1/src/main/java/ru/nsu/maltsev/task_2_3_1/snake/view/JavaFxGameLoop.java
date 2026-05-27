package ru.nsu.maltsev.task_2_3_1.snake.view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import ru.nsu.maltsev.task_2_3_1.snake.controller.port.GameLoop;

public class JavaFxGameLoop implements GameLoop {
    private Timeline timeline;
    private Runnable tickHandler;
    private int currentTickMillis;

    @Override
    public void start(int tickMillis, Runnable tickHandler) {
        stop();

        this.tickHandler = tickHandler;
        this.currentTickMillis = tickMillis;

        timeline = new Timeline(new KeyFrame(Duration.millis(tickMillis), event -> tickHandler.run()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @Override
    public void restartIfDelayChanged(int tickMillis) {
        if (timeline != null && tickMillis != currentTickMillis) {
            start(tickMillis, tickHandler);
        }
    }

    @Override
    public void stop() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }
}
