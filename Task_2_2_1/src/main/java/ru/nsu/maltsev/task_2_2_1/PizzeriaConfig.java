package ru.nsu.maltsev.task_2_2_1;

import java.util.List;

public class PizzeriaConfig {
    private final int warehouseCapacity;
    private final int workDurationMs;
    private final int orderIntervalMs;
    private final int deliveryTimeMs;
    private final int ordersCount;
    private final List<Integer> bakerCookTimesMs;
    private final List<Integer> courierCapacities;

    public PizzeriaConfig(
            int warehouseCapacity,
            int workDurationMs,
            int orderIntervalMs,
            int deliveryTimeMs,
            int ordersCount,
            List<Integer> bakerCookTimesMs,
            List<Integer> courierCapacities
    ) {
        if (warehouseCapacity < 1) {
            throw new IllegalArgumentException("warehouseCapacity must be >= 1");
        }
        if (workDurationMs < 0) {
            throw new IllegalArgumentException("workDurationMs must be >= 0");
        }
        if (orderIntervalMs < 0) {
            throw new IllegalArgumentException("orderIntervalMs must be >= 0");
        }
        if (deliveryTimeMs < 0) {
            throw new IllegalArgumentException("deliveryTimeMs must be >= 0");
        }
        if (ordersCount < 0) {
            throw new IllegalArgumentException("ordersCount must be >= 0");
        }
        if (bakerCookTimesMs == null || bakerCookTimesMs.isEmpty()) {
            throw new IllegalArgumentException("At least one baker is required");
        }
        if (courierCapacities == null || courierCapacities.isEmpty()) {
            throw new IllegalArgumentException("At least one courier is required");
        }
        for (Integer cookTime : bakerCookTimesMs) {
            if (cookTime == null || cookTime < 1) {
                throw new IllegalArgumentException("Baker cook time must be >= 1");
            }
        }
        for (Integer capacity : courierCapacities) {
            if (capacity == null || capacity < 1) {
                throw new IllegalArgumentException("Courier capacity must be >= 1");
            }
        }
        this.warehouseCapacity = warehouseCapacity;
        this.workDurationMs = workDurationMs;
        this.orderIntervalMs = orderIntervalMs;
        this.deliveryTimeMs = deliveryTimeMs;
        this.ordersCount = ordersCount;
        this.bakerCookTimesMs = List.copyOf(bakerCookTimesMs);
        this.courierCapacities = List.copyOf(courierCapacities);
    }

    public int getWarehouseCapacity() {
        return warehouseCapacity;
    }

    public int getWorkDurationMs() {
        return workDurationMs;
    }

    public int getOrderIntervalMs() {
        return orderIntervalMs;
    }

    public int getDeliveryTimeMs() {
        return deliveryTimeMs;
    }

    public int getOrdersCount() {
        return ordersCount;
    }

    public List<Integer> getBakerCookTimesMs() {
        return bakerCookTimesMs;
    }

    public List<Integer> getCourierCapacities() {
        return courierCapacities;
    }
}