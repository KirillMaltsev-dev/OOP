package ru.nsu.maltsev.task_2_4_1.checker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import ru.nsu.maltsev.task_2_4_1.checker.dsl.DslLoader;
import ru.nsu.maltsev.task_2_4_1.checker.model.CourseConfig;

class DslLoaderTest {
    @Test
    void loadsSimpleDslConfig() throws Exception {
        Path directory = Files.createTempDirectory("oop-dsl-test");
        Path config = directory.resolve("oopcheck.groovy");

        Files.writeString(config, """
                tasks {
                    task("Task_2_1_1") {
                        title "Prime checker"
                        maxPoints 1.0
                        softDeadline "2025-03-01"
                        hardDeadline "2025-03-10"
                    }
                }

                groups {
                    group("22214") {
                        student {
                            github "student"
                            fullName "Student Name"
                            repo "https://github.com/student/OOP.git"
                        }
                    }
                }
                """);

        CourseConfig courseConfig = new DslLoader().load(config);

        assertEquals(1, courseConfig.getTasks().size());
        assertEquals(1, courseConfig.getGroups().size());
        assertEquals("Prime checker", courseConfig.getTasks().get("Task_2_1_1").getTitle());
    }
}