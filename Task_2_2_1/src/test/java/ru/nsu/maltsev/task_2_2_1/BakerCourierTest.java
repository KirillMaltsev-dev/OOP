package ru.nsu.maltsev.task_2_2_1;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BakerCourierTest {

    @Test
    void bakerMovesOrdersFromQueueToWarehouse() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(5);

        queue.add(new Order(1));
        queue.add(new Order(2));
        queue.close();

        Baker baker = new Baker(1, 10, queue, warehouse);
        Thread bakerThread = new Thread(baker);
        bakerThread.start();
        bakerThread.join(2000);

        List<Order> batch = warehouse.takeUpTo(5);
        assertEquals(2, batch.size());
        assertEquals(1, batch.get(0).getId());
        assertEquals(2, batch.get(1).getId());

        warehouse.closeProduction();
        assertNull(warehouse.takeUpTo(1));
        assertEquals(1, baker.getId());
    }

    @Test
    void courierTakesOrdersAndStopsWhenProductionClosed() throws InterruptedException {
        Warehouse warehouse = new Warehouse(5);
        warehouse.put(new Order(10));
        warehouse.put(new Order(11));
        warehouse.closeProduction();

        Courier courier = new Courier(7, 2, 10, warehouse);
        Thread courierThread = new Thread(courier);
        courierThread.start();
        courierThread.join(2000);

        assertNull(warehouse.takeUpTo(1));
        assertEquals(7, courier.getId());
    }
}