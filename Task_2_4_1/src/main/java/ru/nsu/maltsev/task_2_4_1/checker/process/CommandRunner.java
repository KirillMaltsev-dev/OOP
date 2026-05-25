package ru.nsu.maltsev.task_2_4_1.checker.process;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandRunner {
    public CommandResult run(List<String> command, Path workingDirectory, Duration timeout) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(workingDirectory.toFile());
        processBuilder.redirectErrorStream(true);
        processBuilder.environment().put("GIT_TERMINAL_PROMPT", "0");

        try {
            Process process = processBuilder.start();
            boolean finished = process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);

            if (!finished) {
                process.destroyForcibly();
                String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                return new CommandResult(-1, output, true);
            }

            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return new CommandResult(process.exitValue(), output, false);
        } catch (IOException exception) {
            return new CommandResult(-1, exception.getMessage(), false);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return new CommandResult(-1, "Command was interrupted", false);
        }
    }
}