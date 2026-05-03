package ru.nsu.maltsev.task_2_2_1;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PizzeriaConfigTest {

    @Test
    void createsValidConfig() {
        PizzeriaConfig config = new PizzeriaConfig(
                3,
                5000,
                200,
                1000,
                10,
                List.of(500, 700),
                List.of(2, 3)
        );

        assertEquals(3, config.getWarehouseCapacity());
        assertEquals(5000, config.getWorkDurationMs());
        assertEquals(200, config.getOrderIntervalMs());
        assertEquals(1000, config.getDeliveryTimeMs());
        assertEquals(10, config.getOrdersCount());
        assertEquals(List.of(500, 700), config.getBakerCookTimesMs());
        assertEquals(List.of(2, 3), config.getCourierCapacities());
    }

    @Test
    void rejectsInvalidWarehouseCapacity() {
        assertThrows(IllegalArgumentException.class, () ->
                new PizzeriaConfig(0, 1000, 100, 100, 1, List.of(1), List.of(1)));
    }

    @Test
    void rejectsInvalidDurationsAndCount() {
        assertThrows(IllegalArgumentException.class, () ->
                new PizzeriaConfig(1, -1, 100, 100, 1, List.of(1), List.of(1)));
        assertThrows(IllegalArgumentException.class, () ->
                new PizzeriaConfig(1, 1000, -1, 100, 1, List.of(1), List.of(1)));
        assertThrows(IllegalArgumentException.class, () ->
                new PizzeriaConfig(1, 1000, 100, -1, 1, List.of(1), List.of(1)));
        assertThrows(IllegalArgumentException.class, () ->
                new PizzeriaConfig(1, 1000, 100, 100, -1, List.of(1), List.of(1)));
    }

    @Test
    void rejectsMissingWorkers() {
        assertThrows(IllegalArgumentException.class, () ->
                new PizzeriaConfig(1, 1000, 100, 100, 1, null, List.of(1)));
        assertThrows(IllegalArgumentException.class, () ->
                new PizzeriaConfig(1, 1000, 100, 100, 1, List.of(), List.of(1)));
        assertThrows(IllegalArgumentException.class, () ->
                new PizzeriaConfig(1, 1000, 100, 100, 1, List.of(1), null));
        assertThrows(IllegalArgumentException.class, () ->
                new PizzeriaConfig(1, 1000, 100, 100, 1, List.of(1), List.of()));
    }

    @Test
    void rejectsInvalidBakerAndCourierValues() {
        assertThrows(IllegalArgumentException.class, () ->
                new PizzeriaConfig(1, 1000, 100, 100, 1, List.of(0), List.of(1)));
        assertThrows(IllegalArgumentException.class, () ->
                new PizzeriaConfig(1, 1000, 100, 100, 1, List.of(1), List.of(0)));
    }
}