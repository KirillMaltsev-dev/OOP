package ru.nsu.maltsev.task_2_4_1.checker.check;

import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import ru.nsu.maltsev.task_2_4_1.checker.git.GitService;
import ru.nsu.maltsev.task_2_4_1.checker.grading.ScoreCalculator;
import ru.nsu.maltsev.task_2_4_1.checker.model.CheckRequest;
import ru.nsu.maltsev.task_2_4_1.checker.model.CourseConfig;
import ru.nsu.maltsev.task_2_4_1.checker.model.GroupConfig;
import ru.nsu.maltsev.task_2_4_1.checker.model.StudentConfig;
import ru.nsu.maltsev.task_2_4_1.checker.model.TaskConfig;
import ru.nsu.maltsev.task_2_4_1.checker.process.CommandRunner;
import ru.nsu.maltsev.task_2_4_1.checker.report.HtmlReportGenerator;

public class CheckerApplication {
    private final CourseConfig config;

    public CheckerApplication(CourseConfig config) {
        this.config = config;
    }

    public String run() {
        List<TaskCheckResult> results = new ArrayList<>();

        CommandRunner commandRunner = new CommandRunner();
        Duration timeout = Duration.ofSeconds(config.getSettings().getCommandTimeoutSeconds());

        GitService gitService = new GitService(commandRunner, timeout);
        GradleTaskChecker taskChecker = new GradleTaskChecker(commandRunner, timeout);
        ScoreCalculator scoreCalculator = new ScoreCalculator(config);

        Path repositoriesDirectory = config.getSettings()
                .getWorkDir()
                .resolve("repositories")
                .toAbsolutePath()
                .normalize();

        for (StudentConfig student : resolveStudents()) {
            Path repositoryDirectory = null;

            try {
                repositoryDirectory = gitService.prepareRepository(student, repositoriesDirectory);
            } catch (Exception exception) {
                for (TaskConfig task : resolveTasks()) {
                    TaskCheckResult result = new TaskCheckResult(student, task);
                    result.setErrorMessage(exception.getMessage());
                    results.add(result);
                }

                continue;
            }

            for (TaskConfig task : resolveTasks()) {
                TaskCheckResult result = taskChecker.check(student, task, repositoryDirectory);
                result.setPoints(scoreCalculator.calculate(result));
                results.add(result);
            }
        }

        return new HtmlReportGenerator(config).generate(results);
    }

    private List<StudentConfig> resolveStudents() {
        Set<StudentConfig> students = new LinkedHashSet<>();

        if (config.getCheckRequests().isEmpty()) {
            for (GroupConfig group : config.getGroups().values()) {
                students.addAll(group.getStudents());
            }

            return new ArrayList<>(students);
        }

        for (CheckRequest request : config.getCheckRequests()) {
            for (String groupName : request.getGroupNames()) {
                GroupConfig group = config.getGroups().get(groupName);
                if (group != null) {
                    students.addAll(group.getStudents());
                }
            }

            for (String github : request.getStudentGithubs()) {
                findStudent(github, students);
            }
        }

        return new ArrayList<>(students);
    }

    private void findStudent(String github, Set<StudentConfig> students) {
        for (GroupConfig group : config.getGroups().values()) {
            for (StudentConfig student : group.getStudents()) {
                if (student.getGithub().equals(github)) {
                    students.add(student);
                    return;
                }
            }
        }
    }

    private List<TaskConfig> resolveTasks() {
        Set<TaskConfig> tasks = new LinkedHashSet<>();

        if (config.getCheckRequests().isEmpty()) {
            tasks.addAll(config.getTasks().values());
            return new ArrayList<>(tasks);
        }

        for (CheckRequest request : config.getCheckRequests()) {
            for (String taskId : request.getTaskIds()) {
                TaskConfig task = config.getTasks().get(taskId);
                if (task != null) {
                    tasks.add(task);
                }
            }
        }

        if (tasks.isEmpty()) {
            tasks.addAll(config.getTasks().values());
        }

        return new ArrayList<>(tasks);
    }
}