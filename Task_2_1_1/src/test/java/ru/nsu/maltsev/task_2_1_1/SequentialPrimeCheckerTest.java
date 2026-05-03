package ru.nsu.maltsev.task_2_1_1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SequentialPrimeCheckerTest {

    @Test
    void testSequentialChecker() {
        PrimeChecker checker = new SequentialPrimeChecker();
        assertEquals("Sequential", checker.getMethodName());

        assertTrue(checker.hasNonPrime(new int[]{2, 3, 5, 8, 11})); // 8 - непростое

        assertFalse(checker.hasNonPrime(new int[]{2, 3, 5, 7, 11}));

        assertFalse(checker.hasNonPrime(new int[]{}));
    }
}