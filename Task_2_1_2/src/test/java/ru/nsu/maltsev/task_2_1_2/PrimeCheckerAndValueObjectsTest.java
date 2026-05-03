package ru.nsu.maltsev.task_2_1_2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimeCheckerAndValueObjectsTest {

    private static class TestPrimeChecker extends PrimeChecker {
        @Override
        public boolean hasNonPrime(int[] numbers) {
            for (int number : numbers) {
                if (!isPrime(number)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getMethodName() {
            return "Test";
        }

        boolean checkPrime(int n) {
            return isPrime(n);
        }
    }

    @Test
    void isPrimeHandlesBasicCases() {
        TestPrimeChecker checker = new TestPrimeChecker();

        assertFalse(checker.checkPrime(-10));
        assertFalse(checker.checkPrime(0));
        assertFalse(checker.checkPrime(1));
        assertTrue(checker.checkPrime(2));
        assertTrue(checker.checkPrime(3));
        assertFalse(checker.checkPrime(4));
        assertTrue(checker.checkPrime(97));
        assertFalse(checker.checkPrime(99));
    }

    @Test
    void chunkTaskStoresValues() {
        int[] numbers = {2, 3, 5};
        ChunkTask task = new ChunkTask("task-1", numbers);

        assertEquals("task-1", task.getTaskId());
        assertArrayEquals(numbers, task.getNumbers());
    }

    @Test
    void workerEndpointParsesAndComparesCorrectly() {
        WorkerEndpoint endpoint = WorkerEndpoint.parse(" localhost:5001 ");

        assertEquals("localhost", endpoint.getHost());
        assertEquals(5001, endpoint.getPort());
        assertEquals("localhost:5001", endpoint.toString());
        assertEquals(endpoint, new WorkerEndpoint("localhost", 5001));
        assertEquals(endpoint.hashCode(), new WorkerEndpoint("localhost", 5001).hashCode());
        assertNotEquals(endpoint, new WorkerEndpoint("127.0.0.1", 5001));
        assertNotEquals(endpoint, "localhost:5001");
    }

    @Test
    void workerEndpointRejectsInvalidValues() {
        assertThrows(IllegalArgumentException.class, () -> new WorkerEndpoint(null, 5001));
        assertThrows(IllegalArgumentException.class, () -> new WorkerEndpoint(" ", 5001));
        assertThrows(IllegalArgumentException.class, () -> new WorkerEndpoint("localhost", 0));
        assertThrows(IllegalArgumentException.class, () -> new WorkerEndpoint("localhost", 70000));
        assertThrows(IllegalArgumentException.class, () -> WorkerEndpoint.parse("localhost"));
        assertThrows(IllegalArgumentException.class, () -> WorkerEndpoint.parse(":5001"));
        assertThrows(NumberFormatException.class, () -> WorkerEndpoint.parse("localhost:not-a-port"));
    }

    @Test
    void protocolChecksumIsStableAndSensitiveToData() {
        int[] first = {1, 2, 3, 4};
        int[] same = {1, 2, 3, 4};
        int[] different = {1, 2, 3, 5};

        long firstChecksum = Protocol.checksum(first);
        long sameChecksum = Protocol.checksum(same);
        long differentChecksum = Protocol.checksum(different);

        assertEquals(firstChecksum, sameChecksum);
        assertNotEquals(firstChecksum, differentChecksum);
        assertEquals("CHECK_TASK", Protocol.CHECK_TASK);
        assertEquals("RESULT", Protocol.RESULT);
        assertEquals("ERROR", Protocol.ERROR);
    }
}