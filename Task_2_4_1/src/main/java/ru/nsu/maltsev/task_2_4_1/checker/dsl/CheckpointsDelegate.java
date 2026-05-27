package ru.nsu.maltsev.task_2_4_1.checker.dsl;

import groovy.lang.Closure;
import java.time.LocalDate;
import ru.nsu.maltsev.task_2_4_1.checker.model.CheckpointConfig;

public class CheckpointsDelegate {
    public void checkpoint(String name, Closure<?> closure) {
        CheckpointConfig checkpoint = new CheckpointConfig(name);
        DslEnvironment.current().configure(new CheckpointDelegate(checkpoint), closure);
        DslEnvironment.current().getConfig().addCheckpoint(checkpoint);
    }

    public static class CheckpointDelegate {
        private final CheckpointConfig checkpoint;

        public CheckpointDelegate(CheckpointConfig checkpoint) {
            this.checkpoint = checkpoint;
        }

        public void date(String date) {
            checkpoint.setDate(LocalDate.parse(date));
        }
    }
}