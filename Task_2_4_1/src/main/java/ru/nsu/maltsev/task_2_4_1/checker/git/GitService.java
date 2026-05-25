package ru.nsu.maltsev.task_2_4_1.checker.git;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import ru.nsu.maltsev.task_2_4_1.checker.model.StudentConfig;
import ru.nsu.maltsev.task_2_4_1.checker.process.CommandResult;
import ru.nsu.maltsev.task_2_4_1.checker.process.CommandRunner;

public class GitService {
    private final CommandRunner commandRunner;
    private final Duration timeout;

    public GitService(CommandRunner commandRunner, Duration timeout) {
        this.commandRunner = commandRunner;
        this.timeout = timeout;
    }

    public Path prepareRepository(StudentConfig student, Path repositoriesDirectory) {
        Path absoluteRepositoriesDirectory = repositoriesDirectory.toAbsolutePath().normalize();
        Path repositoryDirectory = absoluteRepositoriesDirectory
                .resolve(sanitize(student.getGithub()))
                .toAbsolutePath()
                .normalize();

        createDirectories(absoluteRepositoriesDirectory);

        if (Files.exists(repositoryDirectory.resolve(".git"))) {
            CommandResult fetchResult = runGit(repositoryDirectory, "fetch", "--all", "--prune");

            if (!fetchResult.isSuccess()) {
                throw new IllegalStateException(
                        "Cannot fetch repository for " + student.getGithub() + ":\n"
                                + fetchResult.getOutput()
                );
            }
        } else {
            CommandResult cloneResult = commandRunner.run(
                    List.of(
                            "git",
                            "clone",
                            student.getRepositoryUrl(),
                            repositoryDirectory.toString()
                    ),
                    absoluteRepositoriesDirectory,
                    timeout
            );

            if (!cloneResult.isSuccess()) {
                throw new IllegalStateException(
                        "Cannot clone repository for " + student.getGithub() + ":\n"
                                + cloneResult.getOutput()
                );
            }
        }

        checkoutMainBranch(repositoryDirectory);
        return repositoryDirectory;
    }

    private void checkoutMainBranch(Path repositoryDirectory) {
        CommandResult checkoutMain = runGit(repositoryDirectory, "checkout", "main");
        if (checkoutMain.isSuccess()) {
            runGit(repositoryDirectory, "pull", "--ff-only");
            return;
        }

        CommandResult checkoutOriginMain = runGit(repositoryDirectory, "checkout", "-B", "main", "origin/main");
        if (checkoutOriginMain.isSuccess()) {
            runGit(repositoryDirectory, "pull", "--ff-only");
            return;
        }

        CommandResult checkoutMaster = runGit(repositoryDirectory, "checkout", "master");
        if (checkoutMaster.isSuccess()) {
            runGit(repositoryDirectory, "pull", "--ff-only");
            return;
        }

        CommandResult checkoutOriginMaster = runGit(repositoryDirectory, "checkout", "-B", "master", "origin/master");
        if (checkoutOriginMaster.isSuccess()) {
            runGit(repositoryDirectory, "pull", "--ff-only");
            return;
        }

        CommandResult branches = runGit(repositoryDirectory, "branch", "-a");

        throw new IllegalStateException(
                "Cannot checkout main or master in " + repositoryDirectory + "\n"
                        + "checkout main output:\n" + checkoutMain.getOutput() + "\n"
                        + "checkout origin/main output:\n" + checkoutOriginMain.getOutput() + "\n"
                        + "checkout master output:\n" + checkoutMaster.getOutput() + "\n"
                        + "checkout origin/master output:\n" + checkoutOriginMaster.getOutput() + "\n"
                        + "available branches:\n" + branches.getOutput()
        );
    }

    private CommandResult runGit(Path repositoryDirectory, String... arguments) {
        List<String> command = new ArrayList<>();
        command.add("git");
        command.addAll(List.of(arguments));

        return commandRunner.run(command, repositoryDirectory.toAbsolutePath().normalize(), timeout);
    }

    private void createDirectories(Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot create directory: " + path, exception);
        }
    }

    private String sanitize(String value) {
        return value.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}