package ru.nsu.maltsev.task_2_4_1.checker.dsl;

import groovy.lang.Closure;
import groovy.lang.Script;

public abstract class DslScript extends Script {
    protected void importConfig(String path) {
        DslEnvironment.current().importConfig(path);
    }

    protected void tasks(Closure<?> closure) {
        DslEnvironment.current().configure(new TasksDelegate(), closure);
    }

    protected void groups(Closure<?> closure) {
        DslEnvironment.current().configure(new GroupsDelegate(), closure);
    }

    protected void check(Closure<?> closure) {
        DslEnvironment.current().configure(new CheckDelegate(), closure);
    }

    protected void checkpoints(Closure<?> closure) {
        DslEnvironment.current().configure(new CheckpointsDelegate(), closure);
    }

    protected void submissions(Closure<?> closure) {
        DslEnvironment.current().configure(new SubmissionsDelegate(), closure);
    }

    protected void settings(Closure<?> closure) {
        DslEnvironment.current().configure(new SettingsDelegate(), closure);
    }
}