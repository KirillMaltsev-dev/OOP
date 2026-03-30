package ru.nsu.maltsev.task_2_2_1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderGeneratorTest {

    @Test
    void generatesAllOrdersWhenEnoughTime() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        OrderGenerator generator = new OrderGenerator(3, 0, 1000, queue);

        generator.run();

        assertEquals(1, queue.take().getId());
        assertEquals(2, queue.take().getId());
        assertEquals(3, queue.take().getId());
        assertNull(queue.take());
    }

    @Test
    void stopsWhenWorkTimeExpires() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        OrderGenerator generator = new OrderGenerator(10, 200, 50, queue);

        generator.run();

        Order first = queue.take();
        if (first != null) {
            Order second = queue.take();
            assertNull(second);
        } else {
            assertNull(queue.take());
        }
    }
}