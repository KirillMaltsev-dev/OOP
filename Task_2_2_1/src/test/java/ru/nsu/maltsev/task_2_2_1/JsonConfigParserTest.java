package ru.nsu.maltsev.task_2_2_1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonConfigParserTest {

    @TempDir
    Path tempDir;

    @Test
    void parsesValidJson() throws IOException {
        Path file = tempDir.resolve("config.json");
        Files.writeString(file, """
                {
                  "warehouseCapacity": 3,
                  "workDurationMs": 5000,
                  "orderIntervalMs": 200,
                  "deliveryTimeMs": 1000,
                  "ordersCount": 10,
                  "bakers": [500, 700],
                  "couriers": [2, 3]
                }
                """, StandardCharsets.UTF_8);

        PizzeriaConfig config = JsonConfigParser.parse(file.toString());

        assertEquals(3, config.getWarehouseCapacity());
        assertEquals(5000, config.getWorkDurationMs());
        assertEquals(200, config.getOrderIntervalMs());
        assertEquals(1000, config.getDeliveryTimeMs());
        assertEquals(10, config.getOrdersCount());
        assertEquals(List.of(500, 700), config.getBakerCookTimesMs());
        assertEquals(List.of(2, 3), config.getCourierCapacities());
    }

    @Test
    void parsesEmptyArrays() throws IOException {
        Path file = tempDir.resolve("config-empty.json");
        Files.writeString(file, """
                {
                  "warehouseCapacity": 3,
                  "workDurationMs": 5000,
                  "orderIntervalMs": 200,
                  "deliveryTimeMs": 1000,
                  "ordersCount": 10,
                  "bakers": [],
                  "couriers": []
                }
                """, StandardCharsets.UTF_8);

        assertThrows(IllegalArgumentException.class, () -> JsonConfigParser.parse(file.toString()));
    }

    @Test
    void failsWhenFieldMissing() throws IOException {
        Path file = tempDir.resolve("config-missing.json");
        Files.writeString(file, """
                {
                  "warehouseCapacity": 3,
                  "workDurationMs": 5000,
                  "orderIntervalMs": 200,
                  "deliveryTimeMs": 1000,
                  "bakers": [500],
                  "couriers": [2]
                }
                """, StandardCharsets.UTF_8);

        assertThrows(IllegalArgumentException.class, () -> JsonConfigParser.parse(file.toString()));
    }
}