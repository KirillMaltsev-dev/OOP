package ru.nsu.maltsev.task_2_2_1;

import org.junit.jupiter.api.Test;

import java.util.List;

class PizzeriaTest {

    @Test
    void pizzeriaRunCompletes() throws InterruptedException {
        PizzeriaConfig config = new PizzeriaConfig(
                3,
                300,
                20,
                20,
                5,
                List.of(20, 30),
                List.of(2, 1)
        );

        Pizzeria pizzeria = new Pizzeria(config);
        pizzeria.run();
    }
}