package ru.nsu.maltsev.task_2_2_1;

import java.util.List;

public class Courier implements Runnable {
    private final int id;
    private final int capacity;
    private final int deliveryTimeMs;
    private final Warehouse warehouse;

    public Courier(int id, int capacity, int deliveryTimeMs, Warehouse warehouse) {
        this.id = id;
        this.capacity = capacity;
        this.deliveryTimeMs = deliveryTimeMs;
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        try {
            while (true) {
                List<Order> batch = warehouse.takeUpTo(capacity);
                if (batch == null) {
                    return;
                }
                for (Order order : batch) {
                    OrderLogger.log(order.getId(), OrderState.TAKEN_BY_COURIER);
                }
                for (Order order : batch) {
                    OrderLogger.log(order.getId(), OrderState.DELIVERING);
                }
                Thread.sleep(deliveryTimeMs);
                for (Order order : batch) {
                    OrderLogger.log(order.getId(), OrderState.DELIVERED);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getId() {
        return id;
    }
}