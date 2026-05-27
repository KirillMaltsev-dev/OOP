package ru.nsu.maltsev.task_2_4_1.checker.dsl;

import groovy.lang.Closure;
import ru.nsu.maltsev.task_2_4_1.checker.model.SubmissionConfig;

public class SubmissionsDelegate {
    public void submitted(Closure<?> closure) {
        SubmissionConfig submission = new SubmissionConfig();
        DslEnvironment.current().configure(new SubmittedDelegate(submission), closure);
        DslEnvironment.current().getConfig().addSubmission(submission);
    }

    public static class SubmittedDelegate {
        private final SubmissionConfig submission;

        public SubmittedDelegate(SubmissionConfig submission) {
            this.submission = submission;
        }

        public void student(String studentGithub) {
            submission.setStudentGithub(studentGithub);
        }

        public void task(String taskId) {
            submission.setTaskId(taskId);
        }

        public void softPassed(boolean softPassed) {
            submission.setSoftPassed(softPassed);
        }

        public void hardPassed(boolean hardPassed) {
            submission.setHardPassed(hardPassed);
        }
    }
}