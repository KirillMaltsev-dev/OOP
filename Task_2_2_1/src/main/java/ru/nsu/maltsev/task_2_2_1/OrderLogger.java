package ru.nsu.maltsev.task_2_2_1;

public final class OrderLogger {
    private OrderLogger() {
    }

    public static synchronized void log(int orderId, OrderState state) {
        System.out.println(orderId + " " + state.name());
    }
}