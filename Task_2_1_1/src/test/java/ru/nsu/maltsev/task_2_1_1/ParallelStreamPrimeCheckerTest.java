package ru.nsu.maltsev.task_2_1_1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParallelStreamPrimeCheckerTest {

    @Test
    void testParallelStreamChecker() {
        PrimeChecker checker = new ParallelStreamPrimeChecker();
        assertEquals("ParallelStream", checker.getMethodName());

        assertTrue(checker.hasNonPrime(new int[]{2, 3, 5, 8, 11}));

        assertFalse(checker.hasNonPrime(new int[]{2, 3, 5, 7, 11}));

        assertFalse(checker.hasNonPrime(new int[]{}));
    }
}