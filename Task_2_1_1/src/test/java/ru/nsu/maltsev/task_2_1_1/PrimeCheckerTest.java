package ru.nsu.maltsev.task_2_1_1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PrimeCheckerTest {

    @Test
    void testIsPrimeBasic() {
        assertTrue(PrimeChecker.isPrime(2));
        assertTrue(PrimeChecker.isPrime(3));
        assertTrue(PrimeChecker.isPrime(5));
        assertTrue(PrimeChecker.isPrime(13));
        assertTrue(PrimeChecker.isPrime(17));
        assertTrue(PrimeChecker.isPrime(19));
        assertTrue(PrimeChecker.isPrime(2147483647)); // Integer.MAX_VALUE

        assertFalse(PrimeChecker.isPrime(4));
        assertFalse(PrimeChecker.isPrime(6));
        assertFalse(PrimeChecker.isPrime(8));
        assertFalse(PrimeChecker.isPrime(9));
        assertFalse(PrimeChecker.isPrime(100));
        assertFalse(PrimeChecker.isPrime(2147483646));
    }

    @Test
    void testIsPrimeEdgeCases() {
        assertFalse(PrimeChecker.isPrime(Integer.MIN_VALUE));
        assertFalse(PrimeChecker.isPrime(-1));
        assertFalse(PrimeChecker.isPrime(0));
        assertFalse(PrimeChecker.isPrime(1));
    }
}