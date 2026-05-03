package ru.nsu.maltsev.task_2_2_1;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderQueueTest {

    @Test
    void addAndTakeWorksInFifoOrder() throws InterruptedException {
        OrderQueue queue = new OrderQueue();

        assertTrue(queue.add(new Order(1)));
        assertTrue(queue.add(new Order(2)));

        assertEquals(1, queue.take().getId());
        assertEquals(2, queue.take().getId());
    }

    @Test
    void takeReturnsNullAfterCloseWhenQueueEmpty() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        queue.close();

        assertNull(queue.take());
    }

    @Test
    void addReturnsFalseAfterClose() {
        OrderQueue queue = new OrderQueue();
        queue.close();

        assertTrue(!queue.add(new Order(1)));
    }

    @Test
    void takeWaitsUntilOrderAppears() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        AtomicReference<Order> result = new AtomicReference<>();

        Thread thread = new Thread(() -> {
            try {
                result.set(queue.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();

        Thread.sleep(100);
        queue.add(new Order(42));

        thread.join(2000);
        assertEquals(42, result.get().getId());
    }
}