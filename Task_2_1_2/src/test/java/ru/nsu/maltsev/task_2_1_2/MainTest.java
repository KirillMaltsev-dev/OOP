package ru.nsu.maltsev.task_2_1_2;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MainTest {

    @Test
    void mainPrintsTrueForArrayWithNonPrime() throws Exception {
        int port = findFreePort();
        DistributedWorker worker = new DistributedWorker(port);
        Thread workerThread = startWorker(worker, port);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            Main.main(new String[]{"2", "localhost:" + port, "2,3,4,5,7"});
            assertEquals("true", output.toString().trim());
        } finally {
            System.setOut(originalOut);
            worker.close();
            workerThread.join(2000);
        }
    }

    @Test
    void mainRejectsWrongArgumentsCount() {
        assertThrows(IllegalArgumentException.class, () -> Main.main(new String[0]));
    }

    @Test
    void mainRejectsEmptyWorkersArgument() {
        assertThrows(IllegalArgumentException.class, () -> Main.main(new String[]{"2", " , , ", "2,3,5"}));
    }

    @Test
    void mainRejectsInvalidNumber() {
        assertThrows(NumberFormatException.class, () -> Main.main(new String[]{"2", "localhost:5001", "2,abc,5"}));
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