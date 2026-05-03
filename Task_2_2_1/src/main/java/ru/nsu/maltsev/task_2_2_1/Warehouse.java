package ru.nsu.maltsev.task_2_2_1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Warehouse {
    private final int capacity;
    private final LinkedList<Order> readyOrders;
    private boolean productionFinished;

    public Warehouse(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be >= 1");
        }
        this.capacity = capacity;
        this.readyOrders = new LinkedList<>();
        this.productionFinished = false;
    }

    public synchronized void put(Order order) throws InterruptedException {
        while (readyOrders.size() >= capacity) {
            wait();
        }
        readyOrders.addLast(order);
        notifyAll();
    }

    public synchronized List<Order> takeUpTo(int maxCount) throws InterruptedException {
        while (readyOrders.isEmpty() && !productionFinished) {
            wait();
        }
        if (readyOrders.isEmpty()) {
            return null;
        }
        int count = Math.min(maxCount, readyOrders.size());
        List<Order> batch = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            batch.add(readyOrders.removeFirst());
        }
        notifyAll();
        return batch;
    }

    public synchronized void closeProduction() {
        productionFinished = true;
        notifyAll();
    }
}