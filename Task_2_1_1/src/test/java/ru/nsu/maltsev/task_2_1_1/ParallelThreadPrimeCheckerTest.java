package ru.nsu.maltsev.task_2_1_1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParallelThreadPrimeCheckerTest {

    @Test
    void testParallelThreadChecker() {
        assertThrows(IllegalArgumentException.class, () -> new ParallelThreadPrimeChecker(0));
        assertThrows(IllegalArgumentException.class, () -> new ParallelThreadPrimeChecker(-1));

        PrimeChecker checker = new ParallelThreadPrimeChecker(4);
        assertEquals("Parallel (Thread, 4 threads)", checker.getMethodName());

        assertTrue(checker.hasNonPrime(new int[]{4, 2, 3, 5}));

        assertTrue(checker.hasNonPrime(new int[]{2, 3, 5, 4}));

        assertTrue(checker.hasNonPrime(new int[]{2, 4, 3}));

        assertFalse(checker.hasNonPrime(new int[]{2, 3, 5, 7, 11}));

        assertFalse(checker.hasNonPrime(new int[]{}));

        PrimeChecker checker8 = new ParallelThreadPrimeChecker(8);
        assertTrue(checker8.hasNonPrime(new int[]{4, 2, 3}));
    }

    @Test
    void testParallelThreadInterruption() {
        Thread.currentThread().interrupt();

        PrimeChecker checker = new ParallelThreadPrimeChecker(2);
        assertFalse(checker.hasNonPrime(new int[]{2, 3, 5}));

        Thread.interrupted();
    }
}