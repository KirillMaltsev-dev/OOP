package ru.nsu.maltsev.task_2_4_1.checker.dsl;

import groovy.lang.GroovyShell;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.nsu.maltsev.task_2_4_1.checker.model.CourseConfig;

public class DslLoader {
    public CourseConfig load(Path configPath) {
        if (!Files.exists(configPath)) {
            throw new IllegalArgumentException("Config file not found: " + configPath);
        }

        CourseConfig config = new CourseConfig();
        DslEnvironment environment = new DslEnvironment(config);

        environment.loadFile(configPath.toAbsolutePath().normalize());
        return config;
    }

    static void evaluate(Path path) {
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setScriptBaseClass(DslScript.class.getName());

        GroovyShell shell = new GroovyShell(DslLoader.class.getClassLoader(), compilerConfiguration);

        try {
            shell.evaluate(path.toFile());
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot read DSL file: " + path, exception);
        }
    }
}