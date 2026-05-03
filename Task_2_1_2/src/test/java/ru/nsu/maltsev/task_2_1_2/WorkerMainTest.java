package ru.nsu.maltsev.task_2_1_2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class WorkerMainTest {

    @Test
    void workerMainRejectsWrongArgumentsCount() {
        assertThrows(IllegalArgumentException.class, () -> WorkerMain.main(new String[0]));
        assertThrows(IllegalArgumentException.class, () -> WorkerMain.main(new String[]{"5001", "extra"}));
    }

    @Test
    void workerMainRejectsInvalidPortValue() {
        assertThrows(NumberFormatException.class, () -> WorkerMain.main(new String[]{"not-a-port"}));
    }
}