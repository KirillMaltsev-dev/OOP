package ru.nsu.maltsev.task_2_4_1.checker.check;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class SimpleGoogleStyleChecker {
    private static final int MAX_LINE_LENGTH = 100;

    public StyleCheckResult check(Path taskDirectory) {
        Path sourceDirectory = taskDirectory.resolve("src").resolve("main").resolve("java");
        StyleCheckResult result = new StyleCheckResult();

        if (!Files.exists(sourceDirectory)) {
            result.addViolation("Source directory does not exist: " + sourceDirectory);
            return result;
        }

        try (Stream<Path> paths = Files.walk(sourceDirectory)) {
            paths.filter(path -> path.getFileName().toString().endsWith(".java"))
                    .forEach(path -> checkFile(path, result));
        } catch (Exception exception) {
            result.addViolation("Cannot scan Java sources: " + exception.getMessage());
        }

        return result;
    }

    private void checkFile(Path path, StyleCheckResult result) {
        try {
            List<String> lines = Files.readAllLines(path);

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                int lineNumber = i + 1;

                if (line.contains("\t")) {
                    result.addViolation(path + ":" + lineNumber + " contains tab character");
                }

                if (line.length() > MAX_LINE_LENGTH) {
                    result.addViolation(path + ":" + lineNumber + " line is longer than 100 characters");
                }

                if (line.matches("\\s*import\\s+.*\\.\\*;\\s*")) {
                    result.addViolation(path + ":" + lineNumber + " contains wildcard import");
                }
            }
        } catch (Exception exception) {
            result.addViolation("Cannot read file " + path + ": " + exception.getMessage());
        }
    }
}