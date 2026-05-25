package ru.nsu.maltsev.task_2_4_1.checker.dsl;

import groovy.lang.Closure;
import java.nio.file.Paths;
import ru.nsu.maltsev.task_2_4_1.checker.model.BonusConfig;
import ru.nsu.maltsev.task_2_4_1.checker.model.GradeRule;

public class SettingsDelegate {
    public void workDir(String workDir) {
        DslEnvironment.current().getConfig().getSettings().setWorkDir(Paths.get(workDir));
    }

    public void commandTimeoutSeconds(int seconds) {
        DslEnvironment.current().getConfig().getSettings().setCommandTimeoutSeconds(seconds);
    }

    public void grade(String name, double minPoints) {
        DslEnvironment.current().getConfig().getSettings().addGradeRule(new GradeRule(name, minPoints));
    }

    public void bonus(Closure<?> closure) {
        BonusConfig bonus = new BonusConfig();
        DslEnvironment.current().configure(new BonusDelegate(bonus), closure);
        DslEnvironment.current().getConfig().addBonus(bonus);
    }

    public static class BonusDelegate {
        private final BonusConfig bonus;

        public BonusDelegate(BonusConfig bonus) {
            this.bonus = bonus;
        }

        public void student(String studentGithub) {
            bonus.setStudentGithub(studentGithub);
        }

        public void task(String taskId) {
            bonus.setTaskId(taskId);
        }

        public void points(double points) {
            bonus.setPoints(points);
        }

        public void reason(String reason) {
            bonus.setReason(reason);
        }
    }
}