package ru.nsu.maltsev.task_2_2_1;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WarehouseTest {

    @Test
    void rejectsInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new Warehouse(0));
    }

    @Test
    void putAndTakeWorks() throws InterruptedException {
        Warehouse warehouse = new Warehouse(3);

        warehouse.put(new Order(1));
        warehouse.put(new Order(2));
        warehouse.put(new Order(3));

        List<Order> batch = warehouse.takeUpTo(2);

        assertEquals(2, batch.size());
        assertEquals(1, batch.get(0).getId());
        assertEquals(2, batch.get(1).getId());
    }

    @Test
    void takeReturnsNullWhenProductionClosedAndEmpty() throws InterruptedException {
        Warehouse warehouse = new Warehouse(2);
        warehouse.closeProduction();

        assertNull(warehouse.takeUpTo(2));
    }

    @Test
    void putWaitsUntilSpaceFreed() throws InterruptedException {
        Warehouse warehouse = new Warehouse(1);
        warehouse.put(new Order(1));

        AtomicBoolean secondPutCompleted = new AtomicBoolean(false);

        Thread putThread = new Thread(() -> {
            try {
                warehouse.put(new Order(2));
                secondPutCompleted.set(true);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        putThread.start();

        Thread.sleep(100);
        assertTrue(!secondPutCompleted.get());

        List<Order> batch = warehouse.takeUpTo(1);
        assertEquals(1, batch.get(0).getId());

        putThread.join(2000);
        assertTrue(secondPutCompleted.get());

        List<Order> nextBatch = warehouse.takeUpTo(1);
        assertEquals(2, nextBatch.get(0).getId());
    }

    @Test
    void takeWaitsUntilReadyOrdersAppear() throws InterruptedException {
        Warehouse warehouse = new Warehouse(2);
        AtomicReference<List<Order>> result = new AtomicReference<>();

        Thread takeThread = new Thread(() -> {
            try {
                result.set(warehouse.takeUpTo(2));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        takeThread.start();

        Thread.sleep(100);
        warehouse.put(new Order(5));

        takeThread.join(2000);
        assertEquals(1, result.get().size());
        assertEquals(5, result.get().get(0).getId());
    }
}