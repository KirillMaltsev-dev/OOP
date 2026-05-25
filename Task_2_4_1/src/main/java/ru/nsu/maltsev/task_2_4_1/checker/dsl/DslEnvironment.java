package ru.nsu.maltsev.task_2_4_1.checker.dsl;

import groovy.lang.Closure;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import ru.nsu.maltsev.task_2_4_1.checker.model.CourseConfig;

public class DslEnvironment {
    private static final ThreadLocal<DslEnvironment> CURRENT = new ThreadLocal<>();

    private final CourseConfig config;
    private final Deque<Path> directoryStack = new ArrayDeque<>();

    public DslEnvironment(CourseConfig config) {
        this.config = config;
    }

    public static DslEnvironment current() {
        DslEnvironment environment = CURRENT.get();
        if (environment == null) {
            throw new IllegalStateException("DSL environment is not initialized");
        }

        return environment;
    }

    public CourseConfig getConfig() {
        return config;
    }

    public void loadFile(Path path) {
        Path absolutePath = path.toAbsolutePath().normalize();
        Path parent = absolutePath.getParent();

        directoryStack.push(parent);
        DslEnvironment previous = CURRENT.get();
        CURRENT.set(this);

        try {
            DslLoader.evaluate(absolutePath);
        } finally {
            CURRENT.set(previous);
            directoryStack.pop();
        }
    }

    public void importConfig(String path) {
        Path currentDirectory = directoryStack.peek();
        if (currentDirectory == null) {
            throw new IllegalStateException("Cannot resolve import without current directory");
        }

        loadFile(currentDirectory.resolve(path));
    }

    public void configure(Object delegate, Closure<?> closure) {
        Closure<?> clonedClosure = (Closure<?>) closure.clone();
        clonedClosure.setDelegate(delegate);
        clonedClosure.setResolveStrategy(Closure.DELEGATE_FIRST);
        clonedClosure.call();
    }
}