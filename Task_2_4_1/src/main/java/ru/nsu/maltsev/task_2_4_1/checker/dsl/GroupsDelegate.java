package ru.nsu.maltsev.task_2_4_1.checker.dsl;

import groovy.lang.Closure;
import ru.nsu.maltsev.task_2_4_1.checker.model.GroupConfig;
import ru.nsu.maltsev.task_2_4_1.checker.model.StudentConfig;

public class GroupsDelegate {
    public void group(String name, Closure<?> closure) {
        GroupConfig group = new GroupConfig(name);
        DslEnvironment.current().configure(new GroupDelegate(group), closure);
        DslEnvironment.current().getConfig().addGroup(group);
    }

    public static class GroupDelegate {
        private final GroupConfig group;

        public GroupDelegate(GroupConfig group) {
            this.group = group;
        }

        public void student(Closure<?> closure) {
            StudentConfig student = new StudentConfig();
            DslEnvironment.current().configure(new StudentDelegate(student), closure);
            group.addStudent(student);
        }
    }

    public static class StudentDelegate {
        private final StudentConfig student;

        public StudentDelegate(StudentConfig student) {
            this.student = student;
        }

        public void github(String github) {
            student.setGithub(github);
        }

        public void fullName(String fullName) {
            student.setFullName(fullName);
        }

        public void repo(String repositoryUrl) {
            student.setRepositoryUrl(repositoryUrl);
        }
    }
}