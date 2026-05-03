package ru.nsu.maltsev.task_2_2_1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MainTest {

    @TempDir
    Path tempDir;

    @Test
    void mainRunsWithValidConfig() throws IOException, InterruptedException {
        Path file = tempDir.resolve("config.json");
        Files.writeString(file, """
                {
                  "warehouseCapacity": 2,
                  "workDurationMs": 200,
                  "orderIntervalMs": 10,
                  "deliveryTimeMs": 10,
                  "ordersCount": 3,
                  "bakers": [10],
                  "couriers": [1]
                }
                """, StandardCharsets.UTF_8);

        Main.main(new String[]{file.toString()});
    }

    @Test
    void mainRejectsWrongArgumentsCount() {
        assertThrows(IllegalArgumentException.class, () -> Main.main(new String[]{}));
        assertThrows(IllegalArgumentException.class, () -> Main.main(new String[]{"a", "b"}));
    }
}