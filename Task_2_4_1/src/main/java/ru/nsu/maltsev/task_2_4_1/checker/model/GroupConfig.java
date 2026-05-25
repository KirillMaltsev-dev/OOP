package ru.nsu.maltsev.task_2_4_1.checker.model;

import java.util.ArrayList;
import java.util.List;

public class GroupConfig {
    private final String name;
    private final List<StudentConfig> students = new ArrayList<>();

    public GroupConfig(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<StudentConfig> getStudents() {
        return students;
    }

    public void addStudent(StudentConfig student) {
        students.add(student);
    }
}