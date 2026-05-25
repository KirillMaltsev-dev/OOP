package ru.nsu.maltsev.task_2_4_1.checker.dsl;

import groovy.lang.Closure;
import java.time.LocalDate;
import ru.nsu.maltsev.task_2_4_1.checker.model.TaskConfig;

public class TasksDelegate {
    public void task(String id, Closure<?> closure) {
        TaskConfig task = new TaskConfig(id);
        DslEnvironment.current().configure(new TaskDelegate(task), closure);
        DslEnvironment.current().getConfig().addTask(task);
    }

    public static class TaskDelegate {
        private final TaskConfig task;

        public TaskDelegate(TaskConfig task) {
            this.task = task;
        }

        public void title(String title) {
            task.setTitle(title);
        }

        public void maxPoints(double maxPoints) {
            task.setMaxPoints(maxPoints);
        }

        public void softDeadline(String date) {
            task.setSoftDeadline(LocalDate.parse(date));
        }

        public void hardDeadline(String date) {
            task.setHardDeadline(LocalDate.parse(date));
        }
    }
}