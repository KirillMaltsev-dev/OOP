package ru.nsu.maltsev.task_2_4_1.checker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import ru.nsu.maltsev.task_2_4_1.checker.check.CheckerApplication;
import ru.nsu.maltsev.task_2_4_1.checker.dsl.DslLoader;
import ru.nsu.maltsev.task_2_4_1.checker.model.CourseConfig;

public class Main {
    private static final String DEFAULT_CONFIG_FILE = "oopcheck.groovy";

    public static void main(String[] args) {
        try {
            Path configPath = resolveConfigPath(args);

            CourseConfig config = new DslLoader().load(configPath);
            CheckerApplication application = new CheckerApplication(config);
            String htmlReport = application.run();

            System.out.println(htmlReport);
        } catch (Exception exception) {
            System.err.println("Application failed: " + exception.getMessage());
            exception.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static Path resolveConfigPath(String[] args) {
        if (args.length > 0) {
            return Paths.get(args[0]).toAbsolutePath().normalize();
        }

        Path defaultPath = Paths.get(DEFAULT_CONFIG_FILE).toAbsolutePath().normalize();
        if (!Files.exists(defaultPath)) {
            throw new IllegalArgumentException(
                    "Config file not found: " + defaultPath
                            + ". Create oopcheck.groovy in the working directory"
            );
        }

        return defaultPath;
    }
}