package ru.nsu.maltsev.task_2_4_1.checker.check;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import ru.nsu.maltsev.task_2_4_1.checker.model.StudentConfig;
import ru.nsu.maltsev.task_2_4_1.checker.model.TaskConfig;
import ru.nsu.maltsev.task_2_4_1.checker.process.CommandResult;
import ru.nsu.maltsev.task_2_4_1.checker.process.CommandRunner;

public class GradleTaskChecker {
    private final CommandRunner commandRunner;
    private final Duration timeout;
    private final TestReportParser testReportParser = new TestReportParser();
    private final SimpleGoogleStyleChecker styleChecker = new SimpleGoogleStyleChecker();

    public GradleTaskChecker(CommandRunner commandRunner, Duration timeout) {
        this.commandRunner = commandRunner;
        this.timeout = timeout;
    }

    public TaskCheckResult check(StudentConfig student, TaskConfig task, Path repositoryDirectory) {
        TaskCheckResult result = new TaskCheckResult(student, task);
        result.setRepositoryPrepared(true);

        Path taskDirectory = repositoryDirectory.resolve(task.getId());
        if (!Files.exists(taskDirectory)) {
            result.setErrorMessage("Task directory does not exist: " + taskDirectory);
            return result;
        }

        result.setTaskDirectoryExists(true);

        CommandResult compilation = runGradle(taskDirectory, "compileJava");
        result.setCompilationPassed(compilation.isSuccess());
        if (!compilation.isSuccess()) {
            result.setErrorMessage(shortOutput(compilation));
            return result;
        }

        CommandResult javadoc = runGradle(taskDirectory, "javadoc");
        result.setJavadocPassed(javadoc.isSuccess());
        if (!javadoc.isSuccess()) {
            result.setErrorMessage(shortOutput(javadoc));
            return result;
        }

        StyleCheckResult styleCheckResult = checkStyle(taskDirectory);
        result.setStylePassed(styleCheckResult.isPassed());
        if (!styleCheckResult.isPassed()) {
            result.setErrorMessage(String.join("\n", styleCheckResult.getViolations()));
            return result;
        }

        CommandResult tests = runGradle(taskDirectory, "test");
        TestSummary testSummary = testReportParser.parse(taskDirectory);
        result.setTestSummary(testSummary);

        boolean testsPassed = tests.isSuccess() && testSummary.getFailed() == 0;
        result.setTestsPassed(testsPassed);

        if (!testsPassed) {
            result.setErrorMessage(shortOutput(tests));
        }

        return result;
    }

    private StyleCheckResult checkStyle(Path taskDirectory) {
        StyleCheckResult result = new StyleCheckResult();

        if (hasCheckstylePlugin(taskDirectory)) {
            CommandResult checkstyle = runGradle(taskDirectory, "checkstyleMain");

            if (!checkstyle.isSuccess()) {
                result.addViolation(shortOutput(checkstyle));
            }

            return result;
        }

        return styleChecker.check(taskDirectory);
    }

    private boolean hasCheckstylePlugin(Path taskDirectory) {
        Path buildGradle = taskDirectory.resolve("build.gradle");
        Path buildGradleKts = taskDirectory.resolve("build.gradle.kts");

        return fileContains(buildGradle, "checkstyle") || fileContains(buildGradleKts, "checkstyle");
    }

    private boolean fileContains(Path path, String text) {
        try {
            return Files.exists(path) && Files.readString(path).contains(text);
        } catch (Exception exception) {
            return false;
        }
    }

    private CommandResult runGradle(Path directory, String taskName) {
        List<String> command = new ArrayList<>();
        command.add(resolveGradleCommand(directory));
        command.add(taskName);
        command.add("--console");
        command.add("plain");

        return commandRunner.run(command, directory, timeout);
    }

    private String resolveGradleCommand(Path directory) {
        Path gradlew = directory.resolve(isWindows() ? "gradlew.bat" : "gradlew");
        if (Files.exists(gradlew)) {
            return gradlew.toAbsolutePath().toString();
        }

        return isWindows() ? "gradle.bat" : "gradle";
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private String shortOutput(CommandResult result) {
        String output = result.getOutput();
        if (output == null || output.isBlank()) {
            return result.isTimedOut() ? "Command timed out" : "Command failed";
        }

        int maxLength = Math.min(output.length(), 1000);
        return output.substring(0, maxLength);
    }
}