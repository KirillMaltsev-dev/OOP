package ru.nsu.maltsev.task_2_1_2;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DistributedPrimeCheckerTest {

    @Test
    void constructorRejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new DistributedPrimeChecker(null, 2, 1000, 1000));
        assertThrows(IllegalArgumentException.class, () -> new DistributedPrimeChecker(List.of(), 2, 1000, 1000));
        assertThrows(IllegalArgumentException.class, () -> new DistributedPrimeChecker(List.of(new WorkerEndpoint("localhost", 5001)), 0, 1000, 1000));
        assertThrows(IllegalArgumentException.class, () -> new DistributedPrimeChecker(List.of(new WorkerEndpoint("localhost", 5001)), 2, 0, 1000));
        assertThrows(IllegalArgumentException.class, () -> new DistributedPrimeChecker(List.of(new WorkerEndpoint("localhost", 5001)), 2, 1000, 0));
    }

    @Test
    void returnsFalseForEmptyArray() {
        DistributedPrimeChecker checker = new DistributedPrimeChecker(List.of(new WorkerEndpoint("localhost", 5001)), 2, 100, 100);
        assertFalse(checker.hasNonPrime(new int[0]));
    }

    @Test
    void distributedCheckReturnsTrueWhenNonPrimeExists() throws Exception {
        int port1 = findFreePort();
        int port2 = findFreePort();

        DistributedWorker worker1 = new DistributedWorker(port1);
        DistributedWorker worker2 = new DistributedWorker(port2);
        Thread thread1 = startWorker(worker1, port1);
        Thread thread2 = startWorker(worker2, port2);

        try {
            DistributedPrimeChecker checker = new DistributedPrimeChecker(
                    List.of(new WorkerEndpoint("localhost", port1), new WorkerEndpoint("localhost", port2)),
                    2,
                    1000,
                    3000
            );

            assertTrue(checker.hasNonPrime(new int[]{2, 3, 5, 7, 11, 12, 13}));
            assertEquals("Distributed", checker.getMethodName());
        } finally {
            worker1.close();
            worker2.close();
            thread1.join(2000);
            thread2.join(2000);
        }
    }

    @Test
    void distributedCheckReturnsFalseWhenAllNumbersArePrime() throws Exception {
        int port1 = findFreePort();
        int port2 = findFreePort();

        DistributedWorker worker1 = new DistributedWorker(port1);
        DistributedWorker worker2 = new DistributedWorker(port2);
        Thread thread1 = startWorker(worker1, port1);
        Thread thread2 = startWorker(worker2, port2);

        try {
            DistributedPrimeChecker checker = new DistributedPrimeChecker(
                    List.of(new WorkerEndpoint("localhost", port1), new WorkerEndpoint("localhost", port2)),
                    3,
                    1000,
                    3000
            );

            assertFalse(checker.hasNonPrime(new int[]{2, 3, 5, 7, 11, 13, 17, 19}));
        } finally {
            worker1.close();
            worker2.close();
            thread1.join(2000);
            thread2.join(2000);
        }
    }

    @Test
    void redistributesTaskWhenOneWorkerIsUnavailable() throws Exception {
        int livePort = findFreePort();
        int deadPort = findFreePort();

        DistributedWorker worker = new DistributedWorker(livePort);
        Thread liveThread = startWorker(worker, livePort);

        try {
            DistributedPrimeChecker checker = new DistributedPrimeChecker(
                    List.of(new WorkerEndpoint("localhost", deadPort), new WorkerEndpoint("localhost", livePort)),
                    2,
                    200,
                    1000
            );

            assertTrue(checker.hasNonPrime(new int[]{2, 3, 4, 5, 7, 11}));
        } finally {
            worker.close();
            liveThread.join(2000);
        }
    }

    @Test
    void throwsWhenNoWorkersAreReachable() throws Exception {
        int deadPort = findFreePort();

        DistributedPrimeChecker checker = new DistributedPrimeChecker(
                List.of(new WorkerEndpoint("localhost", deadPort)),
                2,
                200,
                500
        );

        assertThrows(IllegalStateException.class, () -> checker.hasNonPrime(new int[]{2, 3, 5, 7, 11, 13}));
    }

    private static Thread startWorker(DistributedWorker worker, int port) {
        Thread thread = new Thread(() -> {
            try {
                worker.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        waitForPort(port);
        return thread;
    }

    private static void waitForPort(int port) {
        long deadline = System.currentTimeMillis() + 3000;
        while (System.currentTimeMillis() < deadline) {
            try (java.net.Socket ignored = new java.net.Socket("localhost", port)) {
                return;
            } catch (IOException e) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException(interruptedException);
                }
            }
        }
        throw new IllegalStateException("Port did not open in time");
    }

    private static int findFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }
}