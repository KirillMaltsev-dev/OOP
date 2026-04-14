package ru.nsu.maltsev.task_2_2_1;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderGeneratorTest {

    @Test
    void generatesAllOrdersWhenEnoughTime() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        OrderGenerator generator = new OrderGenerator(3, 0, 1000, queue);

        generator.run();

        List<Integer> ids = new ArrayList<>();
        Order order;
        while ((order = queue.take()) != null) {
            ids.add(order.getId());
        }

        assertTrue(ids.size() == 3);
        assertTrue(ids.get(0) == 1);
        assertTrue(ids.get(1) == 2);
        assertTrue(ids.get(2) == 3);
    }

    @Test
    void stopsWhenWorkTimeExpires() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        OrderGenerator generator = new OrderGenerator(10, 200, 50, queue);

        generator.run();

        int count = 0;
        while (queue.take() != null) {
            count++;
        }

        assertTrue(count < 10);
    }
}