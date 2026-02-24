package ru.nsu.maltsev.task_2_1_1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);

        deleteFileIfExists("performance_results.csv");
        deleteFileIfExists("analysis_report.txt");
    }

    private void deleteFileIfExists(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testMainExecution(@TempDir Path tempDir) throws IOException {
        Main.main(new String[]{});

        String output = outContent.toString();
        assertFalse(output.isEmpty());

        File csvFile = new File("performance_results.csv");
        File reportFile = new File("analysis_report.txt");

        assertTrue(csvFile.exists(), "CSV файл не создан");
        assertTrue(reportFile.exists(), "Файл отчета не создан");
        assertTrue(csvFile.length() > 0, "CSV файл пуст");

        String csvContent = new String(Files.readAllBytes(csvFile.toPath()));
        assertTrue(csvContent.contains("Method,Threads,Time_ms,Speedup"));
        assertTrue(csvContent.contains("Sequential,1"));
        assertTrue(csvContent.contains("ParallelStream"));
    }
}