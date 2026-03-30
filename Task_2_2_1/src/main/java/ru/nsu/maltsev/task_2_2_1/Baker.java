package ru.nsu.maltsev.task_2_2_1;

public class Baker implements Runnable {
    private final int id;
    private final int cookTimeMs;
    private final OrderQueue orderQueue;
    private final Warehouse warehouse;

    public Baker(int id, int cookTimeMs, OrderQueue orderQueue, Warehouse warehouse) {
        this.id = id;
        this.cookTimeMs = cookTimeMs;
        this.orderQueue = orderQueue;
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = orderQueue.take();
                if (order == null) {
                    return;
                }
                OrderLogger.log(order.getId(), OrderState.TAKEN_BY_BAKER);
                OrderLogger.log(order.getId(), OrderState.BAKING);
                Thread.sleep(cookTimeMs);
                OrderLogger.log(order.getId(), OrderState.BAKED);
                warehouse.put(order);
                OrderLogger.log(order.getId(), OrderState.STORED);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getId() {
        return id;
    }
}