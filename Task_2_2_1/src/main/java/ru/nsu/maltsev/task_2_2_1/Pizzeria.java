package ru.nsu.maltsev.task_2_2_1;

import java.util.ArrayList;
import java.util.List;

public class Pizzeria {
    private final PizzeriaConfig config;
    private final OrderQueue orderQueue;
    private final Warehouse warehouse;

    public Pizzeria(PizzeriaConfig config) {
        this.config = config;
        this.orderQueue = new OrderQueue();
        this.warehouse = new Warehouse(config.getWarehouseCapacity());
    }

    public void run() throws InterruptedException {
        List<Thread> bakerThreads = new ArrayList<>();
        List<Thread> courierThreads = new ArrayList<>();

        for (int i = 0; i < config.getBakerCookTimesMs().size(); i++) {
            Baker baker = new Baker(i + 1, config.getBakerCookTimesMs().get(i), orderQueue, warehouse);
            Thread thread = new Thread(baker, "Baker-" + (i + 1));
            bakerThreads.add(thread);
            thread.start();
        }

        for (int i = 0; i < config.getCourierCapacities().size(); i++) {
            Courier courier = new Courier(i + 1, config.getCourierCapacities().get(i), config.getDeliveryTimeMs(), warehouse);
            Thread thread = new Thread(courier, "Courier-" + (i + 1));
            courierThreads.add(thread);
            thread.start();
        }

        Thread generatorThread = new Thread(
                new OrderGenerator(config.getOrdersCount(), config.getOrderIntervalMs(), config.getWorkDurationMs(), orderQueue),
                "OrderGenerator"
        );
        generatorThread.start();
        generatorThread.join();

        for (Thread bakerThread : bakerThreads) {
            bakerThread.join();
        }

        warehouse.closeProduction();

        for (Thread courierThread : courierThreads) {
            courierThread.join();
        }
    }
}