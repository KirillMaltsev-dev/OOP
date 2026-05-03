package ru.nsu.maltsev.task_2_2_1;

public class OrderGenerator implements Runnable {
    private final int ordersCount;
    private final int orderIntervalMs;
    private final int workDurationMs;
    private final OrderQueue orderQueue;

    public OrderGenerator(int ordersCount, int orderIntervalMs, int workDurationMs, OrderQueue orderQueue) {
        this.ordersCount = ordersCount;
        this.orderIntervalMs = orderIntervalMs;
        this.workDurationMs = workDurationMs;
        this.orderQueue = orderQueue;
    }

    @Override
    public void run() {
        long deadline = System.currentTimeMillis() + workDurationMs;
        try {
            for (int id = 1; id <= ordersCount; id++) {
                if (System.currentTimeMillis() > deadline) {
                    break;
                }
                Order order = new Order(id);
                if (!orderQueue.add(order)) {
                    break;
                }
                OrderLogger.log(order.getId(), OrderState.RECEIVED);
                if (orderIntervalMs > 0 && id < ordersCount) {
                    long remaining = deadline - System.currentTimeMillis();
                    if (remaining <= 0) {
                        break;
                    }
                    Thread.sleep(Math.min(orderIntervalMs, remaining));
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            orderQueue.close();
        }
    }
}