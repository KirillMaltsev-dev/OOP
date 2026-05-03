package ru.nsu.maltsev.task_2_2_1;

import java.util.LinkedList;

public class OrderQueue {
    private final LinkedList<Order> orders;
    private boolean closed;

    public OrderQueue() {
        this.orders = new LinkedList<>();
        this.closed = false;
    }

    public synchronized boolean add(Order order) {
        if (closed) {
            return false;
        }
        orders.addLast(order);
        notifyAll();
        return true;
    }

    public synchronized Order take() throws InterruptedException {
        while (orders.isEmpty() && !closed) {
            wait();
        }
        if (orders.isEmpty()) {
            return null;
        }
        Order order = orders.removeFirst();
        notifyAll();
        return order;
    }

    public synchronized void close() {
        closed = true;
        notifyAll();
    }
}